package com.example.android.mykaarma.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.android.mykaarma.R;
import com.example.android.mykaarma.adapters.mailAdapter;
import com.example.android.mykaarma.fetchdata.fetch;
import com.example.android.mykaarma.fetchdata.responsedata;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * This activity class get current user emails and displays them in list ( Recycler View )
 *
 * @author Vinod Kumar
 * @version 1.0
 */
public class MyMails extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_mails);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        SharedPreferences sharedpreferences = getSharedPreferences("userdata", Context.MODE_PRIVATE);

        final String user_id = sharedpreferences.getString("id","");

        String url = getResources().getString(R.string.domain) + "mails.php";

        Map<String,String> params = new HashMap<>();
        params.put("user_id",user_id);

        new fetch(url,params,getApplicationContext()).startfetch(new responsedata() {
            @Override
            public void response(JSONObject data) {

                try {
                    if(data.getInt("conn_status") == 1 ){

                        // specify an adapter (see also next example)
                        mAdapter = new mailAdapter(data.getJSONArray("mails"), user_id);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                Log.d("Response data",data.toString());
            }
        });

    }
}
