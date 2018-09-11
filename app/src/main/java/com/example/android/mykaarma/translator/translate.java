package com.example.android.mykaarma.translator;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.Locale;

/**
 * This class translates the input string from one language to other
 *
 * @author Vinod Kumar
 * @version 1.0
 */

public class translate {

    private Context context;
    private String query,langpair, url, word;
    private int position;

    /**
     * Params required for translation
     *
     * @param context is the activity context
     * @param word is the string to be translated
     * @param srcLanguage is the source language or initial language
     * @param dstLanguage is the destination or final language
     * @param position is the position of string in string array , used to create string again with each word placed in proper order
     */
    public translate(Context context, String word, Locale srcLanguage, Locale dstLanguage, int position) {
        this.context = context;
        this.word = word;
//        Log.d("TranslationWord ",word);
        try {
            this.query = URLEncoder.encode(word, "UTF-8");
            this.langpair = URLEncoder.encode(srcLanguage.getLanguage() + "|" + dstLanguage.getLanguage(), "UTF-8");
            this.url = "http://mymemory.translated.net/api/get?q=" + query + "&langpair=" + langpair;
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.position = position;
    }


    /**
     * Begins the translation and initialise responsedata interface with translated response
     * @param resp interface to be initialised with translated word
     */
    public void starttranslation(final responsedata resp) {

//        Log.d("Language Url : ",url);
        try {


            RequestQueue requestQueue = Volley.newRequestQueue(context);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONObject translation = response.getJSONObject("responseData");


                        if (response.getInt("responseStatus") == 200) {
//                            Log.d("Translation Respose",translation.getString("translatedText"));

                            resp.response(translation.getString("translatedText") + " ", position);
                        } else {

                            resp.response(word + " ", position);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(context, "Translation Failed", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(request);
        }catch (Exception e){

        }
    }
}
