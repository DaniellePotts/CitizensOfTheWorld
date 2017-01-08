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

    public double ConvertCurrency(double base, CurrencyCodes code){
        double reuslt = 0.0;
        switch (code){
            case EUR:
                reuslt = Convert(base, EUR_Value);
            case USD:
                reuslt = Convert(base, USD_Value);
            case JPY:
                reuslt= Convert(base, JPY_Value);
            case GBP:
                reuslt= Convert(base, GBP_Value);
        }
        return reuslt;
    }
}
