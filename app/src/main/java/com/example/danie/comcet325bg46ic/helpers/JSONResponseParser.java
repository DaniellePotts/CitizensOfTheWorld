package com.example.danie.comcet325bg46ic.helpers;

import com.example.danie.comcet325bg46ic.data.Currency;

import org.json.JSONObject;

/**
 * Created by danie on 03/01/2017.
 */
public class JSONResponseParser {
    public static Currency CurrencyConverter(String data) {
        try {
            Currency conversionRates = new Currency();
            JSONObject jObj = new JSONObject(data);

            String base = jObj.getString("base");
            JSONObject rates = jObj.getJSONObject("rates");
            if(!base.equals("EUR")){
                conversionRates.EUR_Value = rates.getDouble("EUR");
            }
            if(!base.equals("JPY")){
                conversionRates.JPY_Value = rates.getDouble("JPY");
            }
            if(!base.equals("GBP")){
                conversionRates.GBP_Value = rates.getDouble("GBP");
            }
            if(!base.equals("USD")){
                conversionRates.JPY_Value = rates.getDouble("USD");
            }

            return conversionRates;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
