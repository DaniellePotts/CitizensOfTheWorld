package com.example.danie.comcet325bg46ic;

import android.content.Context;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import android.app.LoaderManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.data.LocationCursorAdapter;


/**
 * Created by danie on 18/12/2016.
 */
public class LocationsList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri uri;
    CursorAdapter cursorAdapter;
    ListView lvItems;
    Context c = this;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_to_visit_activity);
        cursorAdapter = new LocationCursorAdapter(this, null, 0);
        PopulateListView();

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object val = parent.getAdapter().getItem(position);
                LayoutInflater li = LayoutInflater.from(LocationsList.this);
                View getEmpIdView = li.inflate(R.layout.detailed_location,null);
                Object obj = parent.getItemAtPosition(position);
                SQLDatabase db = new SQLDatabase(c);
                Location l = db.getLocation((int) parent.getItemIdAtPosition(position));

                AlertDialog.Builder alertDiaglogBuilder = new AlertDialog.Builder(LocationsList.this);
                alertDiaglogBuilder.setView(getEmpIdView);

                final TextView name = (TextView)getEmpIdView.findViewById(R.id.nameTxt);
                final TextView location = (TextView)getEmpIdView.findViewById(R.id.locationTxt);
                final TextView description = (TextView)getEmpIdView.findViewById(R.id.descriptionTxt);
                final TextView price = (TextView)getEmpIdView.findViewById(R.id.priceTxt);

                name.setText(l.Name);
                location.setText(l.Location);
                description.setText(l.Description);
                price.setText(Double.toString(l.Price));
                if(l.Image != null) {
                    final ImageView locationImage = (ImageView) getEmpIdView.findViewById(R.id.locationImage);
                    locationImage.setImageBitmap(l.Image);
                }

                alertDiaglogBuilder.create().show();

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

    private void PopulateListView() {
        SQLiteDatabase db;
        SQLDatabase helper = new SQLDatabase(this);
        db = helper.getWritableDatabase();
        Cursor todo = db.rawQuery("SELECT * FROM locations", null);
        Log.v("words", "hhio");
        lvItems = (ListView) findViewById(android.R.id.list);
        LocationCursorAdapter adapter = new LocationCursorAdapter(this, todo);
        lvItems.setAdapter(adapter);
    }
}
