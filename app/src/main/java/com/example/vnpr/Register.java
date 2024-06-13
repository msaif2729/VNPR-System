package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Register extends AppCompatActivity {

    private EditText name,uname,pass,phn_no;
    private TextView goto_login;
    private Button register;
    private TextInputLayout tiname,tiuname,tipass,tiphn;
    private Context context;
    private FirebaseFirestore db;
    private ProgressBar progress;
    Map<String,String> user;
    private String formated_phone_no;

    int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = (EditText) findViewById(R.id.name);
        uname = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.pass);
        phn_no = (EditText) findViewById(R.id.phn_no);
        goto_login = (TextView) findViewById(R.id.goto_login);
        register = (Button) findViewById(R.id.register);
        tiname = (TextInputLayout) findViewById(R.id.tiname);
        tiuname = (TextInputLayout) findViewById(R.id.tiuname);
        tipass = (TextInputLayout) findViewById(R.id.tipass);
        tiphn = (TextInputLayout) findViewById(R.id.tiphn);
        progress = (ProgressBar) findViewById(R.id.progress);

        db = FirebaseFirestore.getInstance();

        //Disable other components
        register.setEnabled(false);
        pass.setEnabled(false);
        phn_no.setEnabled(false);
        uname.setEnabled(false);
        context = Register.this;


        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    tiname.setError("Enter Name");
                    uname.setEnabled(false);
                }
                else
                {
                    tiname.setError(null);
                    uname.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        uname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    tiuname.setError("Enter Username");
                    pass.setEnabled(false);
                }
                else
                {
                    tiuname.setError(null);
                    pass.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //for password validation
        String uppercasec = "(.*[A-Z].*)";
        String lowerCaseChars = "(.*[a-z].*)";
        String numbers = "(.*[0-9].*)";
        String specialChars = "(.*[,~,!,@,#,$,%,^,&,*,(,),-,_,=,+,[,{,],},|,;,:,<,>,/,?].*$)";

        pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String p = s.toString();
                if(s.toString().equals(""))
                {
                    tipass.setError("Enter Password");
                    phn_no.setEnabled(false);
                }
                else if (s.length()>15 || s.length()<8)
                {
                    tipass.setError("Password length must be more than 8 and less than 15 characters");
                    phn_no.setEnabled(false);
                }
                else if (!p.matches(numbers))
                {
                    tipass.setError("Password should contain atleast one number");
                    phn_no.setEnabled(false);
                }
                else if (!p.matches(specialChars))
                {
                    tipass.setError("Password should contain atleast one special character");
                    phn_no.setEnabled(false);
                }
                else
                {
                    tipass.setError(null);
                    phn_no.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        phn_no.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    tiphn.setError("Enter Phone No");
                    register.setEnabled(false);
                } else if (s.toString().length()>10 || s.toString().length()<10) {
                    tiphn.setError("Enter Valid Mobile Number");
                    register.setEnabled(false);
                } else
                {
                    tiphn.setError(null);
                    register.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Goto Login Activity
        goto_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Login.class);
                startActivity(intent);
                finish();
            }
        });



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                progress.setVisibility(View.VISIBLE);
                register.setVisibility(View.GONE);
                db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        for(DocumentSnapshot doc: value)
                        {
                            if(error!=null) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                            String str = doc.getString("uname");
                            if(uname.getText().toString().equals(str))
                            {
                                tiuname.setError("Username already exists");
                                flag = 1;
                                register.setVisibility(View.VISIBLE);
                                progress.setVisibility(View.GONE);
                                break;
                            }
                            else {
                                tiuname.setErrorEnabled(false);
                                tiuname.setMinimumHeight(80);
                                tiuname.setError(null);
                                flag = 0;
                            }
                        }
                        if(flag==0)
                        {
                            callfunction();
                        }
                    }
                });




            }
        });

    }

    public void callfunction()
    {
        formated_phone_no = "+91 "+phn_no.getText().toString().substring(0,4)+" "+phn_no.getText().toString().substring(4,7)+" "+phn_no.getText().toString().substring(7,10);
        Toast.makeText(Register.this, formated_phone_no, Toast.LENGTH_SHORT).show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                formated_phone_no,
                60,
                TimeUnit.SECONDS,
                Register.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                        register.setVisibility(View.VISIBLE);
                        Log.d("Mohammed Saif",e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                //Open Verification Activity
//                                String verificationid = "saif";
                        tiuname.setErrorEnabled(false);
                        tiuname.setError(null);
                        progress.setVisibility(View.GONE);
                        register.setVisibility(View.VISIBLE);
                        Log.d("Mohammed Saif",formated_phone_no);
                        Verification verification = new Verification(name.getText().toString(),uname.getText().toString(),pass.getText().toString(),phn_no.getText().toString(),"register",verificationid);
                        verification.show(getSupportFragmentManager(),"Verification");
                    }
                }
        );
    }
}