package com.example.danie.comcet325bg46ic.data;

import android.content.Context;
import android.database.Cursor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.renderscript.Double2;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.helpers.GetCurrencyRates;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;

public class LocationCursorAdapter extends CursorAdapter {
    public static final String LOCATION_NAME = "name";
    public static final String LOCATION = "location";

    private CurrencyCodes code = null;
    SaveLoadImages saveLoad = new SaveLoadImages();
    public LocationCursorAdapter(Context context, Cursor c, int flags){super(context,c,flags);}
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.places_to_visit_list,parent,false);
    }
    public LocationCursorAdapter(Context context, Cursor c, CurrencyCodes code){
        super(context,c,0);
        this.code = code;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String price = cursor.getString(cursor.getColumnIndex("price"));
        String imageFile = cursor.getString(cursor.getColumnIndex("image"));
        int favourite = cursor.getInt(cursor.getColumnIndex("favourite"));

        Bitmap b = null;
        if(imageFile != null){
            b = saveLoad.LoadImage(imageFile);
        }

        TextView nameTxt = (TextView)view.findViewById(R.id.locationText);
        TextView priceTxt = (TextView)view.findViewById(R.id.priceTxt);
        ImageView img = (ImageView) view.findViewById(R.id.locationImage);
        RatingBar ratingBar = (RatingBar)view.findViewById(R.id.favouriteLocation);

        /*if(code != null && Double.parseDouble(price) != 0.0){
            GetCurrencyRates getRates = new GetCurrencyRates();
            getRates.Run(code);
            if(getRates.currency != null){
                double result = getRates.currency.ConvertCurrency(Double.parseDouble(price), code);
                price = Double.toString(result);
            }
        }*/
        if (favourite == 1){
            ratingBar.setRating(1);
        }
        else{
            img.setImageBitmap(b);
        }
        nameTxt.setText(name);
        priceTxt.setText(GetCurrencySymbol(code) + price);
    }

    private String GetCurrencySymbol(CurrencyCodes code){
        if(code == null){
            return "¥";
        }
        switch (code){
            case USD:
                return "$";
            case GBP:
                return "£";
            case EUR:
                return "€";
            case JPY:
                return "¥";
            default:
                return "¥";
        }
    }
}
