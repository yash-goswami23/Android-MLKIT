package com.example.android_mlkit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.languageid.LanguageIdentification;
import com.google.mlkit.nl.languageid.LanguageIdentificationOptions;
import com.google.mlkit.nl.languageid.LanguageIdentifier;

public class LanguageDetct extends AppCompatActivity {
    TextView actTextView,ResultOutput;
    EditText textInput;
    Button ChickBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_detct);
        actTextView = findViewById(R.id.actTextView);
        ResultOutput = findViewById(R.id.ResultOutput);
        textInput = findViewById(R.id.textInput);
        ChickBtn = findViewById(R.id.ChickBtn);
        actTextView.setText(getIntent().getStringExtra("text"));

        ChickBtn.setOnClickListener(V->{
            if(textInput.getText().toString().isEmpty()){
                Toast.makeText(this, "Please Enter The Text", Toast.LENGTH_SHORT).show();
            }else{
                chickText(textInput.getText().toString());
            }
        });
    }

    private void chickText(String text) {
        LanguageIdentifier languageIdentifier = LanguageIdentification.getClient(
                new LanguageIdentificationOptions.Builder()
                        .setConfidenceThreshold(0.5f)
                        .build()
        );
        languageIdentifier.identifyLanguage(text)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(@Nullable String languageCode) {
                                if (languageCode.equals("und")) {
                                    ResultOutput.setText("Can't Identify Language");
                                } else {
                                 ResultOutput.setText("Language is : " + languageCode);
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                ResultOutput.setText("Exception " + e.getMessage());
                            }
                        });
    }
}