package com.example.danie.comcet325bg46ic.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ViewFlipper;
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

    EditText searchForLocation;

    ViewFlipper flipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        flipper = (ViewFlipper)findViewById(R.id.view_flipper);
        flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_in));
        flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.fade_out));
        detector = new GestureDetector(this, this);
        Boolean isFirstRun = getSharedPreferences("PREFERENCE",MODE_PRIVATE).getBoolean("isfirstrun",true);

        searchForLocation = (EditText)findViewById(R.id.search);
        searchForLocation.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        if(!searchForLocation.equals("")){
                            Location result = SearchForLocation(searchForLocation.getText().toString());

                            if(result == null){
                                final AlertDialog.Builder aD = new AlertDialog.Builder(MainActivity.this);

                                aD.setMessage(searchForLocation.getText().toString() + " could not be found. Would you like to add it into the list of locations?");

                                aD.setPositiveButton("Add Location", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LayoutInflater li = LayoutInflater.from(MainActivity.this);
                                        View addView = li.inflate(R.layout.add_location,null);
                                        AlertDialog.Builder addDialog = new AlertDialog.Builder(getApplicationContext());
                                        addDialog.setView(addView);
                                        addDialog.create().show();
                                    }
                                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                }).create().show();
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Found x location!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"No data in text box.",Toast.LENGTH_LONG).show();
                        }
                    return true;
                }

                return false;
            }
        });
        /*if(isFirstRun){
            Populate();
        }*/
    }

    public Location SearchForLocation(String loc){
        SQLDatabase db = new SQLDatabase(this);
        Location foundLoc = null;
        List<Location> locations = db.GetAll();
        for (Location l : locations){
            if(l.Name.equalsIgnoreCase(loc) || l.Location.equalsIgnoreCase(loc)){
                foundLoc = l;
            }
        }
        return foundLoc;
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
