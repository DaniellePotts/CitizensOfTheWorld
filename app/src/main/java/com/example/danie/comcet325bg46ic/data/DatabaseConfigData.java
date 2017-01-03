package com.example.danie.comcet325bg46ic.data;

/**
 * Created by bg46ic on 06/12/2016.
 */
public final class DatabaseConfigData {

    public static final String TABLE_NAME = "locations";
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

}
