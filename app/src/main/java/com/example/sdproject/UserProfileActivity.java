package com.example.sdproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private AdapterOfUserInfo adapter;
    private List<User> userList;
    User value;
    ProgressBar progressBar;
    Button edit;


    DatabaseReference userinfo;
    FirebaseUser users;

    TextView name, email, mobile, address, bddate, gender, bggroup, donatedate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprofile);


        ImageView backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(UserProfileActivity.this, MainMenuActivity.class);
                //startActivity(intent);
                finish();
            }
        });


        name = findViewById(R.id.nametext);
        email = findViewById(R.id.Emailtext);
        mobile = findViewById(R.id.mobiletext);
        address = findViewById(R.id.addresstext);
        bddate = findViewById(R.id.bdtext);
        gender = findViewById(R.id.gendertext);
        bggroup = findViewById(R.id.bgtext);
        donatedate = findViewById(R.id.donateDateText);

        progressBar = findViewById(R.id.progressBar);

        progressBar.setVisibility(View.VISIBLE);


        edit = findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
                //finish();
            }
        });


        mAuth = FirebaseAuth.getInstance();
        users = mAuth.getCurrentUser();
        userinfo = FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getUid());

        userinfo.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressBar.setVisibility(View.GONE);

                value = dataSnapshot.getValue(User.class);
                Log.d("Gender",value.getGender());
                setValues();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            userList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    public void setValues() {
        try
        {
            gender.setText(value.getGender());
            bddate.setText(value.getBirthDate());
            bggroup.setText(value.getBloodGroup());
            name.setText("Name : " +value.getName());
            email.setText("Email : "+value.getEmail());
            donatedate.setText(value.getDonationDate());
            mobile.setText("Mobile no : "+value.getPhone());
            address.setText("Address : "+ value.getAddress());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
