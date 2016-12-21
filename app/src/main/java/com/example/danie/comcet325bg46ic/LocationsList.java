package com.example.danie.comcet325bg46ic;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by danie on 18/12/2016.
 */
public class LocationsList extends AppCompatActivity {

    SQLDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_to_visit_activity);
        db = new SQLDatabase(this);

        List<Location> locs = db.GetAll();
    }
}
