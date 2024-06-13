package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    private EditText uname,newpass;
    private TextInputLayout tiuname;
    private TextInputLayout tioldpass,tinewpass;
    private Button log_in,ok;
    private FirebaseFirestore db;
    private boolean flag = false;
    private Map<String,String> updateuser;
    private String strname,struname,strpass,strphno,str;
    private Intent intent;
    Dialog dialog;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        uname = (EditText) findViewById(R.id.cpuname);
        newpass = (EditText) findViewById(R.id.newpass);
        log_in = (Button) findViewById(R.id.log_in);
        tiuname = (TextInputLayout) findViewById(R.id.cptiuname);
        tinewpass = (TextInputLayout) findViewById(R.id.tinewpass);
        intent = getIntent();
        if(intent.hasExtra("ForgotPassword"))
        {
            str = intent.getStringExtra("ForgotPassword");
        }
        else {
            str = intent.getStringExtra("Profile");
        }

        db = FirebaseFirestore.getInstance();

        dialog = new Dialog(ChangePassword.this);
        dialog.setContentView(R.layout.custom_dialog);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialog_shape));
        dialog.setCancelable(false);

        ok = (Button) dialog.findViewById(R.id.ok);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Toast.makeText(ChangePassword.this, "Password Changed Successfully", Toast.LENGTH_SHORT).show();
                if(str.equals("forgotPassword"))
                {
                    Intent intent = new Intent(ChangePassword.this, Login.class);
                    startActivity(intent);
                    finish();
                }
                else{
                Intent intent = new Intent(ChangePassword.this, HomePage.class);
                startActivity(intent);
                SessionMaintain sessionMaintain = new SessionMaintain(getApplicationContext());
                sessionMaintain.updateSession(newpass.getText().toString());
                finish();
                }
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
                    newpass.setEnabled(false);
                }
                else
                {
                    db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            for(DocumentSnapshot doc: value)
                            {
                                if(error!=null) {
                                    Toast.makeText(ChangePassword.this, "Error", Toast.LENGTH_SHORT).show();
                                }
                                struname = doc.getString("uname");
                                if(s.toString().equals(struname))
                                {
                                    struname = doc.getString("uname");
                                    strname = doc.getString("name");
                                    strphno = doc.getString("phn_no");
                                    Log.d("Mohammed Saif",struname);
                                    tiuname.setError(null);
                                    tiuname.setErrorEnabled(false);
                                    newpass.setEnabled(true);
                                    break;
                                }
                                else {
                                    tiuname.setError("User Not Found");
                                    newpass.setEnabled(false);
                                    tiuname.setErrorEnabled(true);
                                    Log.d("Mohammed Saif1",struname);
                                }
                            }
                        }
                    });
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

        newpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String p = s.toString();
                if(s.toString().equals(""))
                {
                    tinewpass.setError("Enter Password");
                    log_in.setEnabled(false);
                }
                else if (s.length()>15 || s.length()<8)
                {
                    tinewpass.setError("Password length must be more than 8 and less than 15 characters");
                    log_in.setEnabled(false);
                }
                else if (!p.matches(numbers))
                {
                    tinewpass.setError("Password should contain atleast one number");
                    log_in.setEnabled(false);
                }
                else if (!p.matches(specialChars))
                {
                    tinewpass.setError("Password should contain atleast one special character");
                    log_in.setEnabled(false);
                }
                else
                {
                    tinewpass.setError(null);
                    log_in.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {



            }
        });


        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(ChangePassword.this, "Name : "+struname+newpass.getText().toString(), Toast.LENGTH_SHORT).show();
                    db.collection("users").document(struname).update("name",strname,"uname",struname,"pass",newpass.getText().toString(),"phn_no",strphno)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(ChangePassword.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                        dialog.show();
                                    }
                                    else{
                                        Toast.makeText(ChangePassword.this, "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ChangePassword.this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
            }
        });

    }
}