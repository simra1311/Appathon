package com.example.android.mykaarma.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mykaarma.ClassObjects.Dealer;
import com.example.android.mykaarma.R;
import com.example.android.mykaarma.fetchdata.fetch;
import com.example.android.mykaarma.translator.responsedata;
import com.example.android.mykaarma.translator.translate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

/**
 * This activity is for creating emails in different language
 *
 * @author Vinod Kumar
 * @version 1.0
 */
public class Email extends AppCompatActivity {

    private android.widget.TextView receiverEmail;
    private android.widget.EditText subject;
    private android.widget.EditText message;
    private android.widget.Button send;
    private String receiver_email = "";
    private String receiver_id = "";
    private String sender_email;
    private String sender_id;
    private ProgressDialog mProgress;

    private android.widget.ImageButton subjectvoiceinput;
    private android.widget.ImageButton messagevoiceinput;

    private StringBuilder subjectString, messageString;
    private android.widget.Spinner selectlanguage;

    private Locale srcLanguage = Locale.ENGLISH;
    private Locale dstLanguage = Locale.ENGLISH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        this.selectlanguage = (Spinner) findViewById(R.id.select_language);
        this.messagevoiceinput = (ImageButton) findViewById(R.id.message_voice_input);
        this.subjectvoiceinput = (ImageButton) findViewById(R.id.subject_voice_input);
        this.send = (Button) findViewById(R.id.send);
        this.message = (EditText) findViewById(R.id.message);
        this.subject = (EditText) findViewById(R.id.subject);
        this.receiverEmail = (TextView) findViewById(R.id.receiverEmail);

        mProgress = new ProgressDialog(Email.this);
        mProgress.setMessage("Please wait...");

//        receiver_id = getIntent().getStringExtra("receiver_id");
//        receiver_email = getIntent().getStringExtra("receiver_email");
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final Dealer dealer = (Dealer) Objects.requireNonNull(bundle).getSerializable("Dealer");
        receiver_id = dealer.ID;
        receiver_email = dealer.email;
        Log.d("email",receiver_email);

        subjectString = new StringBuilder("");
        messageString = new StringBuilder("");

        SharedPreferences sharedpreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

        sender_email = sharedpreferences.getString("email","");
        sender_id = sharedpreferences.getString("id","");
        receiverEmail.setText("To : " + receiver_email);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validityCheck()){

                    sendEmail();
                }else {
                    Toast.makeText(getApplicationContext(),"All Fields Required",Toast.LENGTH_SHORT).show();
                }
            }
        });

        subjectvoiceinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSubjectSpeechInput();
            }
        });

        messagevoiceinput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getMessageSpeechInput();
            }
        });


        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                messageString.setLength(0);
                messageString.append(s.toString()+" ");
            }
        });


        subject.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                subjectString.setLength(0);
                subjectString.append(s.toString()+" ");

            }
        });


        final String[] languages = {"Choose","German", "French", "Italian", "Japanese", "Korean"};

        final ArrayAdapter<String> languageAdapter = new ArrayAdapter<>(this, R.layout.languages, languages);
        selectlanguage.setAdapter(languageAdapter);

        selectlanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dstLanguage = getLocale(languages[position]);
                translateTo();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    /**
     * Check for all input field validity
     * @return true if all fields are valid else false
     */
    boolean validityCheck(){

        boolean isValid = true;

        if (subject.getText().toString().isEmpty()){

            isValid = false;
        }else if(message.getText().toString().isEmpty()){

            isValid = false;
        }

        return isValid;
    }

    /**
     * Create Speech recognition intent for voice input in SUBJECT field of mail
     */
    void getSubjectSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(intent, 100);
        } else {
            Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Create Speech recognition intent for voice input in MESSAGE field of mail
     */
    void getMessageSpeechInput() {

        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        if (intent.resolveActivity(getPackageManager()) != null) {

            startActivityForResult(intent, 101);
        } else {
            Toast.makeText(getApplicationContext(), "Your Device Don't Support Speech Input", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 100: {
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    subjectString.append(result.get(0) + " ");
                    subject.setText(subjectString);
                    int length = subject.getText().toString().length();
                    subject.setSelection(length);
                }
                break;
            }

            case 101: {
                if (resultCode == RESULT_OK && data != null) {

                    ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                    messageString.append(result.get(0) + " ");
                    message.setText(messageString);
                    int length = message.getText().toString().length();
                    message.setSelection(length);

                }
                break;
            }
        }
    }

    /**
     * This fuction maps the input language selection by user in UI to Locale.language
     *
     * @param language language selected by user
     * @return Locale class integer id
     */
    public Locale getLocale(String language) {
        Locale local = Locale.ENGLISH;
        if (language.equals("French"))
            local = Locale.FRENCH;
        else if (language.equals("German"))
            local = Locale.GERMAN;
        else if (language.equals("Italian"))
            local = Locale.ITALIAN;
        else if (language.equals("Japanese"))
            local = Locale.JAPANESE;
        else if (language.equals("Korean"))
            local = Locale.KOREAN;
        return local;
    }

    /**
     * Translates the input String in subject and message field from
     * source language to destination language
     * and reset them
     */
    void translateTo(){
        String msg = message.getText().toString();

        final String[] words = msg.split("\\s+");

        message.setText("");
        messageString.setLength(0);

        for (int i = 0; i < words.length; i++) {
            new translate(
                    Email.this,
                    words[i].replaceAll("[^\\w]", ""),
                    srcLanguage,dstLanguage,
                    i
            )
                    .starttranslation(new responsedata() {
                        @Override
                        public void response(String word, int position) {
                            if(position != words.length-1){
                                messageString.append(word);
                            }else {
                                messageString.append(word);
                                message.setText(messageString);
                                int length = message.getText().toString().length();
                                message.setSelection(length);
                            }
                        }
                    });
        }

        String submsg = subject.getText().toString();

        final String[] subwords = submsg.split("\\s+");

        subject.setText("");
        subjectString.setLength(0);


        for (int i = 0; i < subwords.length; i++) {
             new translate(
                            Email.this,
                            subwords[i].replaceAll("[^\\w]", ""),
                            srcLanguage,dstLanguage,
                            i
                        )
                        .starttranslation(new responsedata() {
                            @Override
                            public void response(String word, int position) {
                                if(position != subwords.length-1){
                                    subjectString.append(word);
                                }else {
                                    subjectString.append(word);
                                    subject.setText(subjectString);
                                    int length = subject.getText().toString().length();
                                    subject.setSelection(length);
                                }
                            }
                        });
        }


    }


    /**
     * Sends mail through the server to receiver
     *
     * Creates an Http request for sending mails
     */

    void sendEmail(){
        mProgress.show();

        String url = getResources().getString(R.string.domain)+"message.php";

        Map<String,String> params = new HashMap<>();

        params.put("sender_id", sender_id);
        params.put("sender_email", sender_email);
        params.put("receiver_id", receiver_id);
        params.put("receiver_email", receiver_email);
        params.put("subject", subject.getText().toString());
        params.put("message",message.getText().toString());

        new fetch(url,params,getApplicationContext()).startfetch(new com.example.android.mykaarma.fetchdata.responsedata() {
            @Override
            public void response(JSONObject data) {
                mProgress.dismiss();
                try {
                    Log.d("data",data.toString());
                    if(data.getInt("conn_status") == 1 && data.getInt("status") == 1 && data.getInt("data") == 1){
                        Toast.makeText(getApplicationContext(),"Email Sent...", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),"Failed...", Toast.LENGTH_SHORT).show();
                    }
                        Log.d("Message Response", data.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Failed...", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Toast.makeText(Email.this,"Sent successfully",Toast.LENGTH_SHORT).show();
    }

}




