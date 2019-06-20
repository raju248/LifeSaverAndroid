package com.example.sdproject;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private AdapterOfRequest adapter;
    private List<BloodRequest> bloodRequestList;
    TextView textView;
    TextView makeRequest;
    TextView deleteRequest;
    TextView viewYourRequest;
    
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_request);

        textView = findViewById(R.id.no_donor_text);
        textView.setVisibility(View.INVISIBLE);

        makeRequest = findViewById(R.id.makeRequest);
        makeRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query RequestQuery =    ref.child("Request").orderByChild("id").equalTo(FirebaseAuth.getInstance().getUid().toString());

                RequestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren())
                            {
                                Toast.makeText(AllRequestActivity.this,"You have already made a Request",Toast.LENGTH_LONG).show();
                            }
                        }

                        else
                        {
                            Intent intent = new Intent(AllRequestActivity.this, PostBloodRequestActivity.class);
                            startActivity(intent);
                            //finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });


        deleteRequest = findViewById(R.id.DeleteRequest);

        deleteRequest.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query RequestQuery = ref.child("Request").orderByChild("id").equalTo(FirebaseAuth.getInstance().getUid());

                RequestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if(dataSnapshot.exists())
                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful())
                                    {
                                        Toast.makeText(AllRequestActivity.this,"Request deleted",Toast.LENGTH_LONG).show();
                                    }

                                }
                            });

                            Query query = FirebaseDatabase.getInstance().getReference("Request");
                            clear();
                            query.addValueEventListener(valueEventListener);
                        }

                        else
                        {
                            Toast.makeText(AllRequestActivity.this,"No Request to delete",Toast.LENGTH_LONG).show();
                        }
                    }



                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });


        viewYourRequest = findViewById(R.id.ViewYourRequest);
        viewYourRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query RequestQuery = ref.child("Request").orderByChild("id").equalTo(FirebaseAuth.getInstance().getUid());

                RequestQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists())
                        {
                            for(DataSnapshot snapshot: dataSnapshot.getChildren())
                            {
                                BloodRequest request = snapshot.getValue(BloodRequest.class);

                                AlertDialog.Builder mBuilder = new AlertDialog.Builder(AllRequestActivity.this);

                                View mView = getLayoutInflater().inflate(R.layout.activity_pop_up_request,null);
                                TextView textViewName = mView.findViewById(R.id.text_view_name);
                                TextView textViewMobile = mView.findViewById(R.id.text_view_mobile);
                                TextView textViewBloodGroup = mView.findViewById(R.id.text_view_bloodGroup);
                                TextView textViewComment = mView.findViewById(R.id.text_view_comment);
                                TextView textViewAddress = mView.findViewById(R.id.text_view_address);


                                textViewName.setText("Requested By : "+ request.getName());
                                textViewMobile.setText("Mobile No : " + request.getMobile());
                                textViewBloodGroup.setText("Required Blood Group : " + request.getBloodGroup());
                                textViewAddress.setText("Address : " + request.getAddress());

                                if(request.getComment().equals("No comment"))
                                {
                                    textViewComment.setVisibility(View.GONE);
                                }
                                else
                                {
                                    textViewComment.setText("Comment : "+request.getComment());
                                }


                                mBuilder.setView(mView);
                                AlertDialog dialog = mBuilder.create();
                                dialog.show();
                            }
                        }

                        else
                        {
                            Toast.makeText(AllRequestActivity.this,"You have made no request",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });


        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        ImageView backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(AllRequestActivity.this,MainMenuActivity.class);
                //startActivity(intent);
                finish();
            }
        });


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bloodRequestList = new ArrayList<>();
        adapter = new AdapterOfRequest(this, bloodRequestList);
        recyclerView.setAdapter(adapter);


        Query query = FirebaseDatabase.getInstance().getReference("Request").orderByChild("bloodGroup");
        clear();
        query.addValueEventListener(valueEventListener);

    }



    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            bloodRequestList.clear();
            if (dataSnapshot.exists()) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    BloodRequest req = snapshot.getValue(BloodRequest.class);
                    bloodRequestList.add(req);
                    textView.setVisibility(View.INVISIBLE);
                }
                progressBar.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
            else if(!dataSnapshot.exists())
            {
                textView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public void clear() {
        int size = bloodRequestList.size();
        bloodRequestList.clear();
        adapter.notifyItemRangeRemoved(0, size);
    }
}
