package com.example.vnpr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class HomePage extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private String extract_text;
    SessionMaintain sessionMaintain;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigation = (MeowBottomNavigation) findViewById(R.id.bottomnavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.recent));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.file));
        bottomNavigation.add(new MeowBottomNavigation.Model(4,R.drawable.user));

        Intent intent = getIntent();

        

        bottomNavigation.show(1,true);

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;
                if(model.getId()==4)
                {
//                    Toast.makeText(HomePage.this, "Profile", Toast.LENGTH_SHORT).show();
//                    if(intent.hasExtra("name")&&intent.hasExtra("pass")&&intent.hasExtra("phn_no")&&intent.hasExtra("user"))
//                    {
                        fragment = new Profile();
                        setTitle("Profile");
//                    }
                } else if (model.getId()==3) {
//                    Toast.makeText(HomePage.this, "MyDownload", Toast.LENGTH_SHORT).show();
                    fragment = new Mydownload();
                } else if (model.getId()==2) {
//                    Toast.makeText(HomePage.this, "Recent Searches", Toast.LENGTH_SHORT).show();
                    fragment = new Recent_Search();
                }
                else {
//                    Toast.makeText(HomePage.this, "Home", Toast.LENGTH_SHORT).show();
                    if(intent.hasExtra("Extracted"))
                    {
                        extract_text = intent.getStringExtra("Extracted");
//                        Toast.makeText(HomePage.this, extract_text, Toast.LENGTH_SHORT).show();
//            new Home(extract_text);
                        fragment = new Home(extract_text);

                    }
                    else {
//                        Toast.makeText(HomePage.this, "Not Extracted", Toast.LENGTH_SHORT).show();
                        fragment = new Home();
                    }
                }
                loadFragment(fragment);
                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                Fragment fragment = null;
                if(model.getId()==4)
                {
//                    Toast.makeText(HomePage.this, "Profile", Toast.LENGTH_SHORT).show();
//                    if(intent.hasExtra("name")&&intent.hasExtra("pass")&&intent.hasExtra("phn_no")&&intent.hasExtra("user"))
//                    {
                        fragment = new Profile();
                        setTitle("Profile");
//                    }

                } else if (model.getId()==3) {
//                    Toast.makeText(HomePage.this, "MyDownload", Toast.LENGTH_SHORT).show();
                    fragment = new Mydownload();
                    setTitle("My Downloads");
                } else if (model.getId()==2) {
//                    Toast.makeText(HomePage.this, "Recent Searches", Toast.LENGTH_SHORT).show();
                    fragment = new Recent_Search();
                    setTitle("Recent Searches");
                }
                else {
//                    Toast.makeText(HomePage.this, "Home", Toast.LENGTH_SHORT).show();
                    if(intent.hasExtra("Extracted"))
                    {
                        extract_text = intent.getStringExtra("Extracted");
//                        Toast.makeText(HomePage.this, extract_text, Toast.LENGTH_SHORT).show();
//            new Home(extract_text);
                        fragment = new Home(extract_text);
                        setTitle(R.string.app_name);

                    }
                    else {
//                        Toast.makeText(HomePage.this, "Not Extracted", Toast.LENGTH_SHORT).show();
                        fragment = new Home();
                        setTitle(R.string.app_name);
                    }
                }
                loadFragment(fragment);
                return null;
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
    public void loadFragment(Fragment fragment)
    {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment,null).commit();
//        Animatoo.INSTANCE.animateSlideRight(this);
    }
}