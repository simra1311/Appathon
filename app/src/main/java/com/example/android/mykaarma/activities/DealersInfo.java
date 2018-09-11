package com.example.android.mykaarma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.android.mykaarma.ClassObjects.Dealer;
import com.example.android.mykaarma.R;

import java.util.Objects;

/**
 * This activity is for displaying details of the dealer
 * @author Simra Afreen
 * @version 1.0.0
 */
public class DealersInfo extends AppCompatActivity {

    private TextView ID,add;
    private com.github.clans.fab.FloatingActionButton email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealers_info);

        Intent intent = getIntent();
        ID = findViewById(R.id.name);
        add = findViewById(R.id.address);
        email = findViewById(R.id.email);

//        getSupportActionBar().hide();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        Bundle bundle = intent.getExtras();
        final Dealer dealer = (Dealer) Objects.requireNonNull(bundle).getSerializable("Dealer");
        ID.setText(dealer.ID+"");
        add.setText(dealer.address);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
