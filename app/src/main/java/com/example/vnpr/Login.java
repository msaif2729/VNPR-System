package com.example.vnpr;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

public class Login extends AppCompatActivity {

    private EditText uname1,pass1;
    private TextInputLayout tiuname1,tipass1;
    private TextView forgot,goto_regis;
    private Button log_in;

    private FirebaseFirestore db;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uname1 = (EditText) findViewById(R.id.uname1);
        pass1 = (EditText) findViewById(R.id.pass1);
        tiuname1 = (TextInputLayout) findViewById(R.id.tiuname1);
        tipass1 = (TextInputLayout) findViewById(R.id.tipass1);
        forgot = (TextView) findViewById(R.id.forgot);
        goto_regis = (TextView) findViewById(R.id.goto_regis);
        log_in = (Button) findViewById(R.id.log_in);
        context = Login.this;

        db = FirebaseFirestore.getInstance();

        pass1.setEnabled(false);
        log_in.setEnabled(false);

        uname1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    tiuname1.setError("Enter Username");
                    pass1.setEnabled(false);
                }
                else
                {
                    tiuname1.setError(null);
                    pass1.setEnabled(true);
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
        pass1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String p = s.toString();
                if(s.toString().equals(""))
                {
                    tipass1.setError("Enter Password");
                    log_in.setEnabled(false);
                }
                else if (s.length()>15 || s.length()<8)
                {
                    tipass1.setError("Password length must be more than 8 and less than 15 characters");
                    log_in.setEnabled(false);
                }
                else if (!p.matches(numbers))
                {
                    tipass1.setError("Password should contain atleast one number");
                    log_in.setEnabled(false);
                }
                else if (!p.matches(specialChars))
                {
                    tipass1.setError("Password should contain atleast one special character");
                    log_in.setEnabled(false);
                }
                else
                {
                    tipass1.setError(null);
                    log_in.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Goto Register Activity
        goto_regis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,Register.class);
                startActivity(intent);
                finish();
            }
        });

        //Goto ForgotPassword Activity
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ForgotPassword.class);
                startActivity(intent);
            }
        });
        
        
        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("users").whereEqualTo("uname",uname1.getText().toString()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null || value.size()==0 ) {
                            uname1.setError("User not found");
                        }
                        int flag = 0;
                        for(DocumentSnapshot doc: value)
                        {
                            String p = doc.getString("pass");
                            String u =doc.getString("uname");
                            String n = doc.getString("name");
                            String ph = doc.getString("phn_no");
                            uname1.setError(null);
                            pass1.setError(null);
                            if(pass1.getText().toString().equals(p)&&uname1.getText().toString().equals(u))
                            {
//
//                                intent1.putExtra("name",n);
//                                intent1.putExtra("user",u);
//                                intent1.putExtra("phn_no",ph);
//                                intent1.putExtra("pass",p);
//                                startActivity(intent1);
//                                Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
//                                finish();
                                SessionMaintain sessionMaintain = new SessionMaintain(getApplicationContext());
                                sessionMaintain.createSession(n,u,p,ph);
                                Intent intent1 = new Intent(Login.this,HomePage.class);
                                startActivity(intent1);
//                                Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else
                            {
                                pass1.setError("Invalid password");
                            }
                        }

                    }
                });
            }
        });

    }
}