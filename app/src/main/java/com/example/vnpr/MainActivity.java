//package com.example.vnpr;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.Rect;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.SurfaceView;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.android.gms.tasks.Tasks;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.text.Text;
//import com.google.mlkit.vision.text.TextRecognition;
//import com.google.mlkit.vision.text.TextRecognizer;
//import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
//
//import org.opencv.android.BaseLoaderCallback;
//import org.opencv.android.CameraBridgeViewBase;
//import org.opencv.android.JavaCamera2View;
//import org.opencv.android.OpenCVLoader;
//import org.opencv.android.Utils;
//import org.opencv.core.CvType;
//import org.opencv.core.Mat;
//import org.opencv.core.Core;
//import org.opencv.core.MatOfPoint;
//import org.opencv.core.MatOfPoint2f;
//import org.opencv.core.Point;
//import org.opencv.core.Scalar;
//import org.opencv.core.Size;
//import org.opencv.dnn.Dnn;
//import org.opencv.imgproc.Imgproc;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.mlkit.common.MlKitException;
//import com.google.mlkit.vision.common.InputImage;
//import com.google.mlkit.vision.objects.DetectedObject;
//import com.google.mlkit.vision.objects.ObjectDetection;
//import com.google.mlkit.vision.objects.ObjectDetector;
//import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
//
//
//public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
//
//    CameraBridgeViewBase cameraBridgeViewBase;
//    BaseLoaderCallback baseLoaderCallback;
//    private TextRecognizer textRecognizer;
//    private ObjectDetector objectDetector;
//
//
//    private static final String TAG = "ObjectDetection";
//    int counter = 0;
//    Button capture,next;
//    Mat inputImage;
//    Bitmap bitmap;
//
//    TextView text2;
//
//    private Mat mrgba;
//    private Mat mgray;
//    private Mat minter;
//    Mat hrarchy;
//    List<MatOfPoint> contours;
//    private int w,h;
//
//
//    class AsyncTextRecognition extends AsyncTask<Bitmap, Void, Text> {
//
//        private static final int FRAME_SKIP_INTERVAL = 500; // Process every 3rd frame
//        private int frameCount = 0;
//
//        @Override
//        protected Text doInBackground(Bitmap... bitmaps) {
//            try {
//                if (frameCount % FRAME_SKIP_INTERVAL == 0) { // Check if it's time to process the frame
//                    InputImage image = InputImage.fromBitmap(bitmaps[0], 0);
//                    Task<Text> task = textRecognizer.process(image);
//                    Tasks.await(task); // Wait for the task to complete
//                    return task.getResult(); // Return the text recognition result
//                }
//                frameCount++; // Increment the frame count
//            } catch (Exception e) {
//                Log.e("AsyncTextRecognition", "Text recognition failed", e);
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Text text) {
//            if (text != null) {
//                Log.d("AsyncTextRecognition", text.getText());
//                // Perform any UI updates or further processing here
//            } else {
//                Log.e("AsyncTextRecognition", "Text recognition result is null");
//            }
//        }
//    }
//
//
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        textRecognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());
//        ObjectDetectorOptions options =
//                new ObjectDetectorOptions.Builder()
//                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
//                        .enableMultipleObjects()
//                        .enableClassification()
//                        .build();
//
//        objectDetector = ObjectDetection.getClient(options);
//
//
//        if(OpenCVLoader.initDebug())
//        {
//            Toast.makeText(this, "OpenCV Loaded", Toast.LENGTH_SHORT).show();
//        }
//        else
//        {
//            Toast.makeText(this, "OpenCV Not Loaded", Toast.LENGTH_SHORT).show();
//        }
//
//        capture  = findViewById(R.id.capture);
//        next = (Button)findViewById(R.id.next);
//        cameraBridgeViewBase = (JavaCamera2View) findViewById(R.id.jcameraview);
//        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
//        cameraBridgeViewBase.setCvCameraViewListener((CameraBridgeViewBase.CvCameraViewListener2) this);
//
//        baseLoaderCallback = new BaseLoaderCallback(this)
//        {
//            @Override
//            public void onManagerConnected(int status) {
//                super.onManagerConnected(status);
//                if(status==BaseLoaderCallback.SUCCESS)
//                {
//                    cameraBridgeViewBase.setCameraPermissionGranted();
//                    cameraBridgeViewBase.enableView();
//                }
//                else
//                {
//                    super.onManagerConnected(status);
//                }
//            }
//        };
//
//
//        next.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this,testing.class));
//            }
//        });
//
//
//
//    }
//
//    @Override
//    public void onResume()
//    {
//        super.onResume();
//        if(OpenCVLoader.initDebug())
//        {
////            Toast.makeText(this, "OpenCV Loaded", Toast.LENGTH_SHORT).show();
//            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
//        }
//        else
//        {
//            Toast.makeText(this, "OpenCV Not Loaded", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        if(cameraBridgeViewBase!=null)
//        {
//            cameraBridgeViewBase.disableView();
//        }
//    }
//
//    @Override
//    public void onCameraViewStarted(int width, int height) {
//        inputImage = new Mat(890,840, CvType.CV_8UC4);
//        w = inputImage.width();
//        h = inputImage.height();
//    }
//
//    @Override
//    public void onCameraViewStopped() {
//
//
//    }
//
//    @Override
//    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
//         inputImage = inputFrame.rgba();
//
//        Mat grayImage = new Mat();
//        Imgproc.cvtColor(inputImage,grayImage,Imgproc.COLOR_BGR2GRAY);
//
////        Mat eImag1 = new Mat();
////        Imgproc.equalizeHist(grayImage,eImag1);f
//
//        Mat blurImage = new Mat();
//        Imgproc.blur(grayImage, blurImage, new Size(3.7, 3.7), new Point(-1, -1));
//
////        Mat thresImage = new Mat();
////        Imgproc.threshold(grayImage, thresImage, 120, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
////
//        Mat morphImage = new Mat();
//        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) , (2*2)));
//        Imgproc.erode(blurImage,morphImage,kernel);
////        Imgproc.dilate(blurImage,morphImage,kernel);
////
//
//        Mat we = new Mat();
//        Core.addWeighted(morphImage,2,morphImage,2,2.5,we,0);
//
////        Mat claheImage = new Mat();
////        Imgproc.createCLAHE(2.0, new Size(8, 8)).apply(we, claheImage);
//
//        int w = inputImage.width();
//        int h = inputImage.height();
//
//        bitmap = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);
//        Utils.matToBitmap(we,bitmap);
//
////        capture.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
//
//
//        if (bitmap != null) {
//            try {
//                InputImage image = InputImage.fromBitmap(bitmap, 0);
//                Task<Text> task = textRecognizer.process(image);
//                Text text = Tasks.await(task); // Wait for the task to complete
//                if (text != null) {
//                    Log.d("TextRecognition", "Detected text: " + text.getText());
//                    // Perform any UI updates or further processing here
//                } else {
//                    Log.e("TextRecognition", "Text recognition result is null");
//                }
//
//                objectDetector.process(image)
//                        .addOnSuccessListener(new OnSuccessListener<List<DetectedObject>>() {
//                            @Override
//                            public void onSuccess(List<DetectedObject> detectedObjects) {
//                                for (DetectedObject detectedObject : detectedObjects) {
//                                    Rect boundingBox = detectedObject.getBoundingBox();
//                                    List<DetectedObject.Label> labels = detectedObject.getLabels();
//                                    for (DetectedObject.Label label : labels) {
//                                        String labelText = label.getText();
//                                        float confidence = label.getConfidence();
//                                        Log.d(TAG, "Detected object: " + labelText + " Confidence: " + confidence);
//                                    }
//                                }
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                // Handle failure
//                                if (e instanceof MlKitException) {
//                                    int errorCode = ((MlKitException) e).getErrorCode();
//                                    Toast.makeText(MainActivity.this, "Object detection failed with error code: " + errorCode, Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//
//
//
//            } catch (Exception e) {
//                Log.e("TextRecognition", "Text recognition failed", e);
//            }
//
//
//
////                InputImage image = InputImage.fromBitmap(bitmap,0);
////                Task<Text> r = textRecognizer.process(image)
////                        .addOnSuccessListener(new OnSuccessListener<Text>() {
////                            @Override
////                            public void onSuccess(Text text) {
////                                Log.d("Mohammed Saif Ansari", text.getText());
//////                            Toast.makeText(MainActivity.this, text.getText(), Toast.LENGTH_LONG).show();
//////                                    text2.setText(text.getText());
//////                                Intent intent = new Intent(MainActivity.this, com.example.vnpr.Text.class);
//////
//////                                intent.putExtra("Converted",text.getText());
//////                                startActivity(intent);
////                                Log.d("Converted",text.getText());
////                            }
////                        }).addOnFailureListener(new OnFailureListener() {
////                            @Override
////                            public void onFailure(@NonNull Exception e) {
////                                Log.e("Ansari Mohammed Saif ", String.valueOf(e));
////                            }
////                        });
//            }


//
//
//
////            }
////        });
//        return we;
//
//    }
//
//}

package com.example.vnpr;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {

    CameraBridgeViewBase cameraBridgeViewBase;
    BaseLoaderCallback baseLoaderCallback;

    private static final String TAG = "LicensePlateDetection";
    private JavaCamera2View javaCameraView;
    private Mat mRgba;
    private Mat mGray;
    private Mat mIntermediateMat;
    private Scalar CONTOUR_COLOR = new Scalar(0, 255, 0, 255);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (OpenCVLoader.initDebug()) {
            Toast.makeText(this, "OpenCV Loaded", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "OpenCV Not Loaded", Toast.LENGTH_SHORT).show();
        }

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Load the Haar cascade classifier for license plate detection
//        plateDetector = new CascadeClassifier(getCascadeFilePath(R.raw.haarcascade_russian_plate_number));
//        if (plateDetector.empty()) {
//            Log.e("TAG", "Failed to load cascade classifier");
//            return;
//        }

        cameraBridgeViewBase = (JavaCamera2View) findViewById(R.id.jcameraview);
        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(this);

        baseLoaderCallback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {
                super.onManagerConnected(status);
                if (status == BaseLoaderCallback.SUCCESS) {
                    cameraBridgeViewBase.setCameraPermissionGranted();
                    cameraBridgeViewBase.enableView();
                } else {
                    super.onManagerConnected(status);
                }
            }
        };
    }

    // Function to get file path from raw resource ID
    private String getCascadeFilePath(int resourceId) {
        // Get the InputStream for the raw resource
        InputStream inputStream = getResources().openRawResource(resourceId);
        // Create a temporary file in the app's internal storage directory
        File cascadeDir = getDir("cascade", Context.MODE_PRIVATE);
        File cascadeFile = new File(cascadeDir, "haarcascade_russian_plate_number.xml");
        try {
            // Write the contents of the raw resource to the temporary file
            OutputStream outputStream = new FileOutputStream(cascadeFile);
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return the absolute file path of the temporary file
        return cascadeFile.getAbsolutePath();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OpenCVLoader.initDebug()) {
            baseLoaderCallback.onManagerConnected(baseLoaderCallback.SUCCESS);
        } else {
            Toast.makeText(this, "OpenCV Not Loaded", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cameraBridgeViewBase != null) {
            cameraBridgeViewBase.disableView();
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat();
        mGray = new Mat();
        mIntermediateMat = new Mat();
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
        mGray.release();
        mIntermediateMat.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        Mat rgba = inputFrame.rgba();
        mRgba = inputFrame.rgba();
        Mat gray = inputFrame.gray();

//        /Gray scale Conversion
                Imgproc.cvtColor(rgba,gray,Imgproc.COLOR_RGBA2GRAY);


                //Median Blurring
        Mat blur = new Mat();
                Imgproc.medianBlur(gray, blur, 5);

                //Histogram Equalization
        Mat eui = new Mat();
                Imgproc.equalizeHist(blur,eui);


                //Adaptive Thresholding
        Mat thr = new Mat();
//                Imgproc.adaptiveThreshold(eui,thr,150,Imgproc.ADAPTIVE_THRESH_MEAN_C,Imgproc.THRESH_BINARY,9,9);

        Mat thresImage = new Mat();
        Imgproc.threshold(blur, thr, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);



        //Erode & dilate
        Mat dil = new Mat();
                Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) + 1, (2*2)+1));
                Imgproc.erode(thr,dil,kernel);
//                Imgproc.dilate(rgba,rgba,kernel);


                //Histogram Equalization
                Imgproc.equalizeHist(dil,dil);

                //Contours
                Mat minter = new Mat();
                Mat hrarchy = new Mat();
                MatOfPoint p = null;

                ArrayList<MatOfPoint> contours = new ArrayList<MatOfPoint>();


                //Contours -> Canny
                Mat edge = new Mat();
                Imgproc.Canny(dil,minter,80,100);

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



                    if ( rect.width > 115 && rect.height > 215) {
                        Imgproc.rectangle(rgba,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(255,0,25),4);
//                Imgproc.putText(mat,"Number Plate",rect.tl(),Imgproc.FONT_HERSHEY_SIMPLEX,2,new Scalar(0.255,255),4);

                    }


                }

        return thr;
    }
}
