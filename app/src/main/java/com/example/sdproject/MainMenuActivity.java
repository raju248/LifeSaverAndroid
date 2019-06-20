package com.example.sdproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        linearLayout = findViewById(R.id.facts);
        linearLayout.setOnClickListener(this);


        ImageView imageView =  findViewById(R.id.factsicon);
        imageView.setOnClickListener(this);


        LinearLayout linearLayout1 = findViewById(R.id.requestBlood);
        linearLayout1.setOnClickListener(this);

        ImageView imageView1 = findViewById(R.id.requestBloodIcon);
        imageView1.setOnClickListener(this);

        LinearLayout linearLayout2 = findViewById(R.id.myacoountlayout);
        linearLayout2.setOnClickListener(this);

        ImageView imageView2 = findViewById(R.id.myacoount);
        imageView2.setOnClickListener(this);


        LinearLayout linearLayout3 = findViewById(R.id.nearbyBloodBanksLayout);
        linearLayout3.setOnClickListener(this);

        ImageView imageView3 = findViewById(R.id.nearbybloodbank);
        imageView3.setOnClickListener(this);

        ImageView imageView4 = findViewById(R.id.searchDonor);
        imageView4.setOnClickListener(this);

        LinearLayout linearLayout4  = findViewById(R.id.search_donor_text);
        linearLayout4.setOnClickListener(this);

        LinearLayout linearLayout5 = findViewById(R.id.logoutLayout);
        linearLayout5.setOnClickListener(this);

        ImageView imageView5 = findViewById(R.id.logout);
        imageView5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.search_donor_text || v.getId()==R.id.searchDonor)
        {
            Intent intent = new Intent(MainMenuActivity.this, SearchDonorsActivity.class);
            startActivity(intent);
        }

        if(v.getId()==R.id.facts || v.getId()==R.id.factsicon)
        {
            Intent intent = new Intent(MainMenuActivity.this, BloodCompatibilityListActivity.class);
            startActivity(intent);
        }

        if(v.getId()==R.id.requestBlood || v.getId()==R.id.requestBloodIcon)
        {
            Intent intent = new Intent(MainMenuActivity.this, AllRequestActivity.class);
            startActivity(intent);
        }

        if(v.getId()==R.id.myacoount|| v.getId()==R.id.myacoountlayout)
        {
            Intent intent = new Intent(MainMenuActivity.this, UserProfileActivity.class);
            startActivity(intent);
        }

        if(v.getId()==R.id.nearbybloodbank|| v.getId()==R.id.nearbyBloodBanksLayout)
        {
            Intent intent = new Intent(MainMenuActivity.this, NearbyBloodBanksActivity.class);
            startActivity(intent);
        }

        if(v.getId()==R.id.logoutLayout || v.getId()==R.id.logout)
        {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
