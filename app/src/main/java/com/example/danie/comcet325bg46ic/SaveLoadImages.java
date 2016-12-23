package com.example.danie.comcet325bg46ic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by danie on 21/12/2016.
 */
public class SaveLoadImages extends AppCompatActivity{

    public String SaveImage(Bitmap b){
        OutputStream output;
        File filePath = getFilesDir();
        File dir = new File(filePath.getAbsolutePath() + "/CitizensoftheWorld");

        try{
            dir.mkdirs();
        }catch (Exception e){
            e.printStackTrace();
        }

        File file = new File(dir,"image1.jpg");

        try{
            output = new FileOutputStream(file);

            //compress image
            b.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

        return file.getName();
    }
    public Bitmap LoadImage(String filePath){
        try{
            File f = new File(AppConfigs.INTERNAL_FILE_ROUTE, filePath);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getDefaultIcon(){
        Bitmap b = BitmapFactory.decodeResource(getResources(),R.drawable.pikachu);

        return b;
    }

}
