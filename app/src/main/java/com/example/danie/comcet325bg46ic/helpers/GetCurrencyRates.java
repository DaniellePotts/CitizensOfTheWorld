package com.example.danie.comcet325bg46ic.helpers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.danie.comcet325bg46ic.data.Currency;
import com.example.danie.comcet325bg46ic.data.CurrencyCodes;

/**
 * Created by danie on 08/01/2017.
 */
public class GetCurrencyRates {

    public Currency currency;

    public void Run(CurrencyCodes code){

        if(code == null){
            code = CurrencyCodes.GBP;
        }
        JSONCurrencyTask task = new JSONCurrencyTask();
        task.execute(new CurrencyCodes[]{code});
    }

    private class JSONCurrencyTask extends AsyncTask<CurrencyCodes, Void, Currency> {

        @Override
        protected Currency doInBackground(CurrencyCodes... params) {
            currency = new Currency();
            String response = ((new CurrencyApiHelper()).MakeRequest(CurrencyCodes.USD));
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
