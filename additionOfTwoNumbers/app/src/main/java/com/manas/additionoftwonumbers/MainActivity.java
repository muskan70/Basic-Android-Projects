package com.manas.additionoftwonumbers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button b;
    TextView t;
    EditText t1,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b=(Button)findViewById(R.id.button);
        t1=(EditText) findViewById(R.id.t1);
        t2=(EditText) findViewById(R.id.t2);
        t=(TextView) findViewById(R.id.t3);

    }
        public void sum(View v) {
            int a=Integer.valueOf(t1.getText().toString());
            int b=Integer.valueOf(t2.getText().toString());
            int c=a+b;
            String s=t.getText().toString()+String.valueOf(c);
            t.setText(s);
        }

}
