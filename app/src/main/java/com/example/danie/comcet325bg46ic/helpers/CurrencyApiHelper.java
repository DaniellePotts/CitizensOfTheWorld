package com.example.danie.comcet325bg46ic.helpers;

import android.util.Log;

import com.example.danie.comcet325bg46ic.data.CurrencyCodes;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by danie on 02/01/2017.
 */
public class CurrencyApiHelper {
    private static String BASE_URL = "http://api.fixer.io/";

    public String GetUSCurrency(){
        return "latest?base=USD";
    }

    public String MakeRequest(CurrencyCodes code){
        String request = "latest";//+code.toString();
        HttpURLConnection con = null;
        InputStream inputStream = null;
        String requestString = "";
        try{
            requestString = BASE_URL + URLEncoder.encode(request,"UTF-8");
            Log.v("Log: ","Request made");
        }catch(Exception e){
            e.printStackTrace();
        }

        try{
            con = (HttpURLConnection)(new URL(requestString)).openConnection();
            con.setRequestMethod("GET");
            //HttpURLConnection.setFollowRedirects(false);
            con.connect();

            int response = con.getResponseCode();
            if(response == HttpURLConnection.HTTP_OK){
                StringBuilder builder = new StringBuilder();
                inputStream = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = br.readLine()) != null){
                    builder.append(line + "\r\n");
                }
                inputStream.close();
                con.disconnect();
                String result = builder.toString();
                return result;

            }else{
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return null;
    }
}
