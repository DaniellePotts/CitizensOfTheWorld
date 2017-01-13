package com.example.danie.comcet325bg46ic.activities;

import android.content.Context;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;

import android.app.LoaderManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.AddLocation;
import com.example.danie.comcet325bg46ic.EditLocation;
import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.CurrencyCodes;
import com.example.danie.comcet325bg46ic.data.DatabaseConfigData;
import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.data.LocationCursorAdapter;
import com.example.danie.comcet325bg46ic.helpers.ImageGetIntent;
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
import java.util.Calendar;
import java.util.List;

public class LocationsList extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks<Cursor> {

    Uri uri;
    CursorAdapter cursorAdapter;
    ListView lvItems;
    Context c = this;
    Bitmap imageOverwrite;
    ImageView image;
    RadioGroup get_image;
    RadioButton takePhoto;
    RadioButton selectPhoto;
    RelativeLayout relativeLayout = null;
    CurrencyCodes defaultCurrency;
    GoogleMap map;
    OnMapReadyCallback mapReadyCallback = this;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_to_visit_activity);
        relativeLayout = (RelativeLayout) findViewById(R.id.locationList);
        registerForContextMenu(relativeLayout);
        cursorAdapter = new LocationCursorAdapter(this, null, 0);
        PopulateListView(null, null, 5);
        final LayoutInflater li = LayoutInflater.from(LocationsList.this);

        final SQLDatabase db = new SQLDatabase(this);
        defaultCurrency = CurrencyCodes.JPY;
        AddLocationUI();

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                View getEmpIdView = li.inflate(R.layout.detailed_location, null);
                final Location l = db.GetLocation((int) parent.getItemIdAtPosition(position));
                final FloatingActionButton edit_fab = (FloatingActionButton) getEmpIdView.findViewById(R.id.edit_button);
                final FloatingActionButton notes_fab = (FloatingActionButton) getEmpIdView.findViewById(R.id.notes);

                final SQLDatabase db = new SQLDatabase(c);
                final LayoutInflater li = LayoutInflater.from(LocationsList.this);
                final AlertDialog.Builder alertDiaglogBuilder = new AlertDialog.Builder(LocationsList.this);
                alertDiaglogBuilder.setView(getEmpIdView);

                final TextView name = (TextView) getEmpIdView.findViewById(R.id.nameTxt);
                final TextView location = (TextView) getEmpIdView.findViewById(R.id.locationTxt);
                final TextView description = (TextView) getEmpIdView.findViewById(R.id.descriptionTxt);
                final TextView price = (TextView) getEmpIdView.findViewById(R.id.priceTxt);
                final RatingBar favourite = (RatingBar) getEmpIdView.findViewById(R.id.ratingBar);
                final TextView dateVisited = (TextView) getEmpIdView.findViewById(R.id.dateVisitedLbl);
                final TextView plannedVisit = (TextView) getEmpIdView.findViewById(R.id.plannedVisitLbl);
                final CheckBox plannedCheckBox = (CheckBox) getEmpIdView.findViewById(R.id.plannedCheck);
                final CheckBox visitedCheckBox = (CheckBox) getEmpIdView.findViewById(R.id.visitedCheck);

                if (l.Deletable) {
                    FloatingActionButton delete_fab = (FloatingActionButton) getEmpIdView.findViewById(R.id.delete_button);
                    delete_fab.setVisibility(View.VISIBLE);
                    delete_fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDelete = new AlertDialog.Builder(LocationsList.this);
                            alertDelete.setMessage("Delete " + l.Name + "?");
                            alertDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.DeleteLocation(l);
                                    finish();
                                }
                            }).create().show(); //TODO: set negative/cancel value :)
                        }
                    });
                }

                plannedVisit.setText(l.PlannedVisit != null ? "Planned visit: " + l.PlannedVisit.toString() : "");
                dateVisited.setText(l.DateVisited != null ? "Date visited:" + l.DateVisited.toString() : "");
                plannedCheckBox.setChecked(l.PlannedVisit != null ? true : false);
                visitedCheckBox.setChecked(l.DateVisited != null ? true : false);

                if (!plannedCheckBox.isChecked()) {
                    visitedCheckBox.setEnabled(false);
                }
                visitedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                        if (visitedCheckBox.isChecked()) {
                            AlertDialog.Builder defaultDate = new AlertDialog.Builder(c);
                            defaultDate.setPositiveButton("Set Default Date", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Calendar calendar = Calendar.getInstance();
                                    l.DateVisited = calendar.getTime();
                                    dateVisited.setText(l.DateVisited.toString());
                                }
                            }).setNeutralButton("Set Custom Date", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    GetDate(l, dateVisited, false, true);
                                }
                            }).create().show();
                        }
                    }
                });

                plannedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (plannedCheckBox.isChecked()) {
                            GetDate(l, plannedVisit, true, false);
                            visitedCheckBox.setEnabled(true);
                        } else if (!plannedCheckBox.isChecked()) {
                            visitedCheckBox.setEnabled(false);
                            visitedCheckBox.setChecked(false);
                        }
                    }
                });
                favourite.setRating(l.Favorite ? 1 : 0);
                name.setText(l.Name);
                location.setText(l.Location);
                description.setText(l.Description);
                price.setText(Double.toString(l.Price));

                favourite.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        l.Favorite = favourite.getRating() == 1 ? true : false;
                        Toast.makeText(getApplicationContext(), (favourite.getRating() == 1 ? "Favourited " : "Unfavourited ") + l.Name, Toast.LENGTH_LONG).show();
                        db.UpdateLocation(l);
                    }
                });
                if (l.Image != null) {
                    final ImageView locationImage = (ImageView) getEmpIdView.findViewById(R.id.locationImage);
                    locationImage.setImageBitmap(l.Image);

                    locationImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final LayoutInflater li = LayoutInflater.from(LocationsList.this);
                            View fullSizeImageView = li.inflate(R.layout.full_size_image, null);
                            AlertDialog.Builder fullSizeImageDialog = new AlertDialog.Builder(c);
                            fullSizeImageDialog.setView(fullSizeImageView);
                            ImageView fullSizeImage = (ImageView) fullSizeImageView.findViewById(R.id.fullSizeImage);
                            fullSizeImage.setImageBitmap(l.Image);
                            fullSizeImageDialog.create().show();
                        }
                    });
                }

                alertDiaglogBuilder.create().show();

                edit_fab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent editActivity = new Intent(getApplicationContext(),EditLocation.class);
                        editActivity.putExtra("id",l.ID);
                        startActivity(editActivity);
                        View getEditView = li.inflate(R.layout.edit_location, null);
                        final AlertDialog.Builder editLocationDialog = new AlertDialog.Builder(LocationsList.this);

                        editLocationDialog.setView(getEditView);

                        final EditText name = (EditText) getEditView.findViewById(R.id.nameTxt);
                        final EditText location = (EditText) getEditView.findViewById(R.id.locationTxt);
                        final EditText description = (EditText) getEditView.findViewById(R.id.descriptionTxt);
                        final EditText price = (EditText) getEditView.findViewById(R.id.priceTxt);
                        image = (ImageView) getEditView.findViewById(R.id.locationImage);
                        final Button btnGetImage = (Button) getEditView.findViewById(R.id.btnGetImage);
                        final Button btnSetLocation = (Button)getEditView.findViewById(R.id.setLocation);
                        get_image = (RadioGroup) getEditView.findViewById(R.id.get_image);
                        selectPhoto = (RadioButton) getEditView.findViewById(R.id.rdoUpload);
                        takePhoto = (RadioButton) getEditView.findViewById(R.id.rdoTakePic);

                        btnSetLocation.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                AlertDialog.Builder mapDialog = new AlertDialog.Builder(getApplicationContext());
                                View mapView = li.inflate(R.layout.activity_maps,null);
                                mapDialog.setView(mapView);

                                final EditText txtSearch = (EditText)mapView.findViewById(R.id.address);
                                final Button search = (Button)mapView.findViewById(R.id.search);

                                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                        .findFragmentById(R.id.map);
                                mapFragment.getMapAsync(mapReadyCallback);

                                final double [] geoLoc = new double[2];
                                lat = l.GeoLocation[0];
                                lng = l.GeoLocation[1];

                                txtSearch.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        List<Address> geocodeMatches = null;
                                        if (txtSearch.getText().toString().equals("")) {
                                            Toast.makeText(getApplicationContext(),"No location was entered.",Toast.LENGTH_LONG).show();
                                        } else {
                                            try {
                                                geocodeMatches = new Geocoder(getApplicationContext()).getFromLocationName(
                                                        search.getText().toString(), 1);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                            if (!geocodeMatches.isEmpty()) {
                                                geoLoc[0] = geocodeMatches.get(0).getLatitude();
                                                geoLoc[1] = geocodeMatches.get(0).getLongitude();
                                                map.addMarker(new MarkerOptions().position(
                                                        new LatLng(geoLoc[0], geoLoc[1])).title(search.getText().toString())).showInfoWindow();

                                                LatLng posLL = new LatLng(geoLoc[0], geoLoc[1]);
                                                map.moveCamera(CameraUpdateFactory.newLatLng(posLL));
                                            }
                                        }
                                    }
                                });

                                mapDialog.setPositiveButton("Set Location", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        l.GeoLocation[0] = geoLoc[0];
                                        l.GeoLocation[1] = geoLoc[1];
                                    }
                                });
                                mapDialog.create().show();
                            }
                        });
                        btnGetImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = ImageGetIntent.SetImageIntent(takePhoto.isChecked(), selectPhoto.isChecked());
                                if (intent != null) {
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(intent, ImageGetIntent.ActivityCode);
                                    }
                                }
                                if (getParent() == null) {
                                    setResult(RESULT_OK, intent);

                                } else {
                                    getParent().setResult(RESULT_OK, intent);
                                }
                            }
                        });

                        name.setText(l.Name);

                        location.setText(l.Location);
                        location.setText(l.Description);
                        price.setText(Double.toString(l.Price));
                        if (l.Image != null) {
                            image.setImageBitmap(l.Image);
                        }
                        editLocationDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                l.Name = name.getText().toString();
                                l.Location = location.getText().toString();
                                l.Description = description.getText().toString();
                                l.Price = Double.parseDouble(price.getText().toString());

                                if (imageOverwrite != null) {
                                    l.FileName = SaveImage(imageOverwrite);
                                }
                                db.UpdateLocation(l);
                                Toast.makeText(c, "Location Updated.", Toast.LENGTH_LONG).show();
                            }
                        }).create().show();
                    }
                });

                notes_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View notesView = li.inflate(R.layout.notes, null);
                        AlertDialog.Builder notesDialog = new AlertDialog.Builder(LocationsList.this);
                        notesDialog.setView(notesView);
                        final EditText notesText = (EditText) notesView.findViewById(R.id.noteTxt);

                        notesText.setText(l.Notes);
                        notesDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                l.Notes = notesText.getText().toString();
                                db.UpdateLocation(l);
                            }
                        }).create().show();
                    }
                });

                return true;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void PopulateListView(String orderBy, CurrencyCodes code, int recordLimit) {
        String query = "SELECT * FROM " + DatabaseConfigData.TABLE_NAME;
        if (orderBy != null) {
            query += " " + orderBy;
        }
        if (recordLimit > -1) {
            query += " LIMIT " + recordLimit;
        }
        SQLDatabase helper = new SQLDatabase(this);
        Cursor data = helper.OrderQuery(query.toUpperCase());
        lvItems = (ListView) findViewById(android.R.id.list);
        if (code != null && code == defaultCurrency) {
            Toast.makeText(getApplicationContext(), "Currency is already in " + code.toString(), Toast.LENGTH_SHORT).show();
        } else {
            LocationCursorAdapter adapter = new LocationCursorAdapter(this, data, code, defaultCurrency);
            lvItems.setAdapter(adapter);
            SwapCursor(adapter.getCursor());
            adapter.notifyDataSetChanged();

            defaultCurrency = code;
        }
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
                image.setImageBitmap(imageOverwrite);
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
                image.setImageBitmap(imageOverwrite);
            }
        }
    }

    public void SwapCursor(Cursor c) {
        cursorAdapter.swapCursor(c);
    }

    public void AddLocationUI() {
        final FloatingActionButton addLocation = (FloatingActionButton) findViewById(R.id.add_location);
        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),AddLocation.class);
                startActivity(intent);
            }
        });
    }

    public void ReloadCursor(Cursor c) {
        cursorAdapter.changeCursor(c);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    String QueryRequest = null;
    int limitRequest = 5;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sortByAscName:
                QueryRequest = "ORDER BY NAME ASC";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByAscLocation:
                QueryRequest = "ORDER BY NAME ASC";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByAscRank:
                QueryRequest = "ORDER BY RANK ASC";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByDescName:
                QueryRequest = "ORDER BY NAME DESC";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByDescLocation:
                QueryRequest = "ORDER BY LOCATION DESC";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByDescRank:
                QueryRequest = "ORDER BY RANK DESC";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByFavourite:
                QueryRequest = "WHERE favourite == 1";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByPlanned:
                QueryRequest = "WHERE planned_visit is not null order by planned_visit";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByVisited:
                QueryRequest = "WHERE date_visited is not null order by date_visited";
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.sortByDefault:
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.jpy:
                PopulateListView(QueryRequest, CurrencyCodes.JPY, limitRequest);
                return true;
            case R.id.usd:
                PopulateListView(QueryRequest, CurrencyCodes.USD, limitRequest);
                return true;
            case R.id.gbp:
                PopulateListView(QueryRequest, CurrencyCodes.GBP, limitRequest);
                return true;
            case R.id.eur:
                PopulateListView(QueryRequest, CurrencyCodes.EUR, limitRequest);
                return true;
            case R.id.display10Records:
                limitRequest = 10;
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.display5Records:
                limitRequest = 5;
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.displayAllRecords:
                limitRequest = -1;
                PopulateListView(QueryRequest, null, limitRequest);
                return true;
            case R.id.faveCurrency:
                return true;
        }
        return false;
    }

    public void GetDate(final Location l, final TextView textView, final boolean plannedVisit, final boolean dateVisited) {
        AlertDialog.Builder setDateDialog = new AlertDialog.Builder(LocationsList.this);
        final LayoutInflater li = LayoutInflater.from(LocationsList.this);

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

                if (plannedVisit) {
                    l.PlannedVisit = calendar.getTime();
                } else if (dateVisited) {
                    l.DateVisited = calendar.getTime();
                }

                if (textView != null) {
                    textView.setText(dateVisited ? l.DateVisited.toString() : l.PlannedVisit.toString());
                }

                SQLDatabase db = new SQLDatabase(c);
                db.UpdateLocation(l);
            }
        }).create().show();
    }

    public void SetFavouriteCurrency() {

    }

    double lat = 0.0;
    double lng = 0.0;

    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        LatLng loc = new LatLng(lat != 0.0 ? lat : -34, lng != 0.0 ? lng : 151);
        map.addMarker(new MarkerOptions().position(loc));
        map.moveCamera(CameraUpdateFactory.newLatLng(loc));
    }
}
