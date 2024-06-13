package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Camera extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;
    ImageButton capture;
    private TextRecognizer textRecognizer;
    String extract_text=null;
    int counter = 0;
    Mat inputImage;
    Bitmap bitmap;
    TextView t1;
    private Mat mrgba;
    private Mat mgray;
    private Mat minter;
    Mat hrarchy;
    List<MatOfPoint> contours;
    private int w,h;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraBridgeViewBase = (CameraBridgeViewBase) findViewById(R.id.cameraview);
        capture = (ImageButton) findViewById(R.id.capture);
        t1 = (TextView) findViewById(R.id.t1);
//        t1.setVisibility(View.INVISIBLE);
        getPermission();

        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener((CameraBridgeViewBase.CvCameraViewListener2) this);

        if(OpenCVLoader.initDebug())
        {
            cameraBridgeViewBase.setCameraPermissionGranted();
            cameraBridgeViewBase.enableView();
        }


    }

    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }

    public void getPermission()
    {
        Context context = Camera.this;
        if(context.checkSelfPermission(android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(new String[]{Manifest.permission.CAMERA},101);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0 && grantResults[0]!=PackageManager.PERMISSION_GRANTED)
        {
            getPermission();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(OpenCVLoader.initDebug())
        {
//            Toast.makeText(this, "OpenCV Loaded", Toast.LENGTH_SHORT).show();
            cameraBridgeViewBase.setCameraPermissionGranted();
            cameraBridgeViewBase.enableView();
        }
        else
        {
            Toast.makeText(this, "OpenCV Not Loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cameraBridgeViewBase!=null)
        {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        inputImage = new Mat(890,840, CvType.CV_8UC4);
        w = inputImage.width();
        h = inputImage.height();
    }

    @Override
    public void onCameraViewStopped() {


    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        inputImage = inputFrame.rgba();

        Mat grayImage = new Mat();
        Imgproc.cvtColor(inputImage,grayImage,Imgproc.COLOR_BGR2GRAY);

//        Mat eImag1 = new Mat();
//        Imgproc.equalizeHist(grayImage,eImag1);f

        Mat blurImage = new Mat();
        Imgproc.blur(grayImage, blurImage, new Size(3.7, 3.7), new Point(-1, -1));

        Mat thresImage = new Mat();
        Imgproc.threshold(blurImage, thresImage, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);

        Mat morphImage = new Mat();
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((3) , (3)));
        Imgproc.erode(blurImage,morphImage,kernel);
        Imgproc.dilate(morphImage,morphImage,kernel);

        Mat we = new Mat();
//        Core.addWeighted(morphImage,1,morphImage,1,1.2,we,0);
        Imgproc.equalizeHist(morphImage,we);
        Imgproc.equalizeHist(we,we);

//        Mat edges = new Mat();
//        Imgproc.Canny(we,edges,50,150);

//Contours
        Mat minter = new Mat();
        Mat hrarchy = new Mat();
        MatOfPoint p = null;

        ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();

        //Contours -> Canny
        Imgproc.Canny(we,minter,80,100);
        Imgproc.findContours(minter,contours,hrarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
        hrarchy.release();

        Log.d("Mohammed Saif", String.valueOf(contours.size()));

        for(int i = 0 ;i <contours.size();i++)
        {
            //Minimum size allowed for consideration
            MatOfPoint2f acurve = new MatOfPoint2f();
            MatOfPoint2f c2f = new MatOfPoint2f(contours.get(i).toArray());

            //Processing of mMop2f1 which is in type of MatOfPoint2f
            double adistance = Imgproc.arcLength(c2f,true)*0.01;
            Imgproc.approxPolyDP(c2f,acurve,adistance,true);

            //convert back to matOfPoint
            p = new MatOfPoint(acurve.toArray());

            Log.d("saif",c2f.toString());

            //Get bounding rect
            Rect rect = Imgproc.boundingRect(p);
            double aspectRatio = (double) rect.width / rect.height;

            double he = rect.height;
            double wi = rect.width;



            if ( aspectRatio > 0.5429 && aspectRatio < 1 && rect.width > 115 && rect.height > 215) {
                Imgproc.rectangle(inputImage,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(255,0,25),4);
//                Imgproc.putText(mat,"Number Plate",rect.tl(),Imgproc.FONT_HERSHEY_SIMPLEX,2,new Scalar(0.255,255),4);

            }


        }

//        Mat claheImage = new Mat();
//        Imgproc.createCLAHE(2.0, new Size(8, 8)).apply(we, claheImage);

        int w = inputImage.width();
        int h = inputImage.height();

        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
        Utils.matToBitmap(we,bitmap);


        textRecognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());

        if(bitmap!=null)
        {
            try {
                InputImage image = InputImage.fromBitmap(bitmap, 0);
                Task<Text> task = textRecognizer.process(image);
                Text text = Tasks.await(task); // Wait for the task to complete
                if (text != null) {
                    extract_text = format_text(text.getText());
                    Log.d("TextRecognition", "Detected text: " + extract_text);
                    t1.setVisibility(View.VISIBLE);
                    t1.setText(extract_text);


                    // Perform any UI updates or further processing here
                } else {
                    t1.setVisibility(View.INVISIBLE);
                    Log.e("TextRecognition", "Text recognition result is null");
                }
            } catch (Exception e) {
                Log.e("TextRecognition", "Text recognition failed", e);
            }
        }

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textRecognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
                if(bitmap!=null)
                {
                    InputImage image = InputImage.fromBitmap(bitmap,0);
                    Task<com.google.mlkit.vision.text.Text> r = textRecognizer.process(image)
                            .addOnSuccessListener(new OnSuccessListener<Text>() {
                                @Override
                                public void onSuccess(Text text) {
                                    Log.d("Mohammed Saif Ansari", text.getText());
//                            Toast.makeText(MainActivity.this, text.getText(), Toast.LENGTH_LONG).show();
//                                    text2.setText(text.getText());
                                    Intent intent = new Intent(Camera.this, HomePage.class);
                                    extract_text = format_text(text.getText());
                                    Toast.makeText(Camera.this, extract_text, Toast.LENGTH_SHORT).show();
                                    intent.putExtra("Extracted",extract_text);
                                    startActivity(intent);
                                    finish();}
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Ansari Mohammed Saif ", String.valueOf(e));}

                            });
                }
            }
        });
        return inputImage;
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