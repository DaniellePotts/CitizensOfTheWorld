package com.example.danie.comcet325bg46ic.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.danie.comcet325bg46ic.data.Currency;
import com.example.danie.comcet325bg46ic.data.CurrencyCodes;

import java.util.concurrent.TimeUnit;

/**
 * Created by danie on 08/01/2017.
 */
public class GetCurrencyRates {

    public Currency currency;

    public CurrencyCodes code;

    public void Run(CurrencyCodes code){

        if(code != null){
            this.code = code;
        }
        try {
            JSONCurrencyTask task = new JSONCurrencyTask();
            task.execute(new CurrencyCodes[]{code});
            //
             task.get(3000, TimeUnit.MILLISECONDS);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private class JSONCurrencyTask extends AsyncTask<CurrencyCodes, Void, Currency> {

        @Override
        protected Currency doInBackground(CurrencyCodes... params) {
            currency = new Currency();
            String response = ((new CurrencyApiHelper()).MakeRequest(code));
            if (response != null) {
                try {
                    currency = JSONResponseParser.CurrencyConverter(response);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return currency;
            } else return null;
        }

        @Override
        protected void onPostExecute(Currency currency) {
            super.onPostExecute(currency);
            Log.v("Got to onPostExecute",":)");
        }
    }
}
