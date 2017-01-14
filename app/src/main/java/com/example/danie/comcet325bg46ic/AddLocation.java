package com.example.danie.comcet325bg46ic;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;
import android.location.Address;
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
 * Created by danie on 12/01/2017.
 */
public class AddLocation extends AppCompatActivity implements OnMapReadyCallback {

    EditText name, location, description, price;

    ImageView previewImage;
    TextView coordsLbl, dateVisitLbl, plannedVisitLbl;
    FloatingActionButton notesFab, mapsFab, pictureFab, setdates, addLocation;
    CheckBox favourite;
    RatingBar ranking;
    Bitmap image;
    double[] coords = new double[2];
    Date plannedDate = null;
    Date visitedDate = null;
    String notes = "";

    Context c = this;
    OnMapReadyCallback mapReadyCallback = this;

    GoogleMap map;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_edit_location);

        name = (EditText) findViewById(R.id.name);
        location = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        price = (EditText)findViewById(R.id.priceTxt);
        previewImage = (ImageView) findViewById(R.id.previewImg);
        coordsLbl = (TextView) findViewById(R.id.geoCoordsLbl);
        dateVisitLbl = (TextView)findViewById(R.id.dateVisitedLbl);
        plannedVisitLbl = (TextView)findViewById(R.id.plannedVisitLbl);

        notesFab = (FloatingActionButton) findViewById(R.id.notesBtn);
        mapsFab = (FloatingActionButton) findViewById(R.id.mapsBtn);
        pictureFab = (FloatingActionButton) findViewById(R.id.photoBtn);
        setdates = (FloatingActionButton) findViewById(R.id.setDates);
        addLocation = (FloatingActionButton)findViewById(R.id.addLocation);

        favourite =(CheckBox)findViewById(R.id.setFavourite);
        ranking = (RatingBar)findViewById(R.id.ranking);

        SetImage();
        SetDate();
        SetNotes();
        SetGeoCoords();
        AddLocation();
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
                        coordsLbl.setText("Coordinates: " + coords[0] + " , " + coords[1]);
                    }
                });
                searchBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<Address>coordMatches = null;
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
    public void AddLocation() {
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().equals("") || location.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"Location requires a name and locaiton value",Toast.LENGTH_LONG).show();
                }else {
                    Location newLocation = new Location();

                    newLocation.Deletable = true;
                    newLocation.Name = name.getText().toString();
                    newLocation.Location = location.getText().toString();
                    newLocation.Description = description.getText().toString();
                    newLocation.SetCoordinates(coords[0], coords[1]);
                    newLocation.PlannedVisit = plannedDate;
                    newLocation.DateVisited = visitedDate;
                    newLocation.Notes = notes;
                    newLocation.Price = Double.parseDouble(price.getText().toString());
                    newLocation.Favorite = favourite.isChecked();
                    newLocation.Rank = ranking.getNumStars();
                    newLocation.Deletable = true;
                    newLocation.Editable = true;
                    if (image != null) {
                        newLocation.FileName = SaveImage(image);
                    } else {
                        newLocation.FileName = Integer.toString(R.drawable.default_icon);
                    }
                    SQLDatabase db = new SQLDatabase(c);
                    db.addLocation(newLocation);
                    Intent intent = new Intent(getApplicationContext(), LocationsList.class);
                    startActivity(intent);
                }
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
                image = (Bitmap) extras.get("data");
                previewImage.setImageBitmap(image);
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

                image = BitmapFactory.decodeFile(filePath);
                previewImage.setImageBitmap(image);
            }
        }
    }

    public void GetDate(final boolean plannedVisit, final boolean dateVisited) {
        AlertDialog.Builder setDateDialog = new AlertDialog.Builder(AddLocation.this);
        final LayoutInflater li = LayoutInflater.from(AddLocation.this);

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

                        dateVisitLbl.setText("Date visited: " + visitedDataString);
                    }
                }
                catch (Exception e){
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
