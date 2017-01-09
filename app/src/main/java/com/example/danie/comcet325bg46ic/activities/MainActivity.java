package com.example.danie.comcet325bg46ic.activities;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.R;

import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.helpers.PopulateDatabase;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    GestureDetector detector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        detector = new GestureDetector(this, this);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isfirstrun",true);

        if(isFirstRun){
            Populate();
        }

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

    public void Populate() {
        PopulateDatabase pop = new PopulateDatabase();
        List<Location> locations = pop.LoadInDefault10(getResources(), this);
        SQLDatabase db = new SQLDatabase(this);
        for (Location l : locations) {
            if (l.Image != null) {
                l.FileName = SaveImage(l.Image);
                db.addLocation(l);
            }
        }
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
            System.out.print("Image was saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error Saving the Images", Toast.LENGTH_LONG).show();
        }

        return fileName;
    }
}
