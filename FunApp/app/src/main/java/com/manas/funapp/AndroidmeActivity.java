package com.manas.funapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.manas.funapp.data.AndroidImageAssests;

public class AndroidmeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_androidme);

    if(savedInstanceState==null)

    {

        FragmentManager fragmentManager = getSupportFragmentManager();

        BodyPartFragment headFragment = new BodyPartFragment();
        headFragment.setImageIds(AndroidImageAssests.getHeads());
        int headIndex= getIntent().getIntExtra("headindex",0);
        headFragment.setListIndex(headIndex);
        fragmentManager.beginTransaction().add(R.id.head_container, headFragment).commit();


        BodyPartFragment bodyFragment = new BodyPartFragment();
        bodyFragment.setImageIds(AndroidImageAssests.getBodies());
        int bodyIndex= getIntent().getIntExtra("bodyindex",0);
        bodyFragment.setListIndex(bodyIndex);
        fragmentManager.beginTransaction().add(R.id.body_container, bodyFragment).commit();

        BodyPartFragment legFragment = new BodyPartFragment();
        legFragment.setImageIds(AndroidImageAssests.getLegs());
        int legIndex= getIntent().getIntExtra("legindex",0);
        legFragment.setListIndex(legIndex);
        fragmentManager.beginTransaction().add(R.id.leg_container, legFragment).commit();
     }

    }
}
