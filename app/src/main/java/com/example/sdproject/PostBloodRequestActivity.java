package com.example.sdproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PostBloodRequestActivity extends AppCompatActivity {

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference mDatabase;

    EditText nametext, mobiletext, addresstext, commenttext;
    Button submit;
    Spinner spinner;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_blood_request);

        ImageView backButton = findViewById(R.id.back);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(PostBloodRequestActivity.this,AllRequestActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        nametext = findViewById(R.id.name);
        mobiletext = findViewById(R.id.mobileNo);
        addresstext = findViewById(R.id.address);
        commenttext = findViewById(R.id.comment);


        submit = findViewById(R.id.postRequest);

        submit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                postRequest();
            }
        });



        spinner = findViewById(R.id.spinner);

        String bloodgroups[] ={
                "A(+ve)", "A(-ve)", "B(+ve)","B(-ve)","O(+ve)","O(-ve)","AB(+ve)","AB(-ve)"
        };


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.custom_spiner,bloodgroups);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spiner);
        spinner.setAdapter(spinnerArrayAdapter);
    }


    void postRequest()
    {

        final String name = nametext.getText().toString().trim();
        final String mobileNo = mobiletext.getText().toString().trim();
        final String address = addresstext.getText().toString().trim();
        final String BloodGroup = spinner.getSelectedItem().toString();
        final String Id = mAuth.getUid();

        final String comment;

        if(commenttext.getText().toString().trim().isEmpty())
           comment = "No comment";
        else
           comment = commenttext.getText().toString().trim();


        if (name.isEmpty()) {
            nametext.setError("EditTextName cannot be empty");
            nametext.requestFocus();
            return;
        }


        if(mobileNo.isEmpty())
        {
            mobiletext.setError("Mobile no is required");
            mobiletext.requestFocus();
            return;
        }

        if(mobileNo.length()!=11)
        {
            mobiletext.setError("Invalid Mobile no");
            mobiletext.requestFocus();
            return;
        }

        if(address.isEmpty())
        {
            addresstext.setError("EditTextAddress cannot be empty");
            addresstext.requestFocus();
            return;
        }



        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        final DatabaseReference userNameRef = rootRef.child("Request").child(Id);
        userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()) {
                    //create new BloodRequest
                    BloodRequest req = new BloodRequest(name,mobileNo,address,comment,BloodGroup,Id);

                    userNameRef.setValue(req).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                resetValues();
                                Toast.makeText(PostBloodRequestActivity.this,"Request Successful",Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                            else
                            {

                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(PostBloodRequestActivity.this,"You have already made a BloodRequest",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("mara", databaseError.getMessage()); //Don't ignore errors!
            }
        });


    }

    void resetValues()
    {
        nametext.setText("");
        mobiletext.setText("");
        addresstext.setText("");
        commenttext.setText("");
    }

}
