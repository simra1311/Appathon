package com.example.android.mykaarma.fetchdata;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * This class is used to fetch data from the server using volley by creating String request
 * and returning JsonObject response
 *
 * @author Vinod Kumar
 * @version 1.0
 *
 */
public class fetch {

    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    private final String url;
    private final Map<String,String> params;
    private final Context context;

    public fetch(String url,Map<String,String> params,Context context) {
        this.url=url;
        this.params=params;
        this.context=context;


    }

    /**
     * This function is called for starting the fetch process and initialise response
     * interface with response (JSON) data
     *
     * @param resp
     */
    public void startfetch(final responsedata resp){


        RequestQueue requestQueue= Volley.newRequestQueue(context);

        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("Response111",response);

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

                Log.d("Response1121",error.toString());
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
            protected Map<String, String> getParams() {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(request);
    }

}

