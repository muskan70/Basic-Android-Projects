
package com.manas.coffeeorderapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

public class MainActivity extends AppCompatActivity {

    int numberOfCoffees=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void increase(View view){
        if(numberOfCoffees==00)
        {
            Toast.makeText(this,"You cannot have more than 100 cups of coffees !",Toast.LENGTH_SHORT).show();
            return;
        }
        numberOfCoffees++;
        display(numberOfCoffees);
    }
    public void decrease(View view){
        if(numberOfCoffees==1){
            Toast.makeText(this,"You cannot have less than 1 cup of coffee !",Toast.LENGTH_SHORT).show();
            return;
        }
        numberOfCoffees--;
        display(numberOfCoffees);
    }
    public void submitOrder(View view){
        EditText text=(EditText)findViewById(R.id.name_field);
        String name=text.getText().toString();
        CheckBox c1=(CheckBox)findViewById(R.id.checkbox1);
        boolean ans=c1.isChecked();
        CheckBox c2=(CheckBox)findViewById(R.id.checkbox2);
        boolean ans2=c2.isChecked();
        int price=calculatePrice(ans,ans2);
        String message=createOrderSummary(price,ans,ans2,name);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, "Coffee order for "+name);
        intent.putExtra(intent.EXTRA_TEXT,message);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    private int calculatePrice(boolean ans,boolean ans2){
        int price=5;
        if(ans)
            price+=1;
        if(ans2)
            price+=2;
        return price*numberOfCoffees;
    }

    protected void display(int n){
        TextView t1=(TextView)findViewById(R.id.quatity_text_view);
        t1.setText(""+n);
    }
    private String createOrderSummary(int price,boolean ans,boolean ans2,String name){
        String message="Name: "+name+"\nAdd Whipped Cream: "+ans+"\nAdd Chocolate: "+ans2+"\nQuantity: "+numberOfCoffees+"\nTotal: $"+price+"\nThankyou!";
        return message;
    }
}

