package com.manas.pets;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_BREED;
import static com.manas.pets.data.BlankContract.BlankEntry.COLUMN_PET_NAME;

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor cursor){
        super(context,cursor,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView name=(TextView)view.findViewById(R.id.name);
        TextView summary=(TextView)view.findViewById(R.id.summary);

        int nameindex=cursor.getColumnIndex(COLUMN_PET_NAME);
        int breedindex=cursor.getColumnIndex(COLUMN_PET_BREED);

        String petname=cursor.getString(nameindex);
        String petbreed=cursor.getString(breedindex);
        if (TextUtils.isEmpty(petbreed)) {
            petbreed ="Unknown Breed";
        }

        name.setText(petname);
        summary.setText(petbreed);
    }
}
