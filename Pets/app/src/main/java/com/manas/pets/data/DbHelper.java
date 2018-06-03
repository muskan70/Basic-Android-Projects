package com.manas.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_ID;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_BREED;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_GENDER;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_NAME;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_WEIGHT;
import static com.manas.pets.data.BlankContract.BlankEntry.TABLE_NAME;

public class DbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Store.db";
    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "+ TABLE_NAME+"("+ COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + "," +
                                                     COLUMN_PET_NAME + " TEXT NOT NULL" + "," + COLUMN_PET_BREED + " TEXT" +","+
                                                     COLUMN_PET_GENDER + " INTEGER NOT NULL"+","+COLUMN_PET_WEIGHT+" INTEGER NOT NULL DEFAULT 0);";
    private static final String SQL_DELETE_ENTRIES="DROP TABLE IF EXISTS "+ TABLE_NAME;

    public DbHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
