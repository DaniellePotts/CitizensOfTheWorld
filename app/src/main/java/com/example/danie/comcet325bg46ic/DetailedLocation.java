package com.example.danie.comcet325bg46ic;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by danie on 13/01/2017.
 */
public class DetailedLocation extends AppCompatActivity implements OnMapReadyCallback {

    TextView name, location, description, price, faveCurrencyPrice, plannedVisit, dateVisited, geoCords;
    ImageView imagePreview;
    CheckBox favourite;
    RatingBar ranking;

    Location detailedLocation = null;

    FloatingActionButton editLocation, deleteLocation, maps, notes;
    Context c = this;

    OnMapReadyCallback onMapReadyCallback = this;
    GoogleMap map;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.detailed_view);

        name = (TextView)findViewById(R.id.nameTxt);
        location = (TextView)findViewById(R.id.locationTxt);
        description = (TextView)findViewById(R.id.descriptionTxt);
        price = (TextView)findViewById(R.id.localCurrency);
        faveCurrencyPrice = (TextView)findViewById(R.id.faveCurrency);
        plannedVisit = (TextView)findViewById(R.id.plannedDate);
        dateVisited = (TextView)findViewById(R.id.dateVisited);
        geoCords = (TextView)findViewById(R.id.coords);

        favourite = (CheckBox)findViewById(R.id.favourite);
        ranking = (RatingBar)findViewById(R.id.ranking);
        imagePreview = (ImageView)findViewById(R.id.imagePreview);

        editLocation = (FloatingActionButton)findViewById(R.id.editBtn);
        deleteLocation = (FloatingActionButton)findViewById(R.id.deleteBtn);
        notes = (FloatingActionButton)findViewById(R.id.notesBtn);
        maps = (FloatingActionButton)findViewById(R.id.mapsBtn);

        GetLocation();
        PopulateFields();
        ViewNotes();
        ViewMap();
    }

    public void ViewNotes(){
        notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder notesDialog = new AlertDialog.Builder(c);
                LayoutInflater li = LayoutInflater.from(getApplicationContext());
                View notesView = li.inflate(R.layout.notes,null);
                notesDialog.setView(notesView);
                final EditText notesTxt = (EditText)notesView.findViewById(R.id.noteTxt);

                notesDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        detailedLocation.Notes = notesTxt.getText().toString();
                        SQLDatabase db = new SQLDatabase(c);
                        db.UpdateLocation(detailedLocation);
                        Toast.makeText(getApplicationContext(),"Notes Updated.",Toast.LENGTH_SHORT).show();
                    }
                }).create().show();

            }
        });
    }
    public void ViewMap(){
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mapsDialog = new AlertDialog.Builder(c);
                LayoutInflater li = LayoutInflater.from(c);
                View mapsView = li.inflate(R.layout.activity_maps, null);
                mapsDialog.setView(mapsView);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

                mapFragment.getMapAsync(onMapReadyCallback);

                final EditText searchTxt = (EditText) mapsView.findViewById(R.id.address);
                Button searchBtn = (Button) mapsView.findViewById(R.id.search);
                searchBtn.setVisibility(View.GONE);
                searchTxt.setVisibility(View.GONE);
                mapsDialog.create().show();
                map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(searchTxt.getText().toString()), Double.parseDouble(searchTxt.getText().toString()))).title(detailedLocation.Location)).showInfoWindow();

                List<Address>coordMatches = null;
                double [] coordsLocation = new double[2];
                coordsLocation[0] = detailedLocation.GeoLocation[0];
                coordsLocation[1] = detailedLocation.GeoLocation[1];
                try {
                    coordMatches = new Geocoder(c).getFromLocation(
                            coordsLocation[1],coordsLocation[0], 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (!coordMatches.isEmpty()) {
                    double [] coords = new double[2];
                    coords[0] = coordMatches.get(0).getLatitude();
                    coords[1] = coordMatches.get(0).getLongitude();
                    map.addMarker(new MarkerOptions().position(new LatLng(coords[0], coords[1])).title(searchTxt.getText().toString())).showInfoWindow();

                    LatLng posLL = new LatLng(coords[0], coords[1]);
                    map.moveCamera(CameraUpdateFactory.newLatLng(posLL));
                }
            }
        });
    }

    public void GetLocation(){
        SQLDatabase db  = new SQLDatabase(this);
        detailedLocation = db.GetLocation((int)getIntent().getSerializableExtra("id"));
    }

    void PopulateFields(){
        name.setText(detailedLocation.Name);
        location.setText(detailedLocation.Location);
        description.setText(detailedLocation.Description);
        price.setText(Double.toString(detailedLocation.Price));
        geoCords.setText("Lat/Lng: " + Double.toString(detailedLocation.GeoLocation[0]) + " , " + detailedLocation.GeoLocation[1]);
        favourite.setChecked(detailedLocation.Favorite);
        SimpleDateFormat sf = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String plannedVisitString = detailedLocation.PlannedVisit != null ? sf.format(detailedLocation.PlannedVisit) : "";
        String dateVisitedString = detailedLocation.DateVisited != null ? sf.format(detailedLocation.DateVisited) : "";

        float floatRanking = detailedLocation.Rank;
        ranking.setRating(floatRanking);
        plannedVisit.setText(plannedVisitString);
        dateVisited.setText(dateVisitedString);
        if(detailedLocation.DrawableImage){
            imagePreview.setImageBitmap(BitmapFactory.decodeResource(getResources(),Integer.parseInt(detailedLocation.FileName)));
        }else{
            SaveLoadImages loadImages = new SaveLoadImages();
            imagePreview.setImageBitmap(loadImages.LoadImage(detailedLocation.FileName));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMyLocationEnabled(true);
        map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        map.getUiSettings().setZoomControlsEnabled(true);
    }
}
