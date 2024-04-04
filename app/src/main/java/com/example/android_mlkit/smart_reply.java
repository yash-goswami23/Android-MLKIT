package com.example.android_mlkit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.smartreply.SmartReply;
import com.google.mlkit.nl.smartreply.SmartReplyGenerator;
import com.google.mlkit.nl.smartreply.SmartReplySuggestion;
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult;
import com.google.mlkit.nl.smartreply.TextMessage;

import java.util.ArrayList;
import java.util.List;

public class smart_reply extends AppCompatActivity {
    TextView actTextView,OututMessage;
    EditText inputMessage;
    Button sendBtn;
    String userUID = "123456";
    List<TextMessage> conversation;
    SmartReplyGenerator smartReplyGenerator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smart_reply);
        actTextView = findViewById(R.id.actTextView);
        actTextView.setText(getIntent().getStringExtra("text"));
        OututMessage = findViewById(R.id.OututMessage);
        inputMessage = findViewById(R.id.inputMessage);
        sendBtn = findViewById(R.id.sendBtn);

        conversation = new ArrayList<>();

        smartReplyGenerator = SmartReply.getClient();

        sendBtn.setOnClickListener(V->{
            String message = inputMessage.getText().toString().trim();
            OututMessage.setText("Wait For Message");
            conversation.add(TextMessage.createForRemoteUser(message,System.currentTimeMillis(),userUID));

            smartReplyGenerator.suggestReplies(conversation).addOnSuccessListener(new OnSuccessListener<SmartReplySuggestionResult>() {
                @Override
                public void onSuccess(SmartReplySuggestionResult smartReplySuggestionResult) {
                    if(smartReplySuggestionResult.getStatus() == smartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE){
                        OututMessage.setText("No Reply");
                    } else if (smartReplySuggestionResult.getStatus() == smartReplySuggestionResult.STATUS_SUCCESS) {
                        String reply = "";
                        int i = 0;
                        for(SmartReplySuggestion suggestion : smartReplySuggestionResult.getSuggestions()){
                            i++;
                            reply = reply + suggestion.getText()+"\n";
                        }
                        OututMessage.setText(reply);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    OututMessage.setText("Error : "+e.getMessage());
                }
            });
        });

    }
}