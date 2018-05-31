package com.manas.earthquakereportapp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Info {
    private String location,url;
    private double mag;
    private long time;

    public Info(double mag,String location,long time,String url)
    {
        this.location=location;
        this.mag=mag;
        this.time=time;
        this.url=url;
    }

    public String getLocation() {
        return location;
    }
    public String getUrl() {
        return url;
    }

    public double getMag() {
        return mag;
    }

    public String getDate() {
        Date dateObject = new Date(time);
        String dateToDisplay = formatDate(dateObject);
        return dateToDisplay;
    }
    public String getTime() {
        Date dateObject = new Date(time);
        String TimeToDisplay = formatTime(dateObject);
        return TimeToDisplay;
    }
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
