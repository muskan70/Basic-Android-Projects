package com.manas.funapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.manas.funapp.data.AndroidImageAssests;

public class MainActivity extends AppCompatActivity implements MasterListFragment.OnImageClickListener{
    private int headIndex;
    private int bodyIndex;
    private int legIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onImageSelected(int position) {
        Toast.makeText(this,"Position clicked="+position,Toast.LENGTH_SHORT).show();

        int bodypartnumber=position/12;
        int listindex=position-12*bodypartnumber;

        switch (bodypartnumber){
            case 0:
                headIndex=listindex;
                break;
            case 1:
                bodyIndex=listindex;
                break;
            case 2:
                legIndex=listindex;
                break;
            default:break;
        }

        Bundle b=new Bundle();
        b.putInt("headindex",headIndex);
        b.putInt("bodyindex",bodyIndex);
        b.putInt("legindex",legIndex);

        final Intent intent=new Intent(this,AndroidmeActivity.class);
        intent.putExtras(b);

        Button nextButton=(Button)findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
            }
        });
    }
}
