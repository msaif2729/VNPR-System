package com.example.vnpr;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.Base64;

public class SessionMaintain {
    private Context context;
    private SharedPreferences sharedPreferences;
    private final String PREF_MEMORY = "vnpr-app";
    private final int PREF_MODE = 0;
    private final String KEY_NAME = "key_name";
    private final String KEY_UNAME = "key_uname";
    private final String KEY_PASS = "key_pass";
    private final String KEY_PHNO = "key_phno";
    private final String KEY_VEHI_NO = "key_vehi_no";
    private final String KEY_IMAGE_MODEL = "key_image_model";
    private final String KEY_OWN_NAME_RECENT = "key_own_name_recent";
    private final String KEY_REGNO_RECENT = "key_regno_recent";

    private final String KEY_OWN_TYPE_RECENT = "key_own_type_recent";

    private final String KEY_IMAGE_MODEL_RECENT = "key_image_model_recent";
    private final String KEY_LOGIN = "key_logged_in";


    SharedPreferences.Editor editor;
    public SessionMaintain(Context context) {
        this.context = context;
        this.sharedPreferences = context.getSharedPreferences(PREF_MEMORY,PREF_MODE);
         editor = sharedPreferences.edit();
    }

    public boolean checkSession()
    {
        if(sharedPreferences.contains(KEY_LOGIN))
        {
            return true;
        }
        else{
            return false;
        }
    }

    public String getSession(String key)
    {
        String value = sharedPreferences.getString(key,null);
        return value;
    }


    public void createSession(String name,String uname,String pass, String phno)
    {
        editor.putString(KEY_NAME,name);
        editor.putString(KEY_UNAME,uname);
        editor.putString(KEY_PASS,pass);
        editor.putString(KEY_PHNO,phno);
        editor.putBoolean(KEY_LOGIN,true);
        editor.commit();
        editor.apply();
    }

    public void My_vehile(String vehicle_no,String image,String name,String otype)
    {
        editor.putString(KEY_VEHI_NO,vehicle_no);
        editor.putString(KEY_IMAGE_MODEL,image);
        editor.putString(KEY_OWN_NAME_RECENT,name);
        editor.putString(KEY_OWN_TYPE_RECENT,otype);
        editor.commit();
        editor.apply();
    }

    public void updateSession(String pass)
    {
        editor.putString(KEY_PASS,pass);
        editor.commit();
        editor.apply();
    }

    public void logout()
    {
        editor.remove(KEY_NAME);
        editor.remove(KEY_UNAME);
        editor.remove(KEY_PHNO);
        editor.remove(KEY_PASS);
        editor.remove(KEY_LOGIN);
        editor.remove(KEY_VEHI_NO);
        editor.remove(KEY_IMAGE_MODEL);
        editor.remove("image");
        editor.clear();
        editor.apply();
        editor.commit();
        Intent intent = new Intent(context,GetStarted.class);
        context.startActivity(intent);

    }
    public void remove_from_myvehicle()
    {
        editor.remove(KEY_VEHI_NO);
        editor.remove(KEY_IMAGE_MODEL);
        editor.apply();
        editor.commit();
    }

    public void storeImage(String image)
    {
        editor.putString("image",image);
        editor.apply();
        editor.commit();
    }

//    public static byte[] bitmapToByteArray(Bitmap bitmap) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        return stream.toByteArray();
//    }
//
//    public static Bitmap byteArrayToBitmap(byte[] byteArray) {
//        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//    }
//
//    public void saveByteArrayToSharedPreferences(Context context, String key, byte[] byteArray) {
//
//        // Serialize byte array using Gson
//        Gson gson = new Gson();
//        String json = gson.toJson(byteArray);
//        editor.putString(key, json);
//        editor.apply();
//        editor.commit();
//    }
//
//    public static byte[] getByteArrayFromSharedPreferences(Context context, String key) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("MyPreferences", Context.MODE_PRIVATE);
//        String json = sharedPreferences.getString(key, null);
//        if (json != null) {
//            // Deserialize byte array using Gson
//            Gson gson = new Gson();
//            Type type = new TypeToken<byte[]>(){}.getType();
//            return gson.fromJson(json, type);
//        } else {
//            return null;
//        }
//    }


    public static String saveImageToInternalStorage(Context context, Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File imagePath = new File(directory, "image.png");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imagePath.getAbsolutePath();
    }

    public static Bitmap loadImageFromInternalStorage(String path) {
        File imgFile = new File(path);
        if (imgFile.exists()) {
            return BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        }
        return null;
    }




}
