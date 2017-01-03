package com.example.danie.comcet325bg46ic.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.Location;

import java.util.List;

/**
 * Created by danie on 03/01/2017.
 */
public class PopulateDatabase {


    public void LoadInDefault10(Resources resources, Context c){
        SQLDatabase db = new SQLDatabase(c);

        List<Location> locations = db.GetAll();

        for(Location l: locations){
            db.DeleteLocation(l);
        }

        //re-populate

        Location MtFuji = new Location();
        MtFuji.Favorite = true;
        MtFuji.Name = "Mount Fuji";
        MtFuji.Location = "Kitayama, Japan";
        MtFuji.Description = "Active volcano";
        MtFuji.GeoLocation[0] = 35.3605555;
        MtFuji.GeoLocation[1] = 138.725589;
        MtFuji.Notes = "Here are some notes";
        MtFuji.Image = BitmapFactory.decodeResource(resources,R.drawable.mt_fuji);

        Location imperialPalace = new Location();
        imperialPalace.Name = "Imperial Palace";
        imperialPalace.Location = "Tokyo, Japan";
        imperialPalace.Description = "Primary residence of the Emperor of Japan";
        imperialPalace.Favorite = true;
        imperialPalace.GeoLocation[0] = 35.685175;
        imperialPalace.GeoLocation[1] = 139.7506108;
        imperialPalace.Notes = "here are some notes";

        Location museum = new Location();
        museum.Name = "National Museum of Nature and Science";
        museum.Location = "Taito, Tokyo, Japan";
        museum.Description = "Opened in 1871";
        museum.GeoLocation[0] = 35.716357;
        museum.GeoLocation[1] = 139.7741939;
        museum.Notes = "here are some notes";

        db.addLocation(MtFuji);
        db.addLocation(museum);
        db.addLocation(imperialPalace);
    }
}
