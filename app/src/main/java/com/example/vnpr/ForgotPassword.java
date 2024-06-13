package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ForgotPassword extends AppCompatActivity {

    private EditText forgot_phno;
    private Button next;
    private String formated_phone_no;
    private ProgressBar progress;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgot_phno = (EditText) findViewById(R.id.forgot_phno);
        progress = (ProgressBar) findViewById(R.id.progress);
        next = (Button) findViewById(R.id.next);
        next.setEnabled(false);

        forgot_phno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals(""))
                {
                    forgot_phno.setError("Enter Phone No");
                    next.setEnabled(false);
                } else if (s.toString().length()>10 || s.toString().length()<10) {
                    forgot_phno.setError("Enter Valid Mobile Number");
                    next.setEnabled(false);
                } else
                {
                    forgot_phno.setError(null);
                    next.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                formated_phone_no = "+91 "+forgot_phno.getText().toString().substring(0,4)+" "+forgot_phno.getText().toString().substring(4,7)+" "+forgot_phno.getText().toString().substring(7,10);
                Toast.makeText(ForgotPassword.this, formated_phone_no, Toast.LENGTH_SHORT).show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        formated_phone_no,
                        60,
                        TimeUnit.SECONDS,
                        ForgotPassword.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
//                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.d("Mohammed Saif",e.getMessage());
                                progress.setVisibility(View.GONE);
                                next.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onCodeSent(@NonNull String verificationid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                                //Open Verification Activity
                                progress.setVisibility(View.GONE);
                                next.setVisibility(View.VISIBLE);
                                Verification verification = new Verification(forgot_phno.getText().toString(),"next",verificationid);
                                verification.show(getSupportFragmentManager(),"Verification");
                            }
                        }
                );



            }
        });






    }
}