package com.example.danie.comcet325bg46ic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by danie on 21/12/2016.
 */
public class AddLocation extends AppCompatActivity{

    EditText locationNameTxt;
    EditText locationTxt;
    EditText descriptionTxt;
    Button takePic;
    EditText priceTxt;
    ImageView preview;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_location);

        locationNameTxt = (EditText)findViewById(R.id.locationName);
        locationTxt = (EditText)findViewById(R.id.locationText);
        descriptionTxt = (EditText)findViewById(R.id.descriptionText);
        priceTxt = (EditText)findViewById(R.id.priceTxt);
        preview = (ImageView)findViewById(R.id.imgPreview);
    }

    public void TakePicture(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent,2);
        }
    }

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

    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap)extras.get("data");
                SaveImage(imageBitmap);
            }
        }
    }
}
