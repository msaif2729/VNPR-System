package com.example.vnpr;

import static android.app.Activity.RESULT_OK;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;


public class Profile extends Fragment {

    private TextView user_name,username;
    private EditText phoneno,password;
    private ListView list_item;
    ImageView userimg;
    String name, uname, pass, phn_no;
    SessionMaintain sessionMaintain;
    private String[] arr = {"Update Profile Photo","Change Password","My Vehicles","Logout"};


//    public Profile(String name,String uname,String pass,String phn_no)
//    {
//       this.name = name;
//       this.uname =uname;
//       this.pass = pass;
//       this.phn_no = phn_no;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list_item = view.findViewById(R.id.list_item);
        username = view.findViewById(R.id.username);
        user_name = view.findViewById(R.id.user_name);
        phoneno = view.findViewById(R.id.phoneno);
        password = view.findViewById(R.id.password);
        userimg = view.findViewById(R.id.userimg);
        sessionMaintain = new SessionMaintain(getContext());
        String name = sessionMaintain.getSession("key_name");
        String uname = sessionMaintain.getSession("key_uname");
        String pass = sessionMaintain.getSession("key_pass");
        String phn_no = sessionMaintain.getSession("key_phno");
        String format_phno = phn_no.substring(0,2)+"******"+phn_no.substring(8,10);
        String format_pass = pass.substring(0,((pass.length()/2)/2))+"****"+pass.substring(pass.length()-2,pass.length());
        user_name.setText(name);
        username.setText(uname);
        phoneno.setText(format_phno);
        password.setText(format_pass);
        if(sessionMaintain.getSession("image")!=null)
        {
            Bitmap bitmap = SessionMaintain.loadImageFromInternalStorage(sessionMaintain.getSession("image"));
            userimg.setImageBitmap(bitmap);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),R.layout.list_design,arr);
        list_item.setAdapter(adapter);
        list_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                {
                    Toast.makeText(getContext(), arr[0], Toast.LENGTH_SHORT).show();
                    CropImage.activity().start(getContext(), Profile.this);
                }
                else if(i==1)
                {
//                    Toast.makeText(getContext(), arr[1], Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Profile.this.getContext(),ChangePassword.class);
                    intent.putExtra("Profile","profile");
                    startActivity(intent);
                }
                else if(i==2)
                {
//                    Toast.makeText(getContext(), arr[2], Toast.LENGTH_SHORT).show();
                    if(sessionMaintain.getSession("key_vehi_no")==null)
                    {
                        Toast.makeText(Profile.this.getContext(), "Vehile Is Not Added ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent=new Intent(Profile.this.getContext(), Vehicle_Desp.class);
                        String plateno = sessionMaintain.getSession("key_vehi_no");
                        String model_name = sessionMaintain.getSession("key_image_model");
                        String name = sessionMaintain.getSession("key_own_name_recent");
                        String type = sessionMaintain.getSession("key_own_type_recent");
                        intent.putExtra("vehicle_plate_no", plateno);
                        intent.putExtra("model_image",model_name);
                        intent.putExtra("oname",name);
                        intent.putExtra("otype",type);
                        startActivity(intent);
                    }
                }
                else
                {
//                    Toast.makeText(getContext(), arr[3], Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage(Html.fromHtml("<font color='#00000' style='bold' >Do you want to LOGOUT ?</font>"));
                    builder.setTitle(Html.fromHtml("<font color='#2a9df4'>Logout! </font>"));
                    builder.setCancelable(false);
                    builder.setPositiveButton(Html.fromHtml("<font color='#2a9df4'>LOGOUT</font>"), (DialogInterface.OnClickListener) (dialog, which) -> {
                        sessionMaintain.logout();
                    });
                    builder.setNegativeButton(Html.fromHtml("<font color='#2a9df4'>CANCEL</font>"), (DialogInterface.OnClickListener) (dialog, which) -> {
                        dialog.cancel();
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

        list_item.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                return (event.getAction() == MotionEvent.ACTION_MOVE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = null;
//                userimg.setImageBitmap(result.getBitmap());
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(Profile.this.getContext().getContentResolver(), result.getUri());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                userimg.setImageBitmap(bitmap);
                String path = SessionMaintain.saveImageToInternalStorage(getContext(),bitmap);
                sessionMaintain.storeImage(path);


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }


}