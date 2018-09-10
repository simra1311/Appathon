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
import android.widget.Toast;

import com.example.android.mykaarma.R;
import com.example.android.mykaarma.fetchdata.fetch;
import com.example.android.mykaarma.fetchdata.responsedata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This is a Signup Activity class creted for new user sign in
 *
 * @author Vinod Kumar
 * @version 1.0
 */
public class Signup extends AppCompatActivity {

    private android.support.design.widget.TextInputEditText firstname;
    private android.support.design.widget.TextInputEditText lastname;
    private android.support.design.widget.TextInputEditText email;
    private android.support.design.widget.TextInputEditText phone;
    private android.support.design.widget.TextInputEditText password;
    private android.support.design.widget.TextInputEditText confirmpassword;
    private android.widget.Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.signup = (Button) findViewById(R.id.signup);
        this.confirmpassword = (TextInputEditText) findViewById(R.id.confirmpassword);
        this.password = (TextInputEditText) findViewById(R.id.password);
        this.phone = (TextInputEditText) findViewById(R.id.phone);
        this.email = (TextInputEditText) findViewById(R.id.email);
        this.lastname = (TextInputEditText) findViewById(R.id.lastname);
        this.firstname = (TextInputEditText) findViewById(R.id.firstname);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(valid()){

                    signup();
                }else {
                    Toast.makeText(getApplicationContext(),"All field required",Toast.LENGTH_SHORT);
                }
            }
        });
    }


    /**
     * Call the server for adding new user information
     */
    void signup(){

        String url = getResources().getString(R.string.domain)+"signup.php";
        Map<String,String> params = new HashMap<>();

        params.put("fname",firstname.getText().toString());
        params.put("lname",lastname.getText().toString());
        params.put("email",email.getText().toString());
        params.put("phone",phone.getText().toString());
        params.put("pass",password.getText().toString());

        new fetch(url, params, getApplicationContext()).startfetch(new responsedata() {
            @Override
            public void response(JSONObject data) {
                Log.d("Responsedata191",data.toString());

                try {
                    if(data.getInt("conn_status") == 1 && data.getInt("status") == 1 ){

                        Toast.makeText(getApplicationContext(),"Signed Up Successfully", Toast.LENGTH_SHORT).show();

                        JSONObject userdata = data.getJSONObject("user");

                        SharedPreferences sharedpreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.clear();
                        editor.putBoolean("loggedin", true);
                        editor.putString("id", userdata.getString("PersonID"));
                        editor.putString("firstname",userdata.getString("FirstName"));
                        editor.putString("lastname", userdata.getString("LastName"));
                        editor.putString("email", userdata.getString("Email"));
                        editor.putString("phone", userdata.getString("Phone"));
                        editor.commit();


                    }else if(data.getInt("conn_status") == 1 && data.getInt("status") == 0){

                        Toast.makeText(getApplicationContext(),"Account Exists , Please Login",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),Login.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(),"Failed",Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Checks for the validity of all input fields before adding new user
     *
     * @return true if all input fields are valid else false
     */
    boolean valid(){

        boolean isValid = true;

        if(firstname.getText().toString().isEmpty()){

            isValid = false;
            firstname.setError("Required");
        }

        else if(lastname.getText().toString().isEmpty()){

            isValid = false;
            lastname.setError("Required");
        }


        else if(email.getText().toString().isEmpty() || !emailvalidate(email.getText().toString())){

            if (email.getText().toString().isEmpty()){

                isValid = false;
                email.setError("Required");
            }else if(!emailvalidate(email.getText().toString())) {

                isValid = false;
                email.setError("Invalid email");
            }
        }

        else if(phone.getText().toString().isEmpty() || phone.getText().toString().length() != 10){

            if(phone.getText().toString().isEmpty()){

                isValid = false;
                phone.setError("Required");
            }
            else {

                isValid = false;
                phone.setError("Invalid Number");
            }
        }

        else if(password.getText().toString().isEmpty() || confirmpassword.getText().toString().isEmpty() || !confirmpassword.getText().toString().contentEquals(password.getText().toString())){

            if (password.getText().toString().isEmpty()){

                isValid = false;
                password.setError("Required");
            }else if (confirmpassword.getText().toString().isEmpty()){

                isValid = false;
                confirmpassword.setError("Required");

            }else {

                isValid = false;
                confirmpassword.setError("Password did not match");

            }
        }

        return  isValid;
    }



    public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    /**
     * Checks for email pattern with regular expression
     * @param emailStr
     * @return
     */
    public static boolean emailvalidate(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX .matcher(emailStr);
        return matcher.find();
    }

}
