package com.example.android_mlkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    ImageView DetctLangBtn,TranTextBtn,smartReplyBtn,recgTIbtn,QRscanBtn,ImageLabaBtn;
    TextView textLD,textTran,textSR,textRT,textQR,textIM;
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;

    public static final String [] CamraPermissions = {Manifest.permission.CAMERA};
    public static final String [] StoragePermissions = {Manifest.permission.READ_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DetctLangBtn = findViewById(R.id.LangDetectBtn);
        TranTextBtn = findViewById(R.id.TranTextBtn);
        smartReplyBtn = findViewById(R.id.smartReplyBtn);
        recgTIbtn = findViewById(R.id.recgTIbtn);
        QRscanBtn = findViewById(R.id.QRscanBtn);
        ImageLabaBtn = findViewById(R.id.ImageLabaBtn);
        textIM = findViewById(R.id.textIM);
        textLD = findViewById(R.id.textLD);
        textTran = findViewById(R.id.textTran);
        textSR = findViewById(R.id.textSR);
        textRT = findViewById(R.id.textRT);
        textQR = findViewById(R.id.textQR);


        ActivityCompat.requestPermissions(MainActivity.this,CamraPermissions, CAMERA_PERMISSION_CODE);
        ActivityCompat.requestPermissions(MainActivity.this,StoragePermissions, STORAGE_PERMISSION_CODE);


        DetctLangBtn.setOnClickListener(V->{
            String btnText = textLD.getText().toString();
            changeAC(getApplicationContext(),LanguageDetct.class,btnText);
        });
        TranTextBtn.setOnClickListener(V->{
            String btnText = textTran.getText().toString();
            changeAC(getApplicationContext(),Translate_Text.class,btnText);
        });
        smartReplyBtn.setOnClickListener(V->{
            String btnText = textSR.getText().toString();
            changeAC(getApplicationContext(), smart_reply.class,btnText);
        });
        recgTIbtn.setOnClickListener(V->{
            String btnText = textRT.getText().toString();
            changeAC(getApplicationContext(), RecognizeTextFromImage.class,btnText);
        });
        QRscanBtn.setOnClickListener(V->{
            String btnText = textQR.getText().toString();
            changeAC(getApplicationContext(),Barcode_activity.class,btnText);
        });
        ImageLabaBtn.setOnClickListener(V->{
            String btnText = textIM.getText().toString();
            changeAC(getApplicationContext(),imgLabeling.class,btnText);
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT) .show();
            }
        }
        else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void changeAC(Context applicationContext, Class languageDetctClass, String btnText) {
        Intent i = new Intent(getApplicationContext(), languageDetctClass);
        i.putExtra("text",btnText);
        startActivity(i);
    }
}