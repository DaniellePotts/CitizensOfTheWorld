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
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.AddLocation;
import com.example.danie.comcet325bg46ic.DetailedLocation;
import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.Currency;
import com.example.danie.comcet325bg46ic.data.CurrencyCodes;
import com.example.danie.comcet325bg46ic.data.DatabaseConfigData;
import com.example.danie.comcet325bg46ic.data.LocationCursorAdapter;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;

public class LocationsList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Uri uri;
    CursorAdapter cursorAdapter;
    ListView lvItems;
    Context c = this;
    RelativeLayout relativeLayout = null;
    CurrencyCodes defaultCurrency;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_to_visit_activity);
        relativeLayout = (RelativeLayout) findViewById(R.id.locationList);
        registerForContextMenu(relativeLayout);
        cursorAdapter = new LocationCursorAdapter(this, null, 0,getResources());
        PopulateListView(null, null, 10);
        final LayoutInflater li = LayoutInflater.from(LocationsList.this);

        final SQLDatabase db = new SQLDatabase(this);
        defaultCurrency = CurrencyCodes.JPY;
        AddLocationUI();

        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detailedView = new Intent(c,DetailedLocation.class);
                detailedView.putExtra("id",((int) parent.getItemIdAtPosition(position)));
                startActivity(detailedView);
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
            LocationCursorAdapter adapter = new LocationCursorAdapter(this, data, code, defaultCurrency,getResources());
            lvItems.setAdapter(adapter);
            SwapCursor(adapter.getCursor());
            adapter.notifyDataSetChanged();

            defaultCurrency = code;
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
                AlertDialog.Builder faveCurrencySet = new AlertDialog.Builder(this);
                return true;
        }
        return false;
    }

    Currency faveCurrency = new Currency();


    public void SetFavouriteCurrency() {

    }

}
