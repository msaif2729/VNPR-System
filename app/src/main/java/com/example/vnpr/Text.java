package com.example.vnpr;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Text extends AppCompatActivity {

    TextView txt;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);

        txt = (TextView) findViewById(R.id.txt);
        Intent intent = getIntent();
        String str = intent.getStringExtra("Converted");
        int i=0;
        int len = str.length();
//        Toast.makeText(this, str.charAt(1), Toast.LENGTH_SHORT).show();
        String s = "";
        for(i=0;i<len;i++)
        {
            if("A".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "B".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "C".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "D".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "E".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "F".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "G".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "H".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "I".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "J".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "K".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "L".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "M".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "N".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "O".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "P".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "Q".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "R".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "S".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "T".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "U".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "V".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "W".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "X".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "Y".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "Z".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "0".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "1".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "2".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "3".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "4".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "5".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "6".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "7".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "8".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "9".equalsIgnoreCase(String.valueOf(str.charAt(i))))
            {
                s += String.valueOf(str.charAt(i));
            }
        }
        txt.setText(s.toUpperCase());
    }
}