package com.manas.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.manas.pets.R;
import com.manas.pets.data.DbHelper;

import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_ID;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_BREED;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_GENDER;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_NAME;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_WEIGHT;
import static com.manas.pets.data.BlankContract.BlankEntry.CONTENT_URI;
import static com.manas.pets.data.BlankContract.BlankEntry.GENDER_MALE;
import static com.manas.pets.data.BlankContract.BlankEntry.TABLE_NAME;

public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int PET_LOADER=0;
    PetCursorAdapter mCursorAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalog_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });
        ListView petListView=(ListView)findViewById(R.id.list);

        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        mCursorAdaptor=new PetCursorAdapter(this,null);
        petListView.setAdapter(mCursorAdaptor);

        petListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri intentPetUri= ContentUris.withAppendedId(CONTENT_URI,id);
                intent.setData(intentPetUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PET_LOADER,null,this);


    }

    private void insertpet(){
        ContentValues values = new ContentValues();
        values.put(COLUMN_PET_NAME,"Tom");
        values.put(COLUMN_PET_BREED,"Tabby");
        values.put(COLUMN_PET_GENDER,GENDER_MALE);
        values.put(COLUMN_PET_WEIGHT,7);

       Uri uri=getContentResolver().insert(CONTENT_URI,values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertpet();
                return true;
            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete(CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from pet database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection=new String[]{COLUMN_ID,COLUMN_PET_NAME,COLUMN_PET_BREED};
        return new CursorLoader(this,CONTENT_URI,projection,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
         mCursorAdaptor.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
         mCursorAdaptor.swapCursor(null);
    }
    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do u want delete all pets ?");
        builder.setPositiveButton("Delete All", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deleteAllPets();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
