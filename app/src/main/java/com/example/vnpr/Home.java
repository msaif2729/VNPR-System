package com.example.vnpr;

import static android.app.Activity.RESULT_OK;
import static androidx.core.content.ContextCompat.checkSelfPermission;

import android.Manifest;
import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.devanagari.DevanagariTextRecognizerOptions;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCamera2View;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Home extends Fragment{

    private CameraBridgeViewBase cameraBridgeViewBase;
    private BaseLoaderCallback baseLoaderCallback;
    private Button search_challan,search_vehicle,selectimg;
    private LinearLayout open_camera;
    protected EditText vehicle_no;
    private TextRecognizer textRecognizer;
    private ProgressBar progress;
    ImageView img;
    String str="",imageUrl,model,oname,otpye;
    String plateno;

    public Home() {
    }

    public Home(String str)
    {
        this.str=str;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraBridgeViewBase =  view.findViewById(R.id.jcameraview_home);
        search_challan = view.findViewById(R.id.search_challan);
        search_vehicle = view.findViewById(R.id.search_vehicle);
        open_camera = view.findViewById(R.id.open_camera);
        vehicle_no = view.findViewById(R.id.vehicle_no);
        selectimg = view.findViewById(R.id.selectimg);
        progress = (ProgressBar) view.findViewById(R.id.progress);
        img = view.findViewById(R.id.img);

        search_vehicle.setVisibility(View.VISIBLE);
        search_vehicle.setEnabled(false);
        progress.setVisibility(View.GONE);

        if(str!="")
        {
//            Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
            vehicle_no.setText(str);
            search_vehicle.setEnabled(true);
        }
        else{
//            Toast.makeText(getContext(), "Not Get Yet", Toast.LENGTH_SHORT).show();

        }



        Intent intent = new Intent(Home.this.getContext(), waiting_screen.class);
        //Search Vehicle
        search_vehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress.setVisibility(View.VISIBLE);
                search_vehicle.setVisibility(View.GONE);
                plateno=vehicle_no.getText().toString().trim().toUpperCase();
                DocumentReference doc_ref= FirebaseFirestore.getInstance().collection("vnpr").document(plateno);
                doc_ref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            model = documentSnapshot.getString("model");
                            oname = documentSnapshot.getString("own_name");
                            otpye = documentSnapshot.getString("ownership");

                            // Create ApiService instance
                            ApiService apiService = RetrofitClient.getRetrofitInstance().create(ApiService.class);

                            // Make API call
                            Call<SearchResult> call = apiService.searchImages("98a25bc45881cca0951538ea584d5399205e89e22bc4fc8b866ccfc3f5c76adc", "google_images", model);
                            call.enqueue(new Callback<SearchResult>() {
                                @Override
                                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                                    if (response.isSuccessful() && response.body() != null) {
                                        SearchResult searchResult = response.body();
                                        if (searchResult.getImageResults() != null && !searchResult.getImageResults().isEmpty()) {
                                            imageUrl = searchResult.getImageResults().get(0).getThumbnail();
//                                            Toast.makeText(Home.this.getContext(), "URL : "+imageUrl, Toast.LENGTH_SHORT).show();
                                            intent.putExtra("image_model",imageUrl);
                                            intent.putExtra("p", plateno);
                                            intent.putExtra("oname",oname);
                                            intent.putExtra("otype",otpye);
//                                            Toast.makeText(Home.this.getContext(), "URL : "+imageUrl, Toast.LENGTH_SHORT).show();
                                            startActivity(intent);
                                            search_vehicle.setVisibility(View.VISIBLE);
                                            progress.setVisibility(View.GONE);

                                        } else {
                                            Toast.makeText(Home.this.getContext(), "No image results found", Toast.LENGTH_SHORT).show();
                                            Log.d("Mohammed Saif ",searchResult.toString());
                                        }
                                    } else {
                                        Toast.makeText(Home.this.getContext(), "Failed to fetch images", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<SearchResult> call, Throwable t) {
                                    // Handle failure
                                    Toast.makeText(Home.this.getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Intent failure_intent=new Intent(Home.this.getContext(), fake_vehicle_activity.class);
                            startActivity(failure_intent);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Intent failure_intent=new Intent(Home.this.getContext(), fake_vehicle_activity.class);
                        startActivity(failure_intent);
                    }
                });

            }
        });

        //Open Gallery
        selectimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity().start(getContext(), Home.this);
            }
        });

        //Open Camera.java
        open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomePage homePage = new HomePage();
                homePage.finish();
                startActivity(new Intent(getContext(), Camera.class));
//                Animatoo.INSTANCE.animateCard(getContext());
//                Animatoo.INSTANCE.animateZoom(getContext());
//                Animatoo.INSTANCE.animateFade(getContext());
//                Animatoo.INSTANCE.animateWindmill(getContext());
//                Animatoo.INSTANCE.animateSpin(getContext());
//                Animatoo.INSTANCE.animateDiagonal(getContext());
//                Animatoo.INSTANCE.animateSplit(getContext());
//                Animatoo.INSTANCE.animateShrink(getContext());
//                Animatoo.INSTANCE.animateInAndOut(getContext());
//                Animatoo.INSTANCE.animateSwipeLeft(getContext());
//                Animatoo.INSTANCE.animateSwipeRight(getContext());
//                Animatoo.INSTANCE.animateSlideLeft(getContext());
//                Animatoo.INSTANCE.animateSlideRight(getContext());
//                Animatoo.INSTANCE.animateSlideUp(getContext());
                Animatoo.INSTANCE.animateSlideDown(getContext());
            }
        });

        //Open WebView
        search_challan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), E_Challan.class);
                startActivity(intent);
            }
        });

        cameraBridgeViewBase.setVisibility(SurfaceView.VISIBLE);
        cameraBridgeViewBase.setCvCameraViewListener(new CameraBridgeViewBase.CvCameraViewListener2() {
            @Override
            public void onCameraViewStarted(int width, int height) {

            }

            @Override
            public void onCameraViewStopped() {

            }

            @Override
            public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
                return inputFrame.rgba();
            }
        });

        if(OpenCVLoader.initDebug())
        {
            cameraBridgeViewBase.setCameraPermissionGranted();
            cameraBridgeViewBase.enableView();
        }


    }
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(cameraBridgeViewBase);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Intent intent = new Intent(Home.this.getContext(),Image_Process.class);
                intent.putExtra("Image",resultUri);
                startActivity(intent);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }



}