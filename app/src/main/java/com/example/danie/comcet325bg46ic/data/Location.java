package com.example.danie.comcet325bg46ic.data;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

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
    public int Rank;

    public double ConvertedPrice;

    public String Notes;

    public Date PlannedVisit;
    public Date DateVisited;

    public boolean Favorite;

    public Location(){
        GeoLocation = new double[2];
        Deletable = false;
        Price = 0.0;
        Favorite = false;
        //PlannedVisit = new Date();
        //DateVisited = new Date();
    }

    public void SetCoordinates(double lat, double lng){
        GeoLocation[0] = lat;
        GeoLocation[1] = lng;
    }
    public String toString(){
        return ""; //TODO: UPDATE
    }
}
