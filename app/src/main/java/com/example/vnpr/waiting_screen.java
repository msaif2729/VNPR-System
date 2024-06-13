package com.example.vnpr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class waiting_screen extends AppCompatActivity {

    String plateno,model_name,oname,otpye;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_screen);
        plateno=getIntent().getStringExtra("p");
        model_name = getIntent().getStringExtra("image_model");
        oname = getIntent().getStringExtra("oname");
        otpye = getIntent().getStringExtra("otype");
        Intent intent=new Intent(waiting_screen.this, Vehicle_Desp.class);
        intent.putExtra("vehicle_plate_no", plateno);
        intent.putExtra("model_image",model_name);
        intent.putExtra("oname",oname);
        intent.putExtra("otype",otpye);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        }, 5000);
    }
}