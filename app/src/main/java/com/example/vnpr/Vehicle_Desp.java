package com.example.vnpr;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.*;

public class Vehicle_Desp extends AppCompatActivity {
    AppBarLayout appbarlayout;
    ImageButton logo_view;
    ImageView vehicle_img;
    CardView cardview,insurance_alert,puc_alert;
    Intent intentrec;

    Switch my_vehicle_switch;
    androidx.appcompat.widget.Toolbar toolbar;
    CollapsingToolbarLayout collapsing_toolbar;
    DocumentReference doc_ref;
    String regi_no,model_name1,oname,oship;
    private Map<String,String> recentsearch,mydownloads;
    String _ownername,_ownership,_model,_regisdate,_regisno,_insdate,_pucdate,_insu_cmp;
    private SessionMaintain sessionMaintain;
    private FirebaseFirestore db;
    Menu item ;
    TextView name_text,owner_type_text,model_name_text,plate_no, owner_name,owner_ship,regis_date,regis_rto,model_name,vehicle_class,fuel_type,fuel_norms,chassis_no,engine_no,regis_date2,fit_upto_date,puc_date,insurance_date1,vehicle_age,regis_no,color,unloaded_weight,rc_status,cmp_name,insurance_date2,policy_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_desp);
        intentrec=getIntent();
        regi_no=intentrec.getStringExtra("vehicle_plate_no");
        model_name1 = intentrec.getStringExtra("model_image");
        oname = intentrec.getStringExtra("oname");
        oship = intentrec.getStringExtra("otype");
        vehicle_img = (ImageView) findViewById(R.id.vehicle_img);
        my_vehicle_switch = (Switch) findViewById(R.id.my_vehicle_switch);
        sessionMaintain = new SessionMaintain(getApplicationContext());
        my_vehicle_switch.setEnabled(true);
        db = FirebaseFirestore.getInstance();
        recentsearch = new HashMap<>();
        mydownloads = new HashMap<>();

        if(!regi_no.equals(null))
        {
            regi_no = regi_no.toUpperCase().trim();
            recentsearch.put("regisno",regi_no);
            recentsearch.put("modelimage",model_name1);
            recentsearch.put("ownname",oname);
            recentsearch.put("ownship",oship);
            mydownloads.put("regisno",regi_no);
            mydownloads.put("modelimage",model_name1);
            mydownloads.put("ownname",oname);
            mydownloads.put("ownship",oship);
            display_info(regi_no);
            Glide.with(Vehicle_Desp.this).load(model_name1).into(vehicle_img);
        }

        my_vehicle_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b)
                {
//                    Toast.makeText(Vehicle_Desp.this, "Checked", Toast.LENGTH_SHORT).show();
                    if(sessionMaintain.getSession("key_vehi_no")==null)
                    {
                        sessionMaintain.My_vehile(regi_no,model_name1,oname,oship);
                        my_vehicle_switch.setEnabled(false);
                    } else if (sessionMaintain.getSession("key_vehi_no").equals(regi_no)) {
                        my_vehicle_switch.setChecked(true);
                        my_vehicle_switch.setEnabled(false);
                    } else{
                        Toast.makeText(Vehicle_Desp.this, "You Can only add one vehile at a time", Toast.LENGTH_SHORT).show();
                        my_vehicle_switch.setChecked(false);
                        my_vehicle_switch.setEnabled(true);
                    }
                }

            }
        });

        String vehi = sessionMaintain.getSession("key_vehi_no");
        if(vehi!=null)
        {
            if(!vehi.equals(regi_no))
            {
                my_vehicle_switch.setEnabled(true);
                my_vehicle_switch.setChecked(false);
            }
            else{
//                Toast.makeText(this, vehi+" Equal "+regi_no, Toast.LENGTH_SHORT).show();
                my_vehicle_switch.setChecked(true);
                my_vehicle_switch.setEnabled(false);

            }
        }

        display_card();
        toolbar=findViewById(R.id.toolbar);
        collapsing_toolbar = findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setVisibility(View.VISIBLE);
        if(getSupportActionBar()!=null)
        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        new MenuInflater(this).inflate(R.menu.result_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int item_id=item.getItemId();
//        if (item_id == R.id.share_opt) {
//            Toast.makeText(getApplicationContext(), "SHARE",Toast.LENGTH_LONG).show();
//        }
         if(item_id==R.id.downloads_opt)
        {
            String username = sessionMaintain.getSession("key_uname");
            db.collection("users").document(username).collection("mydownloads").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                    if(error!=null )
                    {
                        Toast.makeText(Vehicle_Desp.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                    db.collection("users").document(username).collection("mydownloads").document(regi_no).set(mydownloads).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
//                                Toast.makeText(Vehicle_Desp.this, "sucess", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(Vehicle_Desp.this, "Unsucess", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
            Toast.makeText(getApplicationContext(), "Added To My Downloads",Toast.LENGTH_LONG).show();
        }
        else if(item_id==R.id.remove_my_vehicle)
        {
                Toast.makeText(this, "Remove From My Vehilce", Toast.LENGTH_SHORT).show();
                sessionMaintain.remove_from_myvehicle();
                my_vehicle_switch.setChecked(false);
                my_vehicle_switch.setEnabled(true);
        }
        else
        {
            //for back button
            super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    public void display_card()
    {
        cardview=(CardView) findViewById(R.id.top_info);
        appbarlayout=(AppBarLayout) findViewById(R.id.Appbar);
        // TextView toolbar_title=findViewById(R.id.toolbar_plateno_text);
        appbarlayout.addOnOffsetChangedListener(new AppBarStateChangedListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                Log.d("Mohammed Saif"+getClass().getCanonicalName(), state.name());
                if(state.name().equals("COLLAPSED"))
                {
                    cardview.setVisibility(View.INVISIBLE);
                    collapsing_toolbar.setTitle(_regisno);
                }
                if(state.name().equals("EXPANDED")) {
                    cardview.setVisibility(View.VISIBLE);
                    collapsing_toolbar.setTitle(" ");
                }
                if(state.name().equals("IDLE")) {
                    cardview.setVisibility(View.VISIBLE);
                    collapsing_toolbar.setTitle(" ");
                }


            }
        });
    }
    public void display_info(String regi_no)
    {
        alerts();
        //Toast.makeText(getApplicationContext(), regi_no, Toast.LENGTH_LONG).show();
        doc_ref=FirebaseFirestore.getInstance().collection("vnpr").document(regi_no);
        doc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // for Upper Card details
                name_text=(TextView)findViewById(R.id.name_text);
                owner_type_text=(TextView)findViewById(R.id.owner_type_text);
                model_name_text=(TextView)findViewById(R.id.model_name_text);
                plate_no=(TextView)findViewById(R.id.plate_no);
                //for owner_details card
                owner_name=(TextView) findViewById(R.id.owner_name);
                owner_ship=(TextView) findViewById(R.id.owner_ship);
                regis_date=(TextView) findViewById(R.id.regis_date);
                regis_rto=(TextView) findViewById(R.id.regis_rto);
                //for vehicle_details card
                model_name=(TextView) findViewById(R.id.model_name);
                vehicle_class=(TextView) findViewById(R.id.vehicle_class);
                fuel_type=(TextView) findViewById(R.id.fuel_type);
                fuel_norms=(TextView) findViewById(R.id.fuel_norms);
                chassis_no=(TextView) findViewById(R.id.chassis_no);
                engine_no=(TextView) findViewById(R.id.engine_no);
                //for Important Dates card
                regis_date2=(TextView) findViewById(R.id.regis_date2);
                fit_upto_date=(TextView) findViewById(R.id.fit_upto_date);
                puc_date=(TextView) findViewById(R.id.puc_date);
                insurance_date1=(TextView) findViewById(R.id.insurance_date1);
                vehicle_age=(TextView) findViewById(R.id.vehicle_age);
                //for other_information
                regis_no=(TextView) findViewById(R.id.regis_no);
                color=(TextView) findViewById(R.id.color);
                unloaded_weight=(TextView) findViewById(R.id.unloaded_weight);
                rc_status=(TextView) findViewById(R.id.rc_status);
                //for insurance_details
                cmp_name=(TextView) findViewById(R.id.cmp_name);
                insurance_date2=(TextView)findViewById(R.id.insurance_date2);
                policy_no=(TextView)findViewById(R.id.policy_no);
                if(documentSnapshot.exists())
                {
                    _ownername=documentSnapshot.getString("own_name");
                    _ownership=documentSnapshot.getString("ownership");
                    _model=documentSnapshot.getString("model");
                    _regisdate=documentSnapshot.getString("regis_date");
                    _regisno=documentSnapshot.getString("regis_no");
                    _insdate=documentSnapshot.getString("insurance_ex_date");
                    _pucdate=documentSnapshot.getString("puc");
                    _insu_cmp=documentSnapshot.getString("insurance_name");
                    //upper card details
                    name_text.setText(_ownername);
                    owner_type_text.setText(_ownership);
                    model_name_text.setText(_model);
                    plate_no.setText(_regisno);
                    //owner details card
                    owner_name.setText(_ownername);
                    owner_ship.setText(_ownership);
                    regis_date.setText(_regisdate);
                    regis_rto.setText(documentSnapshot.getString("regis_rto"));
                    //vehicle_details
                    model_name.setText(_model);
                    vehicle_class.setText(documentSnapshot.getString("vehicle_class"));
                    fuel_type.setText(documentSnapshot.getString("fuel_type"));
                    fuel_norms.setText(documentSnapshot.getString("fuel_norms"));
                    chassis_no.setText(documentSnapshot.getString("chassis_no"));
                    engine_no.setText(documentSnapshot.getString("engine_no"));
                    //important dates
                    regis_date2.setText(_regisdate);
                    fit_upto_date.setText(documentSnapshot.getString("fit_upto"));
                    puc_date.setText(_pucdate);
                    insurance_date1.setText(_insdate);
                    vehicle_age.setText(documentSnapshot.getString("vehicle_age"));
                    //other information
                    regis_no.setText(_regisno);
                    color.setText(documentSnapshot.getString("color"));
                    unloaded_weight.setText(documentSnapshot.getString("unload_weight"));
                    rc_status.setText(documentSnapshot.getString("rc_status"));
                    //insurance details
                    cmp_name.setText(_insu_cmp);
                    insurance_date2.setText(_insdate);
                    policy_no.setText(documentSnapshot.getString("policy_no"));
                    display_logo();
                }
            }
        });

        String username = sessionMaintain.getSession("key_uname");
        db.collection("users").document(username).collection("recent").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error!=null )
                {
                    Toast.makeText(Vehicle_Desp.this, "Error", Toast.LENGTH_SHORT).show();
                }

//                    Toast.makeText(Vehicle_Desp.this, "Loop", Toast.LENGTH_SHORT).show();
                    db.collection("users").document(username).collection("recent").document(regi_no).set(recentsearch);
//                    Toast.makeText(Vehicle_Desp.this, "Added Recently", Toast.LENGTH_SHORT).show();


            }
        });


    }

    public void regis(String oname,String otype)
    {
        _ownername = oname;
        _ownership = otype;
        Toast.makeText(Vehicle_Desp.this, _ownername+_ownership, Toast.LENGTH_SHORT).show();


    }

    public void alerts()
    {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String strDate= formatter.format(date);
        insurance_alert=(CardView) findViewById(R.id.ins_alert_card);
        puc_alert=(CardView)findViewById(R.id.puc_alert_card);
        if(strDate.equals(_pucdate))
            puc_alert.setVisibility(View.VISIBLE);
        else
            puc_alert.setVisibility(View.GONE);
        if(strDate.equals(_insdate))
            insurance_alert.setVisibility(View.VISIBLE);
        else
            insurance_alert.setVisibility(View.GONE);

    }
    public void display_logo()
    {
        logo_view=(ImageButton) findViewById(R.id.logo);
        String s=_insu_cmp.toLowerCase().trim();
        switch (s) {
            case "go digit general insurance ltd.":
                logo_view.setImageResource(R.drawable.godigit);
                break;
            case "icici lombard":
            case "icici lombard general insurance company limited":
                logo_view.setImageResource(R.drawable.icic);
                break;
            case "tata aig general insurance co. ltd.":
            case "tata aig general insurance co.ltd":
                logo_view.setImageResource(R.drawable.tata);
                break;
            case "shriram general insurance co. ltd.":
                logo_view.setImageResource(R.drawable.shriram);
                break;
            case "united india insurance co. ltd.":
                logo_view.setImageResource(R.drawable.unitedindia);
                break;
            case "cholamandalam ms general insurance co. ltd":
                logo_view.setImageResource(R.drawable.chola);
                break;
            case "hdfc ergo general insurance company ltd.":
                logo_view.setImageResource(R.drawable.hdfc);
                break;
            case "liberty general insurance limited":
                logo_view.setImageResource(R.drawable.liberty);
                break;
            case "acko general insurance limited":
                logo_view.setImageResource(R.drawable.acko);
                break;
            case " sbi general insurance company limited":
                logo_view.setImageResource(R.drawable.sbi);
                break;
            case "reliance general insurance co.ltd.":
                logo_view.setImageResource(R.drawable.reliance);
                break;
            default:
                logo_view.setVisibility(View.GONE);
                break;
        }

    }
}