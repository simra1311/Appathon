package com.example.android.mykaarma.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.android.mykaarma.ClassObjects.Dealer;
import com.example.android.mykaarma.R;

import java.util.Objects;

/**
 * This activity is for displaying details of the dealer
 * @author Simra Afreen
 * @version 1.0.0
 */
public class DealersInfo extends AppCompatActivity {

    private TextView ID,add,mailid,dealeruserid;
    private com.github.clans.fab.FloatingActionButton email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dealers_info);

        final Intent intent = getIntent();
        ID = findViewById(R.id.name);
        add = findViewById(R.id.address);
        email = findViewById(R.id.email);
        mailid = findViewById(R.id.emailadd);

        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        setActionBar(toolbar);

        Bundle bundle = intent.getExtras();
        final Dealer dealer = (Dealer) Objects.requireNonNull(bundle).getSerializable("Dealer");
        ID.setText(dealer.ID+"");
        add.setText(dealer.address);
        mailid.setText(dealer.email);

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent1 = new Intent(DealersInfo.this,Email.class);
                Bundle bundle1 = new Bundle();
                bundle1.putSerializable("Dealer", dealer);
                intent1.putExtras(bundle1);
                startActivity(intent1);
                finish();
            }
        });
    }
}
