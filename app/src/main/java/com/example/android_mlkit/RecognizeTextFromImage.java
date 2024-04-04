package com.example.android_mlkit;

import static android.app.ProgressDialog.show;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;

public class RecognizeTextFromImage extends AppCompatActivity {
    TextView actTextView,resultTV;
    Button ChosePictureBtn;


    InputImage inputImage;
    TextRecognizer textRecognizer;
    ActivityResultLauncher<Intent> galleryLauncher,cameraLauncher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognize_text_from_image);
        actTextView = findViewById(R.id.actTextView);
        resultTV = findViewById(R.id.resultTV);
        ChosePictureBtn = findViewById(R.id.ChosePictureBtn);
        actTextView.setText(getIntent().getStringExtra("text"));

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);


        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Intent data = o.getData();
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                InputImage inputImage = InputImage.fromBitmap(photo,0);
                convertImageToText(inputImage);

            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Intent data = o.getData();
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                ivQrCode.setImageBitmap(photo);
                try {
                    InputImage inputImage = InputImage.fromFilePath(RecognizeTextFromImage.this,data.getData());
                    convertImageToText(inputImage);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });

        ChosePictureBtn.setOnClickListener(V->{
            String [] options = {"camera","gallery"};
            AlertDialog.Builder builder = new AlertDialog.Builder(RecognizeTextFromImage.this);
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

    private void convertImageToText(InputImage inputImage) {
        try{
            Task<Text> result = textRecognizer.process(inputImage).addOnSuccessListener(new OnSuccessListener<Text>() {
                @Override
                public void onSuccess(Text text) {
                    if (!text.getText().isEmpty())
                    resultTV.setText(text.getText());
                    else resultTV.setText("No Text");
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    resultTV.setText("Error : "+e.getMessage());
                }
            });
        }catch (Exception e){
            Toast.makeText(this, "Error : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}