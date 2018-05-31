package com.manas.miwokapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager v=(ViewPager)findViewById(R.id.viewpager);
        SimpleFragmentPagerAdapter fp=new SimpleFragmentPagerAdapter(getSupportFragmentManager(),this);
        v.setAdapter(fp);

        TabLayout tb=(TabLayout)findViewById(R.id.tab);
        tb.setupWithViewPager(v);
    }
}

