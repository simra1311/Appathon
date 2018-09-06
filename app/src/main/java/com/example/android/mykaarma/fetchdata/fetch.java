package com.example.android.mykaarma.fetchdata;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class fetch {

    private  String url;
    private Map<String,String> params;
    private Context context;

    public fetch(String url,Map<String,String> params,Context context) {
        this.url=url;
        this.params=params;
        this.context=context;


    }

    public void startfetch(final responsedata resp){


        RequestQueue requestQueue= Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Log.d("Response111",response);

                try {
                    JSONObject data = new JSONObject(response);
                    data.put("data",1);
                    data.put("value","Volley Success");
                    resp.response(data);
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

//                Log.d("Response1121",error.toString());
                try {
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("data",0);
                    jsonObject.put("value","Volley Error");
                    resp.response(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        requestQueue.add(request);
    }

}

