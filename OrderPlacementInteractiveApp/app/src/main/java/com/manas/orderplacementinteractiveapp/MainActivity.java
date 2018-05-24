package com.manas.orderplacementinteractiveapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    int numberOfCoffees=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void increase(View view){
        numberOfCoffees++;
        display(numberOfCoffees);
        displayprice(numberOfCoffees*5);
    }
    public void decrease(View view){
        numberOfCoffees--;
        display(numberOfCoffees);
        displayprice(numberOfCoffees*5);
    }
    public void submitOrder(View view){
        String message="total:$"+(numberOfCoffees*5);
        message+="\nThankyou";
        displaymessage(message);
    }
    protected void displaymessage(String message){
        TextView t1=(TextView)findViewById(R.id.price_text_view);
        t1.setText(""+message);
    }
    protected void display(int n){
        TextView t1=(TextView)findViewById(R.id.quatity_text_view);
        t1.setText(""+n);
    }
    protected void displayprice(int n){
        TextView t2=(TextView)findViewById(R.id.price_text_view);
        t2.setText(NumberFormat.getCurrencyInstance().format(n));
    }
}
