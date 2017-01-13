package com.example.danie.comcet325bg46ic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.activities.LocationsList;
import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.helpers.ImageGetIntent;
import com.example.danie.comcet325bg46ic.helpers.PopulateDatabase;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by danie on 13/01/2017.
 */
public class EditLocation extends AppCompatActivity implements OnMapReadyCallback{

    Location editableLocation = null;

    EditText name, location, description, price;
    TextView plannedVisitLbl, dateVisitedLbl,geoLocation;
    ImageView previewImage;
    Bitmap imageOverwrite;
    CheckBox favourite;
    RatingBar ranking;

    FloatingActionButton notesFab, mapsFab, pictureFab, setdates, editLocation;

    GoogleMap map;

    double [] coords = new double[2];
    String notes = "";
    Context c = this;
    OnMapReadyCallback mapReadyCallback = this;

    Date plannedDate = null;
    Date visitedDate = null;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_edit_location);

        GetLocation();

        name = (EditText)findViewById(R.id.name);
        location = (EditText)findViewById(R.id.location);
        description = (EditText)findViewById(R.id.description);
        price = (EditText)findViewById(R.id.priceTxt);
        plannedVisitLbl = (TextView)findViewById(R.id.plannedVisitLbl);
        dateVisitedLbl = (TextView)findViewById(R.id.dateVisitedLbl);
        geoLocation = (TextView)findViewById(R.id.geoCoordsLbl);

        previewImage = (ImageView)findViewById(R.id.previewImg);

        notesFab = (FloatingActionButton) findViewById(R.id.notesBtn);
        mapsFab = (FloatingActionButton) findViewById(R.id.mapsBtn);
        pictureFab = (FloatingActionButton) findViewById(R.id.photoBtn);
        setdates = (FloatingActionButton) findViewById(R.id.setDates);
        editLocation = (FloatingActionButton)findViewById(R.id.addLocation);

        notesFab = (FloatingActionButton) findViewById(R.id.notesBtn);
        mapsFab = (FloatingActionButton) findViewById(R.id.mapsBtn);
        pictureFab = (FloatingActionButton) findViewById(R.id.photoBtn);
        setdates = (FloatingActionButton) findViewById(R.id.setDates);
        editLocation = (FloatingActionButton)findViewById(R.id.addLocation);

        favourite =(CheckBox)findViewById(R.id.setFavourite);
        ranking = (RatingBar)findViewById(R.id.ranking);

        PopulateData();

        SetImage();
        SetDate();
        SetNotes();
        SetGeoCoords();
        EditLocation();
    }

    public void PopulateData(){
        name.setText(editableLocation.Name);
        location.setText(editableLocation.Location);
        description.setText(editableLocation.Description);
        price.setText(Double.toString(editableLocation.Price));
        SaveLoadImages loadImages = new SaveLoadImages();
        imageOverwrite = loadImages.LoadImage(editableLocation.FileName);
        previewImage.setImageBitmap(imageOverwrite);
        notes = editableLocation.Notes;
        plannedVisitLbl.setText(editableLocation.PlannedVisit != null ? editableLocation.PlannedVisit.toString() : "Planned Visit: ");
        dateVisitedLbl.setText(editableLocation.DateVisited != null ? editableLocation.DateVisited.toString() : "Visit Date: ");
        geoLocation.setText(Double.toString(editableLocation.GeoLocation[0]) + " , " + Double.toString(editableLocation.GeoLocation[1]));
        if(editableLocation.Favorite){
            favourite.setChecked(true);
        }

        ranking.setRating((float)editableLocation.Rank);
    }

    public void GetLocation(){
        int id = getIntent().getIntExtra("id",-1);

        if(id > -1){
            SQLDatabase db = new SQLDatabase(this);
            editableLocation = db.GetLocation(id);
        }
    }

    public void SetGeoCoords()
    {
        mapsFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mapsDialog = new AlertDialog.Builder(c);
                LayoutInflater li = LayoutInflater.from(c);
                View mapsView = li.inflate(R.layout.activity_maps,null);
                mapsDialog.setView(mapsView);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                mapFragment.getMapAsync(mapReadyCallback);

                final EditText searchTxt = (EditText)mapsView.findViewById(R.id.address);
                Button searchBtn = (Button)mapsView.findViewById(R.id.search);
                FloatingActionButton setCoords = (FloatingActionButton)mapsView.findViewById(R.id.setCoords);

                mapsDialog.create().show();

                setCoords.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        geoLocation.setText("Coordinates: " + coords[0] + " , " + coords[1]);
                    }
                });
                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Address> coordMatches = null;
                        if (searchTxt.getText().toString().equals("")) {
                            Toast.makeText(getApplicationContext(),"No location was entered.",Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                coordMatches = new Geocoder(c).getFromLocationName(
                                        searchTxt.getText().toString(), 1);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (!coordMatches.isEmpty()) {
                                coords[0] = coordMatches.get(0).getLatitude();
                                coords[1] = coordMatches.get(0).getLongitude();
                                map.addMarker(new MarkerOptions().position(new LatLng(coords[0], coords[1])).title(searchTxt.getText().toString())).showInfoWindow();

                                LatLng posLL = new LatLng(coords[0], coords[1]);
                                map.moveCamera(CameraUpdateFactory.newLatLng(posLL));
                            }
                        }
                    }
                });
            }
        });
    }

    public void SetNotes(){
        notesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder notesDialog = new AlertDialog.Builder(c);
                LayoutInflater li = LayoutInflater.from(c);
                View notesView = li.inflate(R.layout.notes,null);
                notesDialog.setView(notesView);

                final EditText notesTxt = (EditText)notesView.findViewById(R.id.noteTxt);
                if(!notes.matches("")){
                    notesTxt.setText(notes);
                }
                notesDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        notes = notesTxt.getText().toString();
                    }
                }).setCancelable(true).create().show();
            }
        });
    }

    public void SetDate(){
        setdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder datesDialog = new AlertDialog.Builder(c);
                datesDialog.setNeutralButton("Set Planned Date", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        GetDate(true,false);
                    }
                }).setPositiveButton("Set Visited Date", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        GetDate(false,true);
                    }
                }).create().show();

            }
        });
    }

    public void SetImage() {
        pictureFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder setImage = new AlertDialog.Builder(c);

                setImage.setNeutralButton("Get from Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = ImageGetIntent.SetImageIntent(false, true);
                        startActivityForResult(intent, 3);

                        GetImageIntentResult(intent);
                    }
                }).setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = ImageGetIntent.SetImageIntent(true, false);
                        startActivityForResult(intent, 2);
                        GetImageIntentResult(intent);

                    }
                }).create().show();
            }
        });
    }

    public void GetImageIntentResult(Intent intent){
        if(intent != null){
            if (getParent() == null) {
                setResult(RESULT_OK, intent);

            } else {
                getParent().setResult(RESULT_OK, intent);
            }
        }
    }

    public void EditLocation() {

        editLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editableLocation.Name = name.getText().toString();
                editableLocation.Location = location.getText().toString();
                editableLocation.Description = description.getText().toString();
                if(coords[0] != 0.0) {
                    editableLocation.SetCoordinates(coords[0], coords[1]);
                }
                editableLocation.PlannedVisit = plannedDate;
                editableLocation.DateVisited = visitedDate;
                editableLocation.Notes = notes;
                editableLocation.Price = Double.parseDouble(price.getText().toString());
                if(imageOverwrite != null){
                    SaveLoadImages loadImages = new SaveLoadImages();
                    loadImages.DeleteImage(editableLocation.FileName);
                    editableLocation.FileName = SaveImage(imageOverwrite);
                }

                SQLDatabase db = new SQLDatabase(c);
                db.UpdateLocation(editableLocation);
                Intent intent = new Intent(getApplicationContext(),LocationsList.class);
                startActivity(intent);
            }
        });
    }

    public String SaveImage(Bitmap b) {
        OutputStream output;
        File filePath = getFilesDir();
        File dir = new File(filePath.getAbsolutePath() + "/CitizensoftheWorld");

        try {
            dir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SaveLoadImages saveLoadImages = new SaveLoadImages();
        String fileName = saveLoadImages.setFileName();
        File file = new File(dir, fileName);

        try {
            output = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

        return fileName;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageOverwrite = (Bitmap) extras.get("data");
                previewImage.setImageBitmap(imageOverwrite);
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                imageOverwrite = BitmapFactory.decodeFile(filePath);
                previewImage.setImageBitmap(imageOverwrite);
            }
        }
    }

    public void GetDate(final boolean plannedVisit, final boolean dateVisited) {
        AlertDialog.Builder setDateDialog = new AlertDialog.Builder(EditLocation.this);
        final LayoutInflater li = LayoutInflater.from(EditLocation.this);

        View datetimeview = li.inflate(R.layout.time_date_picker, null);
        setDateDialog.setView(datetimeview);
        final DatePicker datePicker = (DatePicker) datetimeview.findViewById(R.id.datePicker);
        final TimePicker timePicker = (TimePicker) datetimeview.findViewById(R.id.timePicker);
        setDateDialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int day = datePicker.getDayOfMonth();
                int month = datePicker.getMonth();
                int year = datePicker.getYear();
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day);
                calendar.set(Calendar.HOUR, timePicker.getCurrentHour());
                calendar.set(Calendar.MINUTE, timePicker.getCurrentMinute());

                try {
                    SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
                    if (plannedVisit) {
                        plannedDate = calendar.getTime();
                        String plannedDataString = sf.format(plannedDate);
                        plannedVisitLbl.setText("Planned Visit: " + plannedDataString);
                    } else if (dateVisited) {
                        visitedDate = calendar.getTime();
                        String visitedDataString = sf.format(visitedDate);

                        dateVisitedLbl.setText("Date visited: " + visitedDataString);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).create().show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
    }
}
