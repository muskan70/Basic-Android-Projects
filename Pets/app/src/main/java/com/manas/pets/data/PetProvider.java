package com.manas.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static android.provider.BaseColumns._ID;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_ID;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_GENDER;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_NAME;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_WEIGHT;
import static com.manas.pets.data.BlankContract.BlankEntry.CONTENT_ITEM_TYPE;
import static com.manas.pets.data.BlankContract.BlankEntry.CONTENT_LIST_TYPE;
import static com.manas.pets.data.BlankContract.BlankEntry.TABLE_NAME;
import static com.manas.pets.data.BlankContract.BlankEntry.isValidGender;
import static com.manas.pets.data.BlankContract.CONTENT_AUTHORITY;
import static com.manas.pets.data.BlankContract.PATH_PETS;

public class PetProvider extends ContentProvider {

    private DbHelper helper;
    private static final int PETS=100;
    private static final int PET_ID=101;

    private static final UriMatcher sUriMatcher=new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(CONTENT_AUTHORITY,PATH_PETS,PETS);
        sUriMatcher.addURI(CONTENT_AUTHORITY,PATH_PETS+"/#",PET_ID);
    }
    @Override
    public boolean onCreate() {
        helper=new DbHelper(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase db=helper.getReadableDatabase();
        Cursor cursor;

        int match=sUriMatcher.match(uri);
        switch(match){
            case PETS :
                cursor=db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            case PET_ID:
                selection = _ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor=db.query(TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown uri");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return CONTENT_LIST_TYPE;
            case PET_ID:
                return CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match=sUriMatcher.match(uri);
        switch (match){
            case PETS:
                return insertPet(uri,values);
            default:
                throw new IllegalArgumentException("Error with uri given");
        }
    }
    private Uri insertPet(Uri uri ,ContentValues values){

        String name=values.getAsString(COLUMN_PET_NAME);
        if(name==null){
            throw new IllegalArgumentException("Pet requires a name");
        }

        Integer gender=values.getAsInteger(COLUMN_PET_GENDER);
        if(gender == null ||!isValidGender(gender)){
            throw new IllegalArgumentException("Pet requires a valid gender");
        }

        Integer weight=values.getAsInteger(COLUMN_PET_WEIGHT);
        if(weight != null && weight<0){
            throw new IllegalArgumentException("Pet requires a valid weight");
        }

        SQLiteDatabase db=helper.getWritableDatabase();
        long id=db.insert(TABLE_NAME,null,values);
        if(id==-1){
            Log.e("","Failed to insert pet");
            return null;
        }

        getContext().getContentResolver().notifyChange(uri,null);
        return ContentUris.withAppendedId(uri,id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = helper.getWritableDatabase();
        int rowsDeleted;

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                rowsDeleted= database.delete(TABLE_NAME, selection, selectionArgs);
                if(rowsDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsDeleted;
            case PET_ID:
                selection = COLUMN_ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted= database.delete(TABLE_NAME, selection, selectionArgs);
                if(rowsDeleted!=0){
                    getContext().getContentResolver().notifyChange(uri,null);
                }
                return rowsDeleted;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int match=sUriMatcher.match(uri);
        switch(match){
            case PETS:
                return updatePet(uri,values,selection,selectionArgs);
            case PET_ID:
                selection=COLUMN_ID+"=?";
                selectionArgs=new String[]{String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri,values,selection,selectionArgs);
            default:
                throw new IllegalArgumentException("Error with given uri");
        }
    }
    private int updatePet( Uri uri, ContentValues values, String selection,String[] selectionArgs) {
        if (values.containsKey(COLUMN_PET_NAME)) {
            String name = values.getAsString(COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        if (values.containsKey(COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(COLUMN_PET_GENDER);
            if (gender == null || !isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        if (values.containsKey(COLUMN_PET_WEIGHT)) {
            Integer weight = values.getAsInteger(COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }

        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase db=helper.getWritableDatabase();
        int i=db.update(TABLE_NAME,values,selection,selectionArgs);
        if(i!=0){
        getContext().getContentResolver().notifyChange(uri,null);}
        return i;
    }
}
