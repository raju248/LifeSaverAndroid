package com.example.sdproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchDonorsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterOfUserInfo adapter;
    private List<User> userList;
    TextView textView;

    DatabaseReference users1;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_donors);

        textView = findViewById(R.id.no_donor_text);
        textView.setVisibility(View.VISIBLE);

        progressBar = findViewById(R.id.progressBar);


        ImageView backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String bloodgroups[] ={
                "A(+ve)", "A(-ve)", "B(+ve)","B(-ve)","O(+ve)","O(-ve)","AB(+ve)","AB(-ve)"
        };

        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.custom_spiner,bloodgroups);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spiner);

        final Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(spinnerArrayAdapter);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new AdapterOfUserInfo(this, userList);
        recyclerView.setAdapter(adapter);


        users1 = FirebaseDatabase.getInstance().getReference("Users");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    clear();
                    progressBar.setVisibility(View.VISIBLE);
                    Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("bloodGroup").equalTo(spinner.getSelectedItem().toString());
                    query.addValueEventListener(valueEventListener);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }

        });

    }


    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            textView.setVisibility(View.VISIBLE);
            userList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if(user.getAvailability().equals("Yes"))
                    {
                        userList.add(user);
                    }

                    if(userList.size()>=1)
                        textView.setVisibility(View.INVISIBLE);
                }
                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
            else if(!dataSnapshot.exists())
            {
                textView.setText("No Donors Found");
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void clear() {
        int size = userList.size();
        userList.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }

}