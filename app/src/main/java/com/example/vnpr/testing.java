package com.example.vnpr;

import static org.opencv.imgcodecs.Imgcodecs.imread;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
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
import java.util.Vector;

public class testing extends AppCompatActivity {


    Button select,convert,detect;
    ImageView img;
    ActivityResultLauncher activityResultLauncher;
    Bitmap gbitmap,ibitmap,bitmap;
    List<MatOfPoint> contours;

    TextView text1;
    private TextRecognizer textRecognizer;

    Uri uri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        select = (Button) findViewById(R.id.select);
        convert = (Button) findViewById(R.id.convert);
        detect = (Button) findViewById(R.id.detect);
        img = (ImageView) findViewById(R.id.img);
        text1 = (TextView) findViewById(R.id.text1);
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");


        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityResultLauncher.launch(intent);
            }
        });

        activityResultLauncher = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == RESULT_OK && result.getData()!=null)
            {
                uri = result.getData().getData();

                try {
                    ibitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

//                InputImage image = InputImage.fromBitmap(ibitmap,0);
//                Task<Text> r = textRecognizer.process(image)
//                        .addOnSuccessListener(new OnSuccessListener<Text>() {
//                            @Override
//                            public void onSuccess(Text text) {
//                                Log.d("Mohammed Saif Ansari",text.getText());
//                                Toast.makeText(testing.this, text.getText(), Toast.LENGTH_LONG).show();
//                                text1.setText(text.getText());
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                Log.e("Ansari Mohammed Saif ", String.valueOf(e));
//                            }
//                        });

                img.setImageBitmap(ibitmap);

            }
        });


        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Convert Bitmap to Mat (OpenCV format)
                Mat mat = new Mat();
                Utils.bitmapToMat(ibitmap, mat);

// Convert the image to grayscale
                Mat grayMat = new Mat();
                Imgproc.cvtColor(mat, grayMat, Imgproc.COLOR_BGR2GRAY);

// Get the resource ID of the cascade classifier XML file
                int cascadeResourceId = R.raw.haarcascade_russian_plate_number;

// Get the file path of the cascade classifier XML file
                String cascadeFilePath = getFilePathFromRawResource(cascadeResourceId);

// Load the cascade classifier from the file path
                CascadeClassifier plateDetector = new CascadeClassifier(cascadeFilePath);
                if (plateDetector.empty()) {
                    Log.e("TAG", "Failed to load cascade classifier");
                    return;
                }

// Detect number plates
                MatOfRect plates = new MatOfRect();
                plateDetector.detectMultiScale(grayMat, plates, 1.05, 7, 0, new Size(30, 30));

// Draw rectangles around detected number plates
                for (Rect rect : plates.toArray()) {
                    Imgproc.rectangle(mat, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 255), 2);
                }

// Convert Mat back to Bitmap for display
                Bitmap resultBitmap = Bitmap.createBitmap(mat.cols(), mat.rows(), Bitmap.Config.ARGB_8888);
                Utils.matToBitmap(mat, resultBitmap);

// Display the result in an ImageView or another suitable UI component
                img.setImageBitmap(resultBitmap);

            }
        });

        textRecognizer = TextRecognition.getClient(new DevanagariTextRecognizerOptions.Builder().build());

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Mat rgba = new Mat();
                Mat gray = new Mat();

                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inSampleSize= 4;
                bitmap=ibitmap;

                int w = bitmap.getWidth();
                int h = bitmap.getHeight();

                gbitmap = Bitmap.createBitmap(w,h,Bitmap.Config.RGB_565);

                Utils.bitmapToMat(bitmap,rgba);

                Mat grayImage = new Mat();
                Imgproc.cvtColor(rgba,grayImage,Imgproc.COLOR_BGR2GRAY);


                Mat blurImage = new Mat();
                Imgproc.blur(grayImage, blurImage, new Size(3.7, 3.7), new Point(-1, -1));
//                Imgproc.GaussianBlur(grayImage, blurImage, new Size(5, 5), 0);

                Mat thresImage = new Mat();
                Imgproc.threshold(blurImage, thresImage, 100, 55, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
//
                Mat morphImage = new Mat();
                Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size((2*2) , (2*2)));
                Imgproc.erode(thresImage,morphImage,kernel);


//        Imgproc.dilate(blurImage,morphImage,kernel);
//

                // Define the constant value to increase brightness
                Scalar brightnessValue = new Scalar(50, 50, 50); // Adjust this value as needed

                // Create a matrix with constant pixel values
                Mat constantImage =new Mat(morphImage.size(),morphImage.type());
                Core.multiply(constantImage, brightnessValue, constantImage);

//                Mat we = new Mat();
//                Core.addWeighted(morphImage,1, constantImage,1,2,we);

                Mat eImag1 = new Mat();
                Imgproc.equalizeHist(morphImage,eImag1);


//                Mat hrarchy = new Mat();
//                contours = new ArrayList<MatOfPoint>();
//                Imgproc.findContours(eImag1,contours,hrarchy,Imgproc.RETR_TREE,Imgproc.CHAIN_APPROX_SIMPLE,new Point(0,0));
//
//
//                // Iterate through contours and filter based on criteria
//                for (MatOfPoint contour : contours) {
//                    Rect boundingRect = Imgproc.boundingRect(contour);
//                    double aspectRatio = (double) boundingRect.width / boundingRect.height;
//
//                    // Add your criteria to filter contours
//                    if (aspectRatio > 2 && aspectRatio < 5 && boundingRect.width > 150 && boundingRect.height > 120) {
//                        // This contour may correspond to a number plate
//                        Imgproc.rectangle(eImag1, new Point(boundingRect.x, boundingRect.y), new Point(boundingRect.x + boundingRect.width, boundingRect.y + boundingRect.height), new Scalar(255, 255, 255), 4);
////                Imgproc.putText(eImag1, "Number Plate", boundingRect.tl(), Imgproc.FONT_HERSHEY_SIMPLEX, 2, new Scalar(0.255, 255), 4);
//                    }
//                }


                Utils.matToBitmap(eImag1,gbitmap);

                InputImage image = InputImage.fromBitmap(gbitmap,0);
                Task<Text> r = textRecognizer.process(image)
                        .addOnSuccessListener(new OnSuccessListener<Text>() {
                            @Override
                            public void onSuccess(Text text) {
                                Log.d("Mohammed Saif Ansari",text.getText());
                                Toast.makeText(testing.this, text.getText(), Toast.LENGTH_LONG).show();
                                        text1.setText("Extracted Text : \n"+text.getText());
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Ansari Mohammed Saif ", String.valueOf(e));
                            }
                        });

                img.setImageBitmap(gbitmap);

            }
        });

    }


//    / Function to get file path from raw resource ID
    private String getFilePathFromRawResource(int resourceId) {
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

}