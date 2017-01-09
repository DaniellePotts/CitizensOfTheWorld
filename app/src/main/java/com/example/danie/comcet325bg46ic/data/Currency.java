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

    public double ConvertCurrency(CurrencyCodes code){
        double result = 0.0;
        switch (code){
            case EUR:
                result = EUR_Value;
            case USD:
                result = USD_Value;
            case JPY:
                result = JPY_Value;
            case GBP:
                result=  GBP_Value;
        }
        return result;
    }
}
