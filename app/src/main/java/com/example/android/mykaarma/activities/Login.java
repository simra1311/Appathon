package com.example.android.mykaarma.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.mykaarma.R;
import com.example.android.mykaarma.fetchdata.fetch;
import com.example.android.mykaarma.fetchdata.responsedata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This class authenticate user to access the maps by authenticating user
 *
 * @author Vinod Kumar
 * @version 1.0
 */
public class Login extends AppCompatActivity {


    private android.support.design.widget.TextInputEditText contact;
    private android.support.design.widget.TextInputEditText password;
    private Button submit;
    private TextView signup;
    private SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.signup = findViewById(R.id.signup);
        this.submit = findViewById(R.id.submit);
        this.password = findViewById(R.id.password);
        this.contact = findViewById(R.id.contact);


//     Continue to map if user already logged in
        sharedpreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

        if (sharedpreferences.getBoolean("loggedin", false)){

            startActivity(new Intent(getApplicationContext(),MapsActivity.class));
            finish();
        }

//        Launching For New User
        this.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(),Signup.class));
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (loginFormValidation()) {
                    proceedlogin();
                } else {
                    Toast.makeText(getApplicationContext(), "All entries required.", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Checks valid entries in login form
     * @return true if all fields are valid else false
     */
    boolean loginFormValidation(){

         boolean isValid = true;

        if(contact.getText().toString().length() < 10 || contact.getText().toString().isEmpty()){

            if (contact.getText().toString().isEmpty()) {

                contact.setError("Required");
                isValid= false;

            } else {

                contact.setError("Invalid Contact");
                isValid= false;
            }
        }else if (password.getText().toString().isEmpty()){
            password.setError("Required");
            isValid= false;
        }
        return isValid;
    }

    /**
     * Saves user data if login successful and set login true for login in future
     * Move to maps after login completion
     */
    void proceedlogin(){

        String url = getResources().getString(R.string.domain)+"authentication.php";
        Map<String,String> params = new HashMap<>();
        params.put("phone", contact.getText().toString());
        params.put("pass", password.getText().toString());

        new fetch(url,params,getApplicationContext()).startfetch(new responsedata() {
            @Override
            public void response(JSONObject data) {
                Log.d("Response191",data.toString());

                try {
                    if(data.getInt("status") == 1 && data.getInt("data") == 1){

                        Toast.makeText(getApplicationContext(),"Logged In Successfully", Toast.LENGTH_SHORT).show();

                        JSONObject userdata = data.getJSONObject("user");



                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();

                        editor.putBoolean("loggedin", true);
                        editor.putString("id", userdata.getString("PersonID"));
                        editor.putString("firstname",userdata.getString("FirstName"));
                        editor.putString("lastname", userdata.getString("LastName"));
                        editor.putString("email", userdata.getString("Email"));
                        editor.putString("phone", userdata.getString("Phone"));
                        editor.commit();

                        startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                    }else {
                        Toast.makeText(getApplicationContext(),"Login Failed", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),"Unable to process at this time.", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(),"Please check your Internet Connection and Retry.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
