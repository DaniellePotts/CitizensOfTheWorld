package com.example.danie.comcet325bg46ic.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.preference.Preference;

import com.example.danie.comcet325bg46ic.data.CurrencyCodes;
import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.data.UserPreferences;

/**
 * Created by danie on 10/01/2017.
 */
public class UserPreferencesDatabase extends SQLiteOpenHelper {
    private static final String USER_PREFERENCES_TABLE_NAME = "user_preferences";
    private static final String COLUMN_ID = "_id";
    private static final String USERNAME = "username";
    private static final String FAVECURRENCY = "fave_currency";

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE = "UserPreferencesDatabase";

    public static final String[]COLUMNS={COLUMN_ID,USERNAME,FAVECURRENCY};


    public UserPreferencesDatabase(Context context) {
        super(context, DATABASE, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {

        String CREATE_DATABASE = "" +
                "CREATE TABLE " + USER_PREFERENCES_TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERNAME + " TEXT," +
                FAVECURRENCY + " TEXT)";

        db.execSQL(CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USER_PREFERENCES_TABLE_NAME);
        this.onCreate(db);
    }

    public UserPreferences GetPreferences(){
        UserPreferences preferences = new UserPreferences();
        String query = "SELECT * FROM " + USER_PREFERENCES_TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        UserPreferences preference = new UserPreferences();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                preference.ID = cursor.getInt(0);
                preference.Username = cursor.getString(1) != null ? cursor.getString(1) : "NO NAME";
                preference.FavouriteCurrency = CurrencyCodes.USD.valueOf(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        cursor.close();

        return preference;
    }

    public void UpdatePreference(){

    }

}
