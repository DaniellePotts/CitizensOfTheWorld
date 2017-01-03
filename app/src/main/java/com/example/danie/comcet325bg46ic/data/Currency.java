package com.example.danie.comcet325bg46ic.data;

/**
 * Created by danie on 03/01/2017.
 */
public class Currency {

    public CurrencyCodes Base_Value;
    public double USD_Value;
    public double GBP_Value;
    public double JPY_Value;
    public double EUR_Value;

    public double Convert(double base, double exchange){
        return base * exchange;
    }
}
