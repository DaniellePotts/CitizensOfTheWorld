package com.example.danie.comcet325bg46ic.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.Currency;
import com.example.danie.comcet325bg46ic.data.CurrencyCodes;
import com.example.danie.comcet325bg46ic.helpers.CurrencyApiHelper;
import com.example.danie.comcet325bg46ic.helpers.JSONResponseParser;

import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    GestureDetector detector;
    ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONCurrencyTask task = new JSONCurrencyTask();
        task.execute(new CurrencyCodes[]{CurrencyCodes.USD});
     /*   cover = (ImageView)findViewById(R.id.coverPhoto);
        detector = new GestureDetector(this,this);*/
    }

    public void OpenBudgetPlanner(View v) {
        Intent budgetPlanIntent = new Intent(this, BudgetPlanner.class);
        startActivity(budgetPlanIntent);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return detector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Intent intent = new Intent(getApplicationContext(), LocationsList.class);
        startActivity(intent);
        overridePendingTransition(R.xml.slide_up, R.xml.slide_down);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    public void Populate(){
        SQLDatabase db = new SQLDatabase(this);

        List<Location>locations = db.GetAll();

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

        MtFuji.FileName = SaveImage(BitmapFactory.decodeResource(getResources(),R.drawable.mt_fuji));

        Location imperialPalace = new Location();
        imperialPalace.Name = "Imperial Palace";
        imperialPalace.Location = "Tokyo, Japan";
        imperialPalace.Description = "Primary residence of the Emperor of Japan";
        imperialPalace.Favorite = true;
        imperialPalace.GeoLocation[0] = 35.685175;
        imperialPalace.GeoLocation[1] = 139.7506108;
        imperialPalace.Notes = "here are some notes";

        imperialPalace.FileName = SaveImage(BitmapFactory.decodeResource(getResources(),R.drawable.imperial_palace));

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

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    private class JSONCurrencyTask extends AsyncTask<CurrencyCodes, Void, Currency> {

        @Override
        protected Currency doInBackground(CurrencyCodes... params) {
            Currency curr = new Currency();
            String response = ((new CurrencyApiHelper()).MakeRequest(CurrencyCodes.USD));
            if (response != null) {
                try {
                    curr = JSONResponseParser.CurrencyConverter(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return curr;
            } else return null;
        }

        @Override
        protected void onPostExecute(Currency currency) {
            super.onPostExecute(currency);
            Log.v("Got to onPostExecute",":)");
        }
    }

    public String SaveImage(Bitmap b) {
        OutputStream output;
        File filePath = getFilesDir();
        File dir = new File(filePath.getAbsolutePath() + "/CitizensoftheWorld");

        try {
            dir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SaveLoadImages saveLoad = new SaveLoadImages();

        String fileName = saveLoad.setFileName();
        File file = new File(dir, fileName);

        try {
            output = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

        return fileName;
    }
}
