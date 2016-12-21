package com.example.danie.comcet325bg46ic;

import android.content.Context;
import android.database.Cursor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class LocationCursorAdapter extends CursorAdapter {
    public static final String LOCATION_NAME = "name";
    public static final String LOCATION = "location";

    public LocationCursorAdapter(Context context, Cursor c, int flags){super(context,c,flags);}
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.places_to_visit_list,parent,false);
    }
    public LocationCursorAdapter(Context context, Cursor c){
        super(context,c,0);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(LOCATION_NAME));
        String location = cursor.getString(cursor.getColumnIndex("price"));

        TextView nameTxt = (TextView)view.findViewById(R.id.locationText);
        TextView priceTxt = (TextView)view.findViewById(R.id.priceTxt);
        nameTxt.setText(name);
        priceTxt.setText(location);
    }
}
