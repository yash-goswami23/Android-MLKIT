package com.example.android_mlkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

public class Translate_Text extends AppCompatActivity {
    TextView actTextView,TransResult;
    Spinner spinner1,spinner2;
    EditText textInput1;
    Button TransBtn;
    String languageCode,fromLanguageCode,toLanguageCode;

    String[] FromLanguages = {"English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Hindi","Urdu"};
    String[] ToLanguages = {"English","Afrikaans","Arabic","Belarusian","Bengali","Catalan","Hindi","Urdu"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_translate_text);
        init();
        SetSpinner();

        TransBtn.setOnClickListener(V->{
            TransResult.setText("");
            if(textInput1.getText().toString().isEmpty()){
                Toast.makeText(this, "Please enter your text", Toast.LENGTH_SHORT).show();
            }else if(fromLanguageCode.isEmpty()){
                Toast.makeText(this, "Please Select Source Language", Toast.LENGTH_SHORT).show();
            }else if(toLanguageCode.isEmpty()){
                Toast.makeText(this, "Please Select Target Language", Toast.LENGTH_SHORT).show();
            }else{
                TranslatText(fromLanguageCode,toLanguageCode,textInput1.getText().toString());
            }
        });

    }

    private void TranslatText(String fromLanguageCode, String toLanguageCode, String src) {
        TransResult.setText("Downloading Language");
        TranslatorOptions options = new TranslatorOptions.Builder().setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        Translator translator = Translation.getClient(options);

        DownloadConditions conditions = new DownloadConditions.Builder().build();

        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                TransResult.setText("Translating");
                translator.translate(src).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        TransResult.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Translate_Text.this, "Fail to Translate", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Translate_Text.this, "Fail to Downloading Language", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void SetSpinner(){
        //spinner 1
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fromLanguageCode = getLanguageCode(FromLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter fromAdapter = new ArrayAdapter(this,R.layout.spinner_item,FromLanguages);
        fromAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner1.setAdapter(fromAdapter);
        //spinner 2
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                toLanguageCode = getLanguageCode(ToLanguages[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter toAdapter = new ArrayAdapter(this,R.layout.spinner_item,ToLanguages);
        toAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner2.setAdapter(toAdapter);
    }

    private String getLanguageCode(String lang) {
        String langCode;
        switch (lang){
            case "English" :
                langCode = TranslateLanguage.ENGLISH;
                break;
            case "Afrikaans" :
                langCode = TranslateLanguage.AFRIKAANS;
                break;
            case "Arabic" :
                langCode = TranslateLanguage.ARABIC;
                break;
            case "Belarusian" :
                langCode = TranslateLanguage.BELARUSIAN;
                break;
            case "Bengali" :
                langCode = TranslateLanguage.BENGALI;
                break;
            case "Catalan" :
                langCode = TranslateLanguage.CATALAN;
                break;
            case "Hindi" :
                langCode = TranslateLanguage.HINDI;
                break;
            case "Urdu" :
                langCode = TranslateLanguage.URDU;
                break;
            default:
                langCode = "";
        }
        return langCode;
    }

    private void init(){
        actTextView = findViewById(R.id.actTextView);
        TransResult = findViewById(R.id.TransResult);
        textInput1 = findViewById(R.id.textInput1);
        TransBtn = findViewById(R.id.TransBtn);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);

        actTextView.setText(getIntent().getStringExtra("text"));
    }
}