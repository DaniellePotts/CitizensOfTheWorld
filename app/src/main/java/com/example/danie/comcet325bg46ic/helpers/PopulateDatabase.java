package com.example.danie.comcet325bg46ic.helpers;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;

import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.Location;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by danie on 03/01/2017.
 */
public class PopulateDatabase {

    public List<Location> LoadInDefault10(Resources resources, Context c) {

        DeleteCurrentData(c);

        Location MtFuji = new Location();
        MtFuji.Favorite = true;
        MtFuji.Name = "Mount Fuji";
        MtFuji.Location = "Kitayama, Japan";
        MtFuji.Description = "Active volcano";
        MtFuji.GeoLocation[0] = 35.3605555;
        MtFuji.GeoLocation[1] = 138.725589;
        MtFuji.Notes = "Here are some notes";
        MtFuji.Price = 15953.91;
        MtFuji.PlannedVisit = new Date();
        MtFuji.Image = BitmapFactory.decodeResource(resources, R.drawable.mt_fuji);
        MtFuji.Rank = 4;

        Location imperialPalace = new Location();
        imperialPalace.Name = "Imperial Palace";
        imperialPalace.Location = "Tokyo, Japan";
        imperialPalace.Description = "Primary residence of the Emperor of Japan";
        imperialPalace.Favorite = true;
        imperialPalace.GeoLocation[0] = 35.685175;
        imperialPalace.GeoLocation[1] = 139.7506108;
        imperialPalace.Notes = "here are some notes";
        imperialPalace.Image = BitmapFactory.decodeResource(resources, R.drawable.imperial_palace);
        imperialPalace.Rank = 4;

        Location museum = new Location();
        museum.Name = "National Museum of Nature and Science";
        museum.Location = "Taito, Tokyo, Japan";
        museum.Description = "Opened in 1871";
        museum.GeoLocation[0] = 35.716357;
        museum.GeoLocation[1] = 139.7741939;
        museum.Price = 620;
        museum.Notes = "here are some notes";
        museum.Image = BitmapFactory.decodeResource(resources, R.drawable.science_museum);
        museum.Rank = 5;

        Location disneyLand = new Location();
        disneyLand.Name = "Disneyland";
        disneyLand.Location = "Tokyo, Japan";
        disneyLand.Description = "Tokyo offshoot of the iconic theme park known for its rides, live shows & costumed characters";
        disneyLand.GeoLocation[0] = 35.6328964;
        disneyLand.GeoLocation[1] = 139.8782056;
        disneyLand.Price = 7400;
        disneyLand.Image = BitmapFactory.decodeResource(resources, R.drawable.disney_land);
        disneyLand.Rank = 5;

        Location skytree = new Location();
        skytree.Name = "Skytree";
        skytree.Location = "Tokyo, Japan";
        skytree.Description = "World's tallest freestanding broadcasting tower with an observation deck boasting 360-degree views";
        skytree.GeoLocation[0] = 35.7100627;
        skytree.GeoLocation[1] = 139.8085117;
        skytree.Price = 3000;
        skytree.Image = BitmapFactory.decodeResource(resources, R.drawable.skytree);
        skytree.Rank = 3;

        Location tokyoTower = new Location();
        tokyoTower.Name = "Tokyo Tower";
        tokyoTower.Location = "Tokyo, Japan";
        tokyoTower.Description = "Reminiscent of the Eiffel Tower, this landmark features observation areas & other attractions";
        tokyoTower.GeoLocation[0] = 35.6585805;
        tokyoTower.GeoLocation[1] = 139.7432442;
        tokyoTower.Price = 900;
        tokyoTower.Image = BitmapFactory.decodeResource(resources, R.drawable.tokyo_tower);
        tokyoTower.Rank = 4;

        Location pokeCenter = new Location();
        pokeCenter.Name = "Pokemon Center Mega Tokyo";
        pokeCenter.Location = "Tokyo, Japan";
        pokeCenter.Description = "Largest store dedicated exclusively to Pokemon in the world";
        pokeCenter.GeoLocation[0] = 35.7287927;
        pokeCenter.GeoLocation[1] = 139.7170338;
        pokeCenter.Price = 0.0;
        pokeCenter.Image = BitmapFactory.decodeResource(resources, R.drawable.poke_center);
        pokeCenter.Rank = 5;

        Location onePieceTower = new Location();
        onePieceTower.Name = "Tokyo One Piece Tower";
        onePieceTower.Location = "Tokyo Tower, Japan";
        onePieceTower.Description = "Discover the amazing world of One Piece at Tokyo's newest and most exciting amusement park located in the iconic Tokyo Tower";
        onePieceTower.GeoLocation[0] = 35.658597;
        onePieceTower.GeoLocation[1] = 139.743181;
        onePieceTower.Price = 3000;
        onePieceTower.Image = BitmapFactory.decodeResource(resources, R.drawable.onepiecetower);
        onePieceTower.Rank = 4;

        Location tokyoNationalMuseum = new Location();
        tokyoNationalMuseum.Name = "Tokyo National Museum";
        tokyoNationalMuseum.Location = "Taito, Tokyo, Japan";
        tokyoNationalMuseum.Description = "Stately museum complex devoted to the art & antiquities of Japan, as well as other Asian countries";
        tokyoNationalMuseum.GeoLocation[0] = 35.7188351;
        tokyoNationalMuseum.GeoLocation[1] = 139.7743328;
        tokyoNationalMuseum.Price = 620;
        tokyoNationalMuseum.Image = BitmapFactory.decodeResource(resources, R.drawable.national_museum);
        tokyoNationalMuseum.Rank = 4;

        Location koishikawaKorakuen = new Location();
        koishikawaKorakuen.Name = "Koishikawa Korakuen Garden";
        koishikawaKorakuen.Location = "Bunkyo, Tokyo, Japan";
        koishikawaKorakuen.Description = "One of two surviving Edo period clan gardens in modern Tokyo";
        koishikawaKorakuen.Price = 0.0;
        koishikawaKorakuen.GeoLocation[0] = 35.7057504;
        koishikawaKorakuen.GeoLocation[1] = 139.7482184;
        koishikawaKorakuen.Image = BitmapFactory.decodeResource(resources, R.drawable.korakuen);
        koishikawaKorakuen.Rank = 5;

        List<Location> locationList = new ArrayList<>();
        locationList.add(MtFuji);
        locationList.add(imperialPalace);
        locationList.add(museum);
        locationList.add(disneyLand);
        locationList.add(skytree);
        locationList.add(tokyoTower);
        locationList.add(pokeCenter);
        locationList.add(onePieceTower);
        locationList.add(tokyoNationalMuseum);
        locationList.add(koishikawaKorakuen);

        return locationList;
    }

    public void DeleteCurrentData(Context c) {
        SQLDatabase db = new SQLDatabase(c);
        List<Location> locations = db.GetAll();
        if (!locations.equals(null)) {
            for (Location location : locations) {
                db.DeleteLocation(location);
            }
        }
    }
}

