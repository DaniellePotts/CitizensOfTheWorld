package com.example.danie.comcet325bg46ic.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.danie.comcet325bg46ic.AddLocation;
import com.example.danie.comcet325bg46ic.R;

public class MainActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    GestureDetector detector;
    ImageView cover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cover = (ImageView)findViewById(R.id.coverPhoto);
        detector = new GestureDetector(this,this);
    }

    public void OpenActivity(View v){
        Intent intent = new Intent(this,AddLocation.class);
        startActivity(intent);
    }

    public void OpenBudgetPlanner(View v){
        Intent budgetPlanIntent = new Intent(this,BudgetPlanner.class);
        startActivity(budgetPlanIntent);
    }

    public boolean onTouchEvent(MotionEvent event){
        return detector.onTouchEvent(event);
    }
    @Override
    public boolean onDown(MotionEvent e) {
        Intent intent = new Intent(getApplicationContext(),LocationsList.class);
        startActivity(intent);
        overridePendingTransition(R.xml.slide_up,R.xml.slide_down);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

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
}
