package com.example.vnpr;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Verification extends BottomSheetDialogFragment {


    private EditText otp1,otp2,otp3,otp4,otp5,otp6;
    private String name,uname,pass,phone_no;
    protected String btn,verificationid=null,verificationid2=null;
    private String forgot_phn_no,formated_phone_no;
    private TextView sendagain,phn_number,txttimer;
    public Button verify;
    private FirebaseFirestore db;
    private Map<String,String> user;
    private Context context;
    CountDownTimer countDownTimer;
    int hour = 1;
    int min = 0;
    private View thisview;

    public Verification() {

    }

    public Verification(String forgot_phn_no,String btn,String verificationid)
    {
        this.phone_no = forgot_phn_no;
        this.forgot_phn_no = forgot_phn_no;
        this.btn=btn;
        this.verificationid=verificationid;
    }

    public Verification(String name,String uname,String pass,String phone_no,String btn,String verify_id) {
        // Required empty public constructor
        this.name = name;
        this.uname = uname;
        this.pass = pass;
        this.phone_no = phone_no;
        this.btn = btn;
        this.verificationid=verify_id;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txttimer = view.findViewById(R.id.txttimer);
        sendagain = view.findViewById(R.id.sendagain);
        phn_number = view.findViewById(R.id.phn_number);
        verify = view.findViewById(R.id.verify);
        otp1 = view.findViewById(R.id.otp1);
        otp2 = view.findViewById(R.id.otp2);
        otp3 = view.findViewById(R.id.otp3);
        otp4 = view.findViewById(R.id.otp4);
        otp5 = view.findViewById(R.id.otp5);
        otp6 = view.findViewById(R.id.otp6);
        thisview=view;

        String format = phone_no.substring(0,4)+"****"+phone_no.substring(8,10);
        phn_number.setText("We send verification code to your \n"+format+", You can check your message box");

        //disables sendagain
        sendagain.setEnabled(false);
        sendagain.setTextColor(getResources().getColor(R.color.gray));
        //For timer
        countdown();
        sendagain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(getContext(),"SendAgain",Toast.LENGTH_LONG).show();
                formated_phone_no = "+91 "+phone_no.substring(0,4)+" "+phone_no.substring(4,7)+" "+phone_no.substring(7,10);
                Toast.makeText(getContext(), formated_phone_no, Toast.LENGTH_SHORT).show();
                Dialog dialog = new Dialog(Verification.this.getContext());
                dialog.setContentView(R.layout.progressbar);
                dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                dialog.setCancelable(false);
                dialog.show();
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        formated_phone_no,
                        60,
                        TimeUnit.SECONDS,
                        Verification.this.getActivity(),
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                Toast.makeText(getContext(), "Verification Failed Server Down ", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                                Log.d("Mohammed Saif",e.getMessage());
                            }

                            @Override
                            public void onCodeSent(@NonNull String verifyid, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                Toast.makeText(getContext(), "Code Sent", Toast.LENGTH_SHORT).show();
                                verificationid2 = verifyid;
                                sendagain.setEnabled(false);
                                dialog.dismiss();
                                countdown();
                            }
                        }
                );
            }
        });


        EditText[] editTexts = {otp1,otp2,otp3,otp4,otp5,otp6};

        otp1.addTextChangedListener(new MyTextChange(otp1,editTexts,btn,verify));
        otp2.addTextChangedListener(new MyTextChange(otp2,editTexts,btn,verify));
        otp3.addTextChangedListener(new MyTextChange(otp3,editTexts,btn,verify));
        otp4.addTextChangedListener(new MyTextChange(otp4,editTexts,btn,verify));
        otp5.addTextChangedListener(new MyTextChange(otp5,editTexts,btn,verify));
        otp6.addTextChangedListener(new MyTextChange(otp6,editTexts,btn,verify));

        context = getContext();
        db = FirebaseFirestore.getInstance();
        user = new HashMap<>();
        user.put("name",name);
        user.put("uname",uname);
        user.put("pass",pass);
        user.put("phn_no",phone_no);



        if(btn=="next")
        {
            verify.setId(R.id.verify_next);
            verify.setEnabled(false);
        }
        if(btn=="register")
        {
            verify.setId(R.id.verify_register);
            verify.setEnabled(false);
        }
        if(verify.getId()==R.id.verify_next)
        {
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Login", Toast.LENGTH_SHORT).show();
                    String code = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();
                    if(verificationid!=null)
                    {
//                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationid,code);
//                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful())
//                                {
//                                    Intent intent = new Intent(view.getContext(), ChangePassword.class);
//                                    startActivity(intent);
//                                }
//                                else {
//                                    Toast.makeText(view.getContext(), "The Verification Code Is Invalid", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });

                        Login_Verify(verificationid,code);

                    }
//                    else
//                        Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    if(verificationid2!=null)
                    {
                        Login_Verify(verificationid2,code);
                        //                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationid2,code);
//                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful())
//                                {
//                                    Intent intent = new Intent(view.getContext(), ChangePassword.class);
//                                    startActivity(intent);
//                                }
//                                else {
//                                    Toast.makeText(view.getContext(), "The Verification Code Is Invalid", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
                    }
//                    else
//                        Toast.makeText(view.getContext(), "Failed", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(view.getContext(), ChangePassword.class);
//                    startActivity(intent);
                }
            });
        }



        if(verify.getId()==R.id.verify_register)
        {
            verify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String code = otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString()+otp5.getText().toString()+otp6.getText().toString();

                    if(verificationid!=null)
                    {
//                        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationid,code);
//                        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if(task.isSuccessful())
//                                {
//                                    Intent intent = new Intent(view.getContext(), HomePage.class);
//                                    startActivity(intent);
//                                    db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
//                                        @Override
//                                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
//                                            if(error!=null )
//                                            {
//                                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
//                                            }
//                                            db.collection("users").document(uname).set(user);
//                                            SessionMaintain sessionMaintain = new SessionMaintain(getContext());
//                                            sessionMaintain.createSession(name,uname,pass,phone_no);
//                                            Intent intent1 = new Intent(getContext(),HomePage.class);
//                                            startActivity(intent1);
//                                            Toast.makeText(context, "Register : SuccessFully Registered", Toast.LENGTH_SHORT).show();
//                                            dismiss();
//                                        }
//                                    });
//                                }
//                                else {
//                                    Toast.makeText(view.getContext(), "Register : The Verification Code Is Invalid", Toast.LENGTH_SHORT).show();
//
//                                }
//                            }
//                        });
                        Register_Verify(verificationid,code);
                    }
                    if(verificationid2!=null)
                    {
                        Register_Verify(verificationid2,code);
                    }

                }
            });
        }
    }

    //For Timer
    public void countdown()
    {

        countDownTimer = new CountDownTimer(61000, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                txttimer.setText(f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                txttimer.setText("00:00");
                sendagain.setEnabled(true);
                sendagain.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        };
        countDownTimer.start();
    }

    public void Login_Verify(String verifycode,String code)
    {

            PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verifycode,code);
            FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful())
                    {
                        Intent intent = new Intent(thisview.getContext(), ChangePassword.class);
                        intent.putExtra("ForgotPassword","forgotPassword");
                        startActivity(intent);
                        countDownTimer.cancel();
                    }
                    else {
                        Toast.makeText(thisview.getContext(), "The Verification Code Is Invalid", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void Register_Verify(String verifycode,String code)
    {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationid,code);
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                            if(error!=null )
                            {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                            }
                            db.collection("users").document(uname).set(user);
                            SessionMaintain sessionMaintain = new SessionMaintain(context);
                            sessionMaintain.createSession(name,uname,pass,phone_no);
                            Intent intent1 = new Intent(getContext(),HomePage.class);
                            startActivity(intent1);
                            Toast.makeText(context, "Register : SuccessFully Registered", Toast.LENGTH_SHORT).show();
                            dismiss();
                            countDownTimer.cancel();
                        }
                    });
                }
                else {
                    Toast.makeText(thisview.getContext(), "Register : The Verification Code Is Invalid", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

}