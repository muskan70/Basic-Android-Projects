package com.manas.basketballcourtcounterapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    static int scoreA=0;
    static int scoreB=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void addscoreA3(View v){
        display(3,1);
    }
    public void addscoreA2(View v){
        display(2,1);
    }
    public void addscoreA1(View v){
        display(1,1);
    }
    public void addscoreB3(View v){
        display(3,2);
    }
    public void addscoreb2(View v){
        display(2,2);
    }
    public void addscoreB1(View v){
        display(1,2);
    }
    public void reset(View v){
        TextView t1,t2;
            scoreA = 0;
            t1 = findViewById(R.id.teamAscore);
            t1.setText(String.valueOf(scoreA));
            scoreB = 0;
            t2 = findViewById(R.id.teamBscore);
            t2.setText(String.valueOf(scoreB));
    }
    protected void display(int n,int t){
        TextView t1;
        if(t==1) {
            scoreA += n;
            t1 = findViewById(R.id.teamAscore);
            t1.setText(String.valueOf(scoreA));
        }
        else {
            scoreB += n;
            t1 = findViewById(R.id.teamBscore);
            t1.setText(String.valueOf(scoreB));
        }


    }
}
