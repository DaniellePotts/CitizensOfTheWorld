package com.example.danie.comcet325bg46ic;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.danie.comcet325bg46ic.helpers.ImageGetIntent;

/**
 * Created by danie on 12/01/2017.
 */
public class AddLocation extends AppCompatActivity {

    EditText name;
    EditText location;
    EditText description;

    ImageView previewImage;
    TextView details;
    FloatingActionButton notesFab;
    FloatingActionButton mapsFab;
    FloatingActionButton pictureFab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_edit_location);

        name = (EditText)findViewById(R.id.name);
        location = (EditText)findViewById(R.id.location);
        description = (EditText)findViewById(R.id.description);

        previewImage = (ImageView)findViewById(R.id.previewImg);
        details = (TextView)findViewById(R.id.details);

        notesFab = (FloatingActionButton)findViewById(R.id.notesBtn);
        mapsFab = (FloatingActionButton)findViewById(R.id.mapsBtn);
        pictureFab = (FloatingActionButton)findViewById(R.id.photoBtn);

        SetImage();
    }

    public void SetImage(){
        pictureFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder setImage = new AlertDialog.Builder(getApplicationContext());
                setImage.setPositiveButton("Get from Gallery", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = ImageGetIntent.SetImageIntent(false,true);
                        startActivityForResult(intent,3);
                    }
                }).setPositiveButton("Take Picture", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = ImageGetIntent.SetImageIntent(true,false);
                        startActivityForResult(intent,2);
                    }
                }).create().show();
            }
        });
    }
}
