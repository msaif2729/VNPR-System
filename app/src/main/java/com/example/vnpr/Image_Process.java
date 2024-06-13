package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.io.IOException;
import java.text.Bidi;

public class Image_Process extends AppCompatActivity {

    private Bitmap bitmap;
    private ImageView img;
    private Button scan;
    private String extract_text;

    private TextRecognizer textRecognizer;

    public class Async extends AsyncTask<Bitmap,Bitmap,Bitmap>
    {
        ProgressDialog p;

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            try {
                publishProgress(bitmap);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = ProgressDialog.show(Image_Process.this,"Processing","Extracting Text....");

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            p.dismiss();
            finish();
        }

        @Override
        protected void onProgressUpdate(Bitmap... values) {
            super.onProgressUpdate(values);
            process(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_process);

        img = (ImageView) findViewById(R.id.img);
        scan = (Button) findViewById(R.id.scan);

        Intent i = getIntent();
        Uri uri1 = (Uri) i.getParcelableExtra("Image");
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        img.setImageBitmap(bitmap);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Async async = new Async();
                async.execute(bitmap);
            }
        });

    }

    public void process(Bitmap bitmap)
    {
        Toast.makeText(Image_Process.this, "Process function", Toast.LENGTH_SHORT).show();
        Mat inputImage = new Mat();

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize= 4;

        Utils.bitmapToMat(bitmap,inputImage);

        Mat grayImage = new Mat();
        Imgproc.cvtColor(inputImage,grayImage,Imgproc.COLOR_BGR2GRAY);

//        Mat eImag1 = new Mat();
//        Imgproc.equalizeHist(grayImage,eImag1);f

        Mat blurImage = new Mat();
        Imgproc.blur(grayImage, blurImage, new Size(3.7, 3.7), new Point(-1, -1));

//        Mat thresImage = new Mat();
//        Imgproc.threshold(grayImage, thresImage, 120, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
//
        Mat morphImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((3) , (3)));
        Imgproc.erode(blurImage,morphImage,kernel);
        Imgproc.dilate(morphImage,morphImage,kernel);

        Mat we = new Mat();
//        Core.addWeighted(morphImage,1,morphImage,1,1.2,we,0);
        Imgproc.equalizeHist(morphImage,we);



        int w = inputImage.width();
        int h = inputImage.height();

        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
        Utils.matToBitmap(we,bitmap);

        textRecognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());

        if(bitmap!=null)
        {
            InputImage image = InputImage.fromBitmap(bitmap,0);
            Task<com.google.mlkit.vision.text.Text> r = textRecognizer.process(image)
                    .addOnSuccessListener(new OnSuccessListener<com.google.mlkit.vision.text.Text>() {
                        @Override
                        public void onSuccess(com.google.mlkit.vision.text.Text text) {
                            Log.d("Mohammed Saif Ansari", text.getText());
//                            Toast.makeText(MainActivity.this, text.getText(), Toast.LENGTH_LONG).show();
//                                    text2.setText(text.getText());
                             extract_text = format_text(text.getText());
                            Intent intent = new Intent(Image_Process.this,HomePage.class);
                            intent.putExtra("Extracted",extract_text);
                            startActivity(intent);
//                            vehicle_no.setText(extract_text);
                            Toast.makeText(Image_Process.this, extract_text, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Ansari Mohammed Saif ", String.valueOf(e));
                        }
                    });
        }

    }

    public String format_text(String str){
        int i=0;
        int len = str.length();
//        Toast.makeText(this, str.charAt(1), Toast.LENGTH_SHORT).show();
        String s = "";
        for(i=0;i<len;i++)
        {
            if("A".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "B".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "C".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "D".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "E".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "F".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "G".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "H".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "I".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "J".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "K".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "L".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "M".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "N".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "O".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "P".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "Q".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "R".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "S".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "T".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "U".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "V".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "W".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "X".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "Y".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "Z".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "0".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "1".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "2".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "3".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "4".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "5".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "6".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "7".equalsIgnoreCase(String.valueOf(str.charAt(i)))||
                    "8".equalsIgnoreCase(String.valueOf(str.charAt(i)))|| "9".equalsIgnoreCase(String.valueOf(str.charAt(i))))
            {
                s += String.valueOf(str.charAt(i));
            }
        }
        str = s.toUpperCase();
        return str;
    }


}