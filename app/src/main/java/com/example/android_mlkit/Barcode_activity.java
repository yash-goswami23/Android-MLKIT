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
import android.graphics.Point;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.IOException;
import java.util.List;

public class Barcode_activity extends AppCompatActivity {
    TextView actTextView,resultTV;
    ImageView ivQrCode;
    Button btnScanBarcode;
    ActivityResultLauncher<Intent> galleryLauncher,cameraLauncher;

    BarcodeScanner barcodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        actTextView = findViewById(R.id.actTextView);
        actTextView.setText(getIntent().getStringExtra("text"));
        resultTV = findViewById(R.id.resultTV);
        ivQrCode = findViewById(R.id.imageView);
        btnScanBarcode = findViewById(R.id.BtnScanBarcode);

        barcodeScanner = BarcodeScanning.getClient();

        cameraLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Intent data = o.getData();
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    InputImage inputImage = InputImage.fromBitmap(photo,0);
                    ivQrCode.setImageBitmap(photo);
                    processImageQR(inputImage);

            }
        });

        galleryLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o) {
                Intent data = o.getData();
//                Bitmap photo = (Bitmap) data.getExtras().get("data");
//                ivQrCode.setImageBitmap(photo);
                try {
                    InputImage inputImage = InputImage.fromFilePath(Barcode_activity.this,data.getData());
                    ivQrCode.setImageBitmap(inputImage.getBitmapInternal());
                    processImageQR(inputImage);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        });


        btnScanBarcode.setOnClickListener(V->{
            String [] options = {"camera","gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Barcode_activity.this);
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

    private void processImageQR( InputImage inputImage) {

            Task<List<Barcode>> result = barcodeScanner.process(inputImage)
                    .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            if(barcodes.isEmpty()){
                                resultTV.setText("MayBe is Not Barcode Try Again");
                            }else {
                                for (Barcode barcode : barcodes) {
                                    int valueType = barcode.getValueType();
                                    // See API reference for complete list of supported types
                                    switch (valueType) {
                                        case Barcode.TYPE_WIFI:
                                            String ssid = barcode.getWifi().getSsid();
                                            String password = barcode.getWifi().getPassword();
                                            int type = barcode.getWifi().getEncryptionType();
                                            if (ssid != null) {
                                                resultTV.setText("SSID " + ssid + "\n" +
                                                        "Password " + password + "\n" +
                                                        "Type" + type);
                                            } else {
                                                Toast.makeText(Barcode_activity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case Barcode.TYPE_URL:
                                            String title = barcode.getUrl().getTitle();
                                            String url = barcode.getUrl().getUrl();
                                            if (title != null) {
                                                resultTV.setText("Title " + title + "\n" +
                                                        "url" + url);
                                            } else {
                                                Toast.makeText(Barcode_activity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        case Barcode.TYPE_EMAIL:
                                            String emailAD = barcode.getEmail().getAddress();
                                            String emailSUB = barcode.getEmail().getSubject();
                                            String emailBODY = barcode.getEmail().getBody();
                                            if (emailAD != null) {
                                                resultTV.setText("Email Address" + emailAD + "\n" +
                                                        "Email Subject" + emailSUB + "\n" +
                                                        "Email Body" + emailBODY);
                                            } else {
                                                Toast.makeText(Barcode_activity.this, "Try Again", Toast.LENGTH_SHORT).show();
                                            }
                                            break;
                                        default:
                                            String data = barcode.getDisplayValue();
                                            resultTV.setText("Try agan meyde this is not barcode " + data);
                                            break;
                                    }
                                }
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            resultTV.setText("Try Again");
                        }
                    });
        }
}