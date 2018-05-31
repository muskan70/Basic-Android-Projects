package com.manas.earthquakereportapp;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Info>> {

    private String url;
    public EarthquakeLoader(Context context, String url){
        super(context);
        this.url=url;
    }
    @Override
    public List<Info> loadInBackground() {
        if (url == null) {
            return null;
        }

        List<Info> result = QueryJson.fetchEarthquakeData(url);
        return result;
    }
    protected void onStartLoading() {
        forceLoad();
    }
}
