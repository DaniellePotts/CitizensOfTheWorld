package com.example.danie.comcet325bg46ic.data;

import android.graphics.Bitmap;

/**
 * Created by bg46ic on 06/12/2016.
 */
public class Location {
    public int ID;
    public String Name;
    public String Location;
    public String Description;
    public Bitmap Image;
    public String FileName;
    public double [] GeoLocation;
    public double Price;
    public boolean Deletable;

    public Location(){
        GeoLocation = new double[2];
        Deletable = true;
    }

    public String toString(){
        return ""; //TODO: UPDATE
    }
}
