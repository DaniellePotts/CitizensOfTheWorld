package com.example.danie.comcet325bg46ic.activities;

import android.content.Context;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.app.LoaderManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.data.LocationCursorAdapter;
import com.example.danie.comcet325bg46ic.helpers.ImageGetIntent;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;


/**
 * Created by danie on 18/12/2016.
 */
public class LocationsList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    Uri uri;
    CursorAdapter cursorAdapter;
    ListView lvItems;
    Context c = this;
    Bitmap imageOverwrite; //TODO: for updating image
    Spinner sortList;
    ImageView image;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_to_visit_activity
        );
        cursorAdapter = new LocationCursorAdapter(this, null, 0);
        PopulateListView();
        sortList = (Spinner) findViewById(R.id.sortList);

        final FloatingActionButton addLocation = (FloatingActionButton)findViewById(R.id.add_location);

        sortList.setOnItemSelectedListener(this);
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Object val = parent.getAdapter().getItem(position);
                final LayoutInflater li = LayoutInflater.from(LocationsList.this);
                View getEmpIdView = li.inflate(R.layout.detailed_location, null);
                Object obj = parent.getItemAtPosition(position);
                final SQLDatabase db = new SQLDatabase(c);
                final Location l = db.GetLocation((int) parent.getItemIdAtPosition(position));

                final AlertDialog.Builder alertDiaglogBuilder = new AlertDialog.Builder(LocationsList.this);
                alertDiaglogBuilder.setView(getEmpIdView);

                final TextView name = (TextView) getEmpIdView.findViewById(R.id.nameTxt);
                final TextView location = (TextView) getEmpIdView.findViewById(R.id.locationTxt);
                final TextView description = (TextView) getEmpIdView.findViewById(R.id.descriptionTxt);
                final TextView price = (TextView) getEmpIdView.findViewById(R.id.priceTxt);
                final FloatingActionButton edit_fab = (FloatingActionButton) getEmpIdView.findViewById(R.id.edit_button);
                final FloatingActionButton notes_fab = (FloatingActionButton) getEmpIdView.findViewById(R.id.notes);

                if (l.Deletable) {
                    FloatingActionButton delete_fab = (FloatingActionButton) getEmpIdView.findViewById(R.id.delete_button);
                    delete_fab.setVisibility(View.VISIBLE);
                    delete_fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AlertDialog.Builder alertDelete = new AlertDialog.Builder(LocationsList.this);
                            alertDelete.setMessage("Delete " + l.Name + "?");
                            alertDelete.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    db.DeleteLocation(l);
                                    finish();
                                }
                            }).create().show(); //TODO: set negative/cancel value :)
                        }
                    });
                }

                name.setText(l.Name);
                location.setText(l.Location);
                description.setText(l.Description);
                price.setText(Double.toString(l.Price));
                if (l.Image != null) {
                    final ImageView locationImage = (ImageView) getEmpIdView.findViewById(R.id.locationImage);
                    locationImage.setImageBitmap(l.Image);
                }

                edit_fab.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        View getEditView = li.inflate(R.layout.edit_location, null);
                        final AlertDialog.Builder editLocationDialog = new AlertDialog.Builder(LocationsList.this);

                        editLocationDialog.setView(getEditView);

                        final EditText name = (EditText) getEditView.findViewById(R.id.nameTxt);
                        final EditText location = (EditText) getEditView.findViewById(R.id.locationTxt);
                        final EditText description = (EditText) getEditView.findViewById(R.id.descriptionTxt);
                        final EditText price = (EditText) getEditView.findViewById(R.id.priceTxt);
                        image = (ImageView) getEditView.findViewById(R.id.locationImage);
                        final Button btnGetImage = (Button) getEditView.findViewById(R.id.btnGetImage);

                        btnGetImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              Intent intent = ImageGetIntent.SetImageIntent(true,false);
                                if (intent != null) {
                                    if (intent.resolveActivity(getPackageManager()) != null) {
                                        startActivityForResult(intent, ImageGetIntent.ActivityCode);
                                    }
                                }
                                if (getParent() == null) {
                                    setResult(RESULT_OK, intent);

                                } else {
                                    getParent().setResult(RESULT_OK, intent);
                                }
                            }
                        });

                        name.setText(l.Name);

                        location.setText(l.Location);
                        location.setText(l.Description);
                        price.setText(Double.toString(l.Price));
                        if (l.Image != null) {
                            image.setImageBitmap(l.Image);
                        }
                        editLocationDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                l.Name = name.getText().toString();
                                l.Location = location.getText().toString();
                                l.Description = description.getText().toString();
                                l.Price = Double.parseDouble(price.getText().toString());

                                if(imageOverwrite != null){
                                    l.FileName = SaveImage(imageOverwrite);
                                }
                                db.UpdateLocation(l);
                                Toast.makeText(c, "Location Updated.", Toast.LENGTH_LONG).show();
                            }
                        }).create().show();
                    }
                });

                notes_fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        View notesView = li.inflate(R.layout.notes, null);
                        AlertDialog.Builder notesDialog = new AlertDialog.Builder(LocationsList.this);
                        notesDialog.setView(notesView);
                        final EditText notesText = (EditText) notesView.findViewById(R.id.noteTxt);

                        notesText.setText(l.Notes);
                        notesDialog.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                l.Notes = notesText.getText().toString();
                                db.UpdateLocation(l);
                            }
                        }).create().show();
                    }
                });
                alertDiaglogBuilder.create().show();

                return true;
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    private void PopulateListView() {
        SQLiteDatabase db;
        SQLDatabase helper = new SQLDatabase(this);
        db = helper.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM locations", null);
        lvItems = (ListView) findViewById(android.R.id.list);
        LocationCursorAdapter adapter = new LocationCursorAdapter(this, data);
        lvItems.setAdapter(adapter);
    }

    private void PopulateListView(String orderBy) {
        SQLiteDatabase db;
        SQLDatabase helper = new SQLDatabase(this);
        db = helper.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM locations WHERE " + orderBy.toUpperCase(), null);
        lvItems = (ListView) findViewById(android.R.id.list);
        LocationCursorAdapter adapter = new LocationCursorAdapter(this, data);
        lvItems.setAdapter(adapter);
    }


    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position).toString().equals("Favourite")) {
            PopulateListView("Favourite == 1");
        } else if (parent.getItemAtPosition(position).toString().equals("All")) {
            PopulateListView();
        } else if (parent.getItemAtPosition(position).toString().equals("Planned")) {
            PopulateListView("planned_visit is not null order by planned_visit");
        } else if (parent.getItemAtPosition(position).toString().equals("Visited")) {
            PopulateListView("date_visited is not null order by date_visited");
        }
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    public String SaveImage(Bitmap b) {
        OutputStream output;
        File filePath = getFilesDir();
        File dir = new File(filePath.getAbsolutePath() + "/CitizensoftheWorld");

        try {
            dir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }

        SaveLoadImages saveLoadImages = new SaveLoadImages();
        String fileName = saveLoadImages.setFileName();
        File file = new File(dir, fileName);

        try {
            output = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.JPEG, 50, output);
            output.flush();
            output.close();
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
        }

        return fileName;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageOverwrite = (Bitmap) extras.get("data");
                image.setImageBitmap(imageOverwrite);
            }
        } else if (requestCode == 3) {
            if (resultCode == RESULT_OK) {
                Uri selectedImage = data.getData();

                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();

                imageOverwrite = BitmapFactory.decodeFile(filePath);
                image.setImageBitmap(imageOverwrite);
            }
        }
    }
}
