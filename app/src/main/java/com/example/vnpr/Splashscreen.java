package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public class Splashscreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionMaintain sessionMaintain = new SessionMaintain(getApplicationContext());
                boolean b = sessionMaintain.checkSession();
                if(b==true  )
                {
                    Intent intent1 = new Intent(getApplicationContext(),HomePage.class);
                    startActivity(intent1);
                    Toast.makeText(Splashscreen.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), GetStarted.class);
                    startActivity(intent);

                    finish();
                }
            }
        }, 2000);
    }

    @Override
    public void onResume(){
        super.onResume();
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SessionMaintain sessionMaintain = new SessionMaintain(getApplicationContext());
                boolean b = sessionMaintain.checkSession();
                if(b==true  )
                {
                    Intent intent1 = new Intent(getApplicationContext(),HomePage.class);
                    startActivity(intent1);
                    Toast.makeText(Splashscreen.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), GetStarted.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);
    }
}