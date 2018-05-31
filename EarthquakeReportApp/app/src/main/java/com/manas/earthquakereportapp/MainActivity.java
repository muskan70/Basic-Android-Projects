package com.manas.earthquakereportapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Info>> {

    private static final int EARTHQUAKE_LOADER_ID=1;

    private static final String REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    InfoAdapter adapter;
    private TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView earthquakeListView = (ListView) findViewById(R.id.list);
        adapter = new InfoAdapter(this, new ArrayList<Info>());
        earthquakeListView.setAdapter(adapter);

         empty_view=(TextView)findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(empty_view);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Info currentEarthquake = adapter.getItem(position);
                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);
                startActivity(websiteIntent);
            }
        });
        LoaderManager l=getLoaderManager();
        l.initLoader(EARTHQUAKE_LOADER_ID,null,this);


    }

    @Override
    public Loader<List<Info>> onCreateLoader(int id, Bundle args) {
        return new EarthquakeLoader(this,REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Info>> loader, List<Info> data) {
        View pb=findViewById(R.id.progress);
        pb.setVisibility(View.GONE);
         adapter.clear();
         empty_view.setText("No earthquakes found");
         if(data!=null || !data.isEmpty())
             adapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Info>> loader) {
          adapter.clear();
    }

}
