package com.example.danie.comcet325bg46ic.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.danie.comcet325bg46ic.AddLocation;
import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.Currency;
import com.example.danie.comcet325bg46ic.data.CurrencyCodes;
import com.example.danie.comcet325bg46ic.helpers.CurrencyApiHelper;
import com.example.danie.comcet325bg46ic.helpers.JSONResponseParser;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.lang.ref.ReferenceQueue;
import java.net.HttpURLConnection;
import java.net.URL;

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

    public void OpenActivity(View v) {
        Intent intent = new Intent(this, AddLocation.class);
        startActivity(intent);
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
}
