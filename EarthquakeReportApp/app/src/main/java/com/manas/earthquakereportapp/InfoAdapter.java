package com.manas.earthquakereportapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class InfoAdapter extends ArrayAdapter<Info> {
    private static final String LOCATION_SEPARATOR="of ";

    public InfoAdapter(Activity context, ArrayList<Info> earthquakes){
        super(context,0,earthquakes);
    }
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Info w = getItem(position);

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView mag = (TextView) listItemView.findViewById(R.id.magnitude);
        String formattedMagnitude=formatMagnitude(w.getMag());
        mag.setText(formattedMagnitude);

        int magnitudeColour=getMagnitudeColor(w.getMag());
        mag.setBackgroundColor(magnitudeColour);


        String originalLocation=w.getLocation();
        String primaryLocation,locationOffset;

        if(originalLocation.contains(LOCATION_SEPARATOR))
        {
            String[] parts=originalLocation.split(LOCATION_SEPARATOR);
            primaryLocation=parts[0]+LOCATION_SEPARATOR;
            locationOffset=parts[1];
        }
        else {
            primaryLocation="Near the";
            locationOffset=originalLocation;
        }

        TextView location1 = (TextView) listItemView.findViewById(R.id.primary_location);
        location1.setText(primaryLocation);
        TextView location2 = (TextView) listItemView.findViewById(R.id.location_offset);
        location2.setText(locationOffset);

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(w.getDate());
        TextView time = (TextView) listItemView.findViewById(R.id.time);
        time.setText(w.getTime());

        return listItemView;
    }
    private String formatMagnitude(double magnitude){
        DecimalFormat ans=new DecimalFormat("0.0");
        return ans.format(magnitude);
    }
    private int getMagnitudeColor(double m){
        int magnitudeColorResourceId=0;
        switch((int) Math.floor(m)){
            case 0:
            case 1:
                magnitudeColorResourceId=R.color.magnitude1;
                break;
            case 2:
                magnitudeColorResourceId = R.color.magnitude2;
                break;
            case 3:
                magnitudeColorResourceId = R.color.magnitude3;
                break;
            case 4:
                magnitudeColorResourceId = R.color.magnitude4;
                break;
            case 5:
                magnitudeColorResourceId = R.color.magnitude5;
                break;
            case 6:
                magnitudeColorResourceId = R.color.magnitude6;
                break;
            case 7:
                magnitudeColorResourceId = R.color.magnitude7;
                break;
            case 8:
                magnitudeColorResourceId = R.color.magnitude8;
                break;
            case 9:
                magnitudeColorResourceId = R.color.magnitude9;
                break;
            default:
                magnitudeColorResourceId = R.color.magnitude10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), magnitudeColorResourceId);

    }
}
