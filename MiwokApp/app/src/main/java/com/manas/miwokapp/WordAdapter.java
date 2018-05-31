package com.manas.miwokapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
     private int colourId;

    public WordAdapter(Activity context, ArrayList<Word> words,int ans){
        super(context,0,words);
        colourId=ans;
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Word w = getItem(position);

        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView engTextView = (TextView) listItemView.findViewById(R.id.english_word);
        engTextView.setText(w.getDefaultTranslation());

        TextView hindiTextView = (TextView) listItemView.findViewById(R.id.hindi_word);
        hindiTextView.setText(w.getHindiTranslation());

        ImageView i=(ImageView) listItemView.findViewById(R.id.image);
        if(w.hasImage()){
           i.setImageResource(w.getImageResourceId());}
        else
            i.setVisibility(View.GONE);

        View v=listItemView.findViewById(R.id.text_container);
        int color= ContextCompat.getColor(getContext(),colourId);
        v.setBackgroundColor(color);
        return listItemView;

    }
}
