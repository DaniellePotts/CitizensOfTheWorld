package com.example.danie.comcet325bg46ic.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.danie.comcet325bg46ic.data.Location;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by bg46ic on 06/12/2016.
 */
public class SQLDatabase extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "locations";
    private static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_LOCATION = "location";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMAGE = "image";
    public static final String COLUMN_GEOLOCATION = "geolocation";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_DELETABLE = "deletable";
    public static final String COLUMN_PLANNED_VISIT = "planned_visit";
    public static final String COLUMN_DATE_VISITED = "date_visited";
    public static final String COLUMN_NOTES = "notes";
    public static final String COLUMN_FAVOURITE = "favourite";

    public static final int DATABASE_VERSION = 16;
    public static final String DATABASE = "LocationsDatabase";

    public static final String[] COLUMNS = {COLUMN_ID, COLUMN_NAME, COLUMN_LOCATION, COLUMN_DESCRIPTION, COLUMN_IMAGE, COLUMN_GEOLOCATION, COLUMN_PRICE, COLUMN_DELETABLE, COLUMN_PLANNED_VISIT, COLUMN_DATE_VISITED, COLUMN_NOTES, COLUMN_FAVOURITE};

    public SQLDatabase(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        String CREATE_DATABASE = "" +
                "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME + " TEXT," +
                COLUMN_LOCATION + " TEXT," +
                COLUMN_DESCRIPTION + " TEXT," +
                COLUMN_IMAGE + " TEXT," +
                COLUMN_GEOLOCATION + " TEXT," +
                COLUMN_PRICE + " DOUBLE," +
                COLUMN_DELETABLE + " INTEGER," +
                COLUMN_PLANNED_VISIT + " TEXT," +
                COLUMN_DATE_VISITED + " TEXT," +
                COLUMN_NOTES + " TEXT," +
                COLUMN_FAVOURITE + " INTEGER)";

        db.execSQL(CREATE_DATABASE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        this.onCreate(db);
    }

    public void addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        String path = db.getPath();

        ContentValues values = new ContentValues();

        String geoLocation = ParseGeoLocation(location.GeoLocation);

        values.put(COLUMN_NAME, location.Name);
        values.put(COLUMN_LOCATION, location.Location);
        values.put(COLUMN_DESCRIPTION, location.Description);
        values.put(COLUMN_IMAGE, location.FileName);
        values.put(COLUMN_GEOLOCATION, geoLocation);
        values.put(COLUMN_PRICE, location.Price);
        values.put(COLUMN_DELETABLE, location.Deletable ? 1 : 0);
        values.put(COLUMN_PLANNED_VISIT, "" );
        values.put(COLUMN_DATE_VISITED, "");
        values.put(COLUMN_NOTES, location.Notes);
        values.put(COLUMN_FAVOURITE, location.Favorite ? 1 : 0);

        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Location GetLocation(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME,
                COLUMNS, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)},
                null, null, null, null);

        Location result = null;

        if (cursor != null && cursor.moveToFirst()) {
            result = new Location();
            result.ID = Integer.parseInt(cursor.getString(0));
            result.Name = cursor.getString(1) != null ? cursor.getString(1) : "NO NAME";
            result.Location = cursor.getString(2) != null ? cursor.getString(2) : "NO LOCATION";
            result.Description = cursor.getString(3) != null ? cursor.getString(3) : "NO DESCRIPTION";
            result.FileName = cursor.getString(4) != null ? cursor.getString(4) : "NO IMAGE";
            String geolocation = cursor.getString(5) != null ? cursor.getString(5) : "0";
            result.Price = cursor.getDouble(6);
            result.Deletable = Integer.parseInt(cursor.getString(7)) == 1 ? true : false;
            String PlannedVisit = cursor.getString(8);
            String DateVisited = cursor.getString(9);
            result.Notes = cursor.getString(10);
            result.Favorite = Integer.parseInt(cursor.getString(11)) == 1 ? true : false;

            if (!result.FileName.equals("NO IMAGE")) {
                SaveLoadImages saveLoad = new SaveLoadImages();
                result.Image = saveLoad.LoadImage(result.FileName);
            }

            if (!geolocation.equals("0")) {
                result.GeoLocation = ParseGeoLocation(geolocation);
            }
        }

        return result;
    }

    public int UpdateLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String geoLocation = ParseGeoLocation(location.GeoLocation);

        values.put(COLUMN_NAME, location.Name);
        values.put(COLUMN_LOCATION, location.Location);
        values.put(COLUMN_DESCRIPTION, location.Description);
        values.put(COLUMN_IMAGE, location.FileName);
        values.put(COLUMN_GEOLOCATION, geoLocation);
        values.put(COLUMN_PRICE, location.Price);

        int i = db.update(TABLE_NAME, //table
                values, // column/value
                COLUMN_ID + " = ?", // selections
                new String[]{String.valueOf(location.ID)}); //selection args

        db.close();

        return i;
    }

    public void DeleteLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(location.ID)});

        db.close();
    }

    public List<Location> GetAll() {
        List<Location> locations = new LinkedList<Location>();

        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Location location = null;

        if (cursor != null && cursor.moveToFirst()) {
            do {
                location.Name = cursor.getString(1) != null ? cursor.getString(1) : "NO NAME";
                location.Location = cursor.getString(2) != null ? cursor.getString(2) : "NO LOCATION";
                location.Description = cursor.getString(3) != null ? cursor.getString(3) : "NO DESCRIPTION";
                location.FileName = cursor.getString(4) != null ? cursor.getString(4) : "NO IMAGE";
                location.Price = cursor.getDouble(5);
                int deleteBool = cursor.getInt(7);

                location.Deletable = deleteBool == 1 ? true : false;

                locations.add(location);

            } while (cursor.moveToNext());
        }

        cursor.close();

        return locations;
    }

    private String ParseGeoLocation(double[] geoLocation) {
        if (geoLocation.length == 2) {
            return Double.toString(geoLocation[0]) + "," + Double.toString(geoLocation[1]);
        }

        return "";
    }

    private double[] ParseGeoLocation(String geoLocation) {
        String[] geo = geoLocation.split(",");

        if (geo.length == 2) {
            double[] geoLoc = new double[2];
            geoLoc[0] = Double.parseDouble(geo[0]);
            geoLoc[1] = Double.parseDouble(geo[1]);

            return geoLoc;
        }

        return new double[2];
    }
}
