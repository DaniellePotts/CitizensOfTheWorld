package com.example.danie.comcet325bg46ic.activities;

import android.content.Context;

import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.app.LoaderManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danie.comcet325bg46ic.R;
import com.example.danie.comcet325bg46ic.data.DatabaseConfigData;
import com.example.danie.comcet325bg46ic.data.Location;
import com.example.danie.comcet325bg46ic.data.LocationCursorAdapter;
import com.example.danie.comcet325bg46ic.helpers.ImageGetIntent;
import com.example.danie.comcet325bg46ic.helpers.SQLDatabase;
import com.example.danie.comcet325bg46ic.helpers.SaveLoadImages;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class LocationsList extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, AdapterView.OnItemSelectedListener {

    Uri uri;
    CursorAdapter cursorAdapter;
    ListView lvItems;
    Context c = this;
    Bitmap imageOverwrite; //TODO: for updating image
    Spinner sortList;
    Spinner ascDescSort;
    ImageView image;
    RadioGroup get_image;
    RadioButton takePhoto;
    RadioButton selectPhoto;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.places_to_visit_activity
        );
        cursorAdapter = new LocationCursorAdapter(this, null, 0);
        PopulateListView(null);
        sortList = (Spinner) findViewById(R.id.sortList);
        ascDescSort = (Spinner)findViewById(R.id.ascDsc);
        sortList.setOnItemSelectedListener(this);
        ascDescSort.setOnItemSelectedListener(this);
        final LayoutInflater li = LayoutInflater.from(LocationsList.this);
        final FloatingActionButton addLocation = (FloatingActionButton)findViewById(R.id.add_location);

        addLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder addLocationDialog = new AlertDialog.Builder(LocationsList.this);
                View addLocationView = li.inflate(R.layout.add_location,null);
                addLocationDialog.setView(addLocationView);
                final EditText  locationNameTxt = (EditText) addLocationView.findViewById(R.id.locationName);
                final EditText locationTxt = (EditText) addLocationView.findViewById(R.id.locationText);
                final EditText descriptionTxt = (EditText) addLocationView.findViewById(R.id.descriptionText);;
                final EditText priceTxt = (EditText) addLocationView.findViewById(R.id.priceTxt);;
                image = (ImageView)addLocationView.findViewById(R.id.imgPreview);
                get_image = (RadioGroup) addLocationView.findViewById(R.id.imageChoices);
                takePhoto = (RadioButton) addLocationView.findViewById(R.id.TakePhoto);
                selectPhoto = (RadioButton) addLocationView.findViewById(R.id.UploadImage);
                final Button selectImage = (Button)addLocationView.findViewById(R.id.getImageBtn);

                selectImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = ImageGetIntent.SetImageIntent(takePhoto.isChecked(),selectPhoto.isChecked());
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

                addLocationDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Location locationToAdd = new Location();
                        locationToAdd.Deletable  = true;
                        locationToAdd.Name = locationNameTxt.getText().toString();
                        locationToAdd.Price = Double.parseDouble(priceTxt.getText().toString());
                        locationToAdd.Description = descriptionTxt.getText().toString();
                        locationToAdd.Location = locationTxt.getText().toString();
                        locationToAdd.FileName = SaveImage(imageOverwrite);
                        SQLDatabase db = new SQLDatabase(c);
                        db.addLocation(locationToAdd);
                        Toast.makeText(getApplicationContext(),"Location was added.",Toast.LENGTH_LONG).show();
                    }
                }).create().show();
            }
        });
        lvItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                View getEmpIdView = li.inflate(R.layout.detailed_location, null);
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
                        get_image = (RadioGroup)getEditView.findViewById(R.id.get_image);
                        selectPhoto = (RadioButton)getEditView.findViewById(R.id.rdoUpload);
                        takePhoto = (RadioButton)getEditView.findViewById(R.id.rdoTakePic);
                        btnGetImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                              Intent intent = ImageGetIntent.SetImageIntent(takePhoto.isChecked(),selectPhoto.isChecked());
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

    private void PopulateListView(String orderBy) {
        String query = "SELECT * FROM " + DatabaseConfigData.TABLE_NAME;
        if(orderBy != null){
            query += " " + orderBy;
        }
        SQLDatabase helper = new SQLDatabase(this);
        Cursor data = helper.OrderQuery(query.toUpperCase());
        lvItems = (ListView) findViewById(android.R.id.list);
        LocationCursorAdapter adapter = new LocationCursorAdapter(this, data);
        lvItems.setAdapter(adapter);
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner s = (Spinner)parent;
        if(parent.getId() == R.id.sortList) {
            if (parent.getItemAtPosition(position).toString().equals("Favourite")) {
                PopulateListView("WHERE Favourite == 1");
            } else if (parent.getItemAtPosition(position).toString().equals("All")) {
                PopulateListView(null);
            } else if (parent.getItemAtPosition(position).toString().equals("Planned")) {
                PopulateListView("WHERE planned_visit is not null order by planned_visit");
            } else if (parent.getItemAtPosition(position).toString().equals("Visited")) {
                PopulateListView("WHERE date_visited is not null order by date_visited");
            }
        }
        else if(parent.getId() == R.id.ascDsc){
            if(parent.getItemAtPosition(position).toString().equals("Ascending")){
                PopulateListView("ORDER BY NAME ASC");
            }
            else if(parent.getItemAtPosition(position).toString().equals("Descending")){
                PopulateListView("ORDER BY NAME DESC");
            }
            else if(parent.getItemAtPosition(position).toString().equals("Order By Name...")){
                PopulateListView(null);
            }
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
