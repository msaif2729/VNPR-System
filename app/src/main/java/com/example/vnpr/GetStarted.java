package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GetStarted extends AppCompatActivity {

    private ImageView imgchang;
    private TextView txtchng;
    private Button login,regis;

    private int imagearr[] = new int[5];
    private String txtarr[] = new String[5];
    private Context context;

    String cameraPermission[];
    String storagePermission[];

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;




    private int i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);


        // allowing permissions of gallery and camera
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        getPermission();

        imagearr = new int[]{R.drawable.img1,R.drawable.img2,R.drawable.img3,R.drawable.img4,R.drawable.img5};
        txtarr = new String[]{"RTO Info","Check PUC ","Check Challan","Insurance Details","VehiScan - VNPR"};
        imgchang = (ImageView) findViewById(R.id.imgchng);
        txtchng = (TextView) findViewById(R.id.txtchng);
        login = (Button) findViewById(R.id.login);
        regis = (Button) findViewById(R.id.regis);
        context=GetStarted.this;

        //For changing Image
        changeImage();

        //Open Register
        regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Register.class);
                startActivity(intent);
            }
        });

        //Open Login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Login.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    public void changeImage()
    {
        i=0;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                imgchang.setImageResource(imagearr[i]);
                txtchng.setText(txtarr[i]);
                i++;
                if(i>=imagearr.length)
                {
                    i=0;
                }
                imgchang.postDelayed(this,2000);
            }
        };
        imgchang.postDelayed(r,2000);
    }


    public void getPermission() {
        // Check camera permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted, request them
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    CAMERA_PERMISSION_REQUEST_CODE);
        } else {
           //Permission already given
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            // Check if camera permission is granted
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted, proceed with using the camera and external storage
            } else {
                // Permissions denied, inform the user or handle it gracefully
            }
        }
    }
}