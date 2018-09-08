package com.example.android.mykaarma.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.HttpResponse;
import com.example.android.mykaarma.R;
import com.example.android.mykaarma.fetchdata.fetch;
import com.example.android.mykaarma.fetchdata.responsedata;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Email extends AppCompatActivity {

    private android.widget.TextView receiverEmail;
    private android.widget.EditText subject;
    private android.widget.EditText message;
    private android.widget.Button send;
    private String receiver_email = "vk1997vinodkumar@gmail.com";
    private String receiver_id = "11001";
    private String sender_email;
    private String sender_id;

    private Locale srcLanguage = Locale.ENGLISH;
    private Locale dstLanguage = Locale.ENGLISH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        this.send = (Button) findViewById(R.id.send);
        this.message = (EditText) findViewById(R.id.message);
        this.subject = (EditText) findViewById(R.id.subject);
        this.receiverEmail = (TextView) findViewById(R.id.receiverEmail);

        SharedPreferences sharedpreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

        sender_email = sharedpreferences.getString("email","");
        sender_id = sharedpreferences.getString("id","");
        receiverEmail.setText("To : "+sender_email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validityCheck()){

//                    sendEmail();
                }else {
                    Toast.makeText(getApplicationContext(),"All Fields Required",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    boolean validityCheck(){

        boolean isValid = true;

        if (subject.getText().toString().isEmpty()){

            isValid = false;
        }else if(message.getText().toString().isEmpty()){

            isValid = false;
        }

        return isValid;
    }


}




