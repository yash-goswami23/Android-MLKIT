package com.example.android_mlkit;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.label.ImageLabel;
import com.google.mlkit.vision.label.ImageLabeler;
import com.google.mlkit.vision.label.ImageLabeling;
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions;

import java.io.IOException;
import java.util.List;

public class imgLabeling extends AppCompatActivity {
    TextView actTextView,resultTV;
    ImageView MyImg;
    Button btnScanImg;
    ActivityResultLauncher<Intent> galleryLauncher,cameraLauncher;
    ImageLabeler imageLabeler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_labeling);
        actTextView = findViewById(R.id.actTextView);
        actTextView.setText(getIntent().getStringExtra("text"));
        resultTV = findViewById(R.id.resultTV);
        MyImg = findViewById(R.id.imageView);
        btnScanImg = findViewById(R.id.btnscanImg);

//        imageLabeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS);
         ImageLabelerOptions imageLabelerOptions =
     new ImageLabelerOptions.Builder()
         .setConfidenceThreshold(0.7f)
         .build();
             imageLabeler = ImageLabeling.getClient(imageLabelerOptions);

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Intent data = o.getData();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                InputImage inputImage = InputImage.fromBitmap(photo,0);
                MyImg.setImageBitmap(photo);
                processImage(inputImage);

            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Intent data = o.getData();
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                ivQrCode.setImageBitmap(photo);
                try {
                    InputImage inputImage = InputImage.fromFilePath(imgLabeling.this,data.getData());
                    MyImg.setImageBitmap(inputImage.getBitmapInternal());
                    processImage(inputImage);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });


        btnScanImg.setOnClickListener(V->{
            String [] options = {"camera","gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(imgLabeling.this);
            builder.setTitle("Pick A Option");
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which == 0){
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        cameraLauncher.launch(cameraIntent);
                    }else{
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        galleryLauncher.launch(i);
                    }
                }
            });
            builder.show();
        });


    }

    private void processImage(InputImage inputImage) {
        imageLabeler.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<ImageLabel>>() {
            @Override
            public void onSuccess(List<ImageLabel> imageLabels) {
                String result = "";
                float confidence = 0;
                int index = 0;
                for(ImageLabel img : imageLabels){
                    result = result+"\n"+img.getText();
                    confidence = img.getConfidence();
                    index = img.getIndex();
                }
                resultTV.setText(result);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                resultTV.setText("Error : "+e.getMessage());
            }
        });
    }
}