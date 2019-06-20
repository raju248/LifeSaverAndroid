package com.example.sdproject;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView textView ;
    EditText emails,password;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    ProgressBar progressBar;
    Button button;
    private  DatabaseHelper databaseHelper;

    //test data
    //razu4.bd@gmail.com
    //22448800

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //databaseHelper = new DatabaseHelper(LoginActivity.this);
       // databaseHelper = new DatabaseHelper(this);


        if (ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }


        /*Query query = FirebaseDatabase.getInstance().getReference("Users");

        //fetched data from FireBase and inserted the data into the SQLite database
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot snapshot: dataSnapshot.getChildren())
                    {
                        User user = snapshot.getValue(User.class);

                        if(!databaseHelper.ifUserAlreadyExits(user.getEmail()))
                                         databaseHelper.addUser(user);

                        //System.out.println(user.getName()+"\n"+user.getPassword()+"\n"+user.getPhone());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/

        progressBar = findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.GONE);

        textView = findViewById(R.id.createnewaccounttext);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        button = findViewById(R.id.loginbutton);
        
        emails = findViewById(R.id.email);
        password = findViewById(R.id.password);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    signIn();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        });


        emails.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    TextView textView1 = findViewById(R.id.incorrect);
                    textView1.setText("");
                }
            }
        });


        password.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                {
                    TextView textView1 = findViewById(R.id.incorrect);
                    textView1.setText("");
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }



    void signIn()
    {
        String email = emails.getText().toString().trim();
        String Password = password.getText().toString().trim();

        try{
            if (email.isEmpty()) {
                emails.setError("Invalid Email");
                emails.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emails.setError("Invalid Email");
                emails.requestFocus();
                return;
            }

            if (Password.isEmpty()) {
                password.setError("Invalid password");
                password.requestFocus();
                return;
            }

            if(isNetworkConnected())
            {
                button.setClickable(false);
                progressBar.setVisibility(View.VISIBLE);

                //User user = databaseHelper.getUser(email);

                //Log.d("password",user.getPassword());

                //if(user.getPassword().equals(Password))

               // if(databaseHelper.checkUser(email,Password))
                {
                    mAuth.signInWithEmailAndPassword(email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            FirebaseUser users = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(),Toast.LENGTH_LONG).show();
                            TextView textView1 = findViewById(R.id.incorrect);
                            button.setClickable(true);
                            progressBar.setVisibility(View.GONE);
                            textView1.setText("Incorrect Email or password!\nPlease try again");
                        }
                        }
                    });

                   /* Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    finish();*/

                }
                /*else
                {
                    //Toast.makeText(LoginActivity.this, ,Toast.LENGTH_LONG).show();
                    TextView textView1 = findViewById(R.id.incorrect);
                    button.setClickable(true);
                    progressBar.setVisibility(View.GONE);
                    textView1.setText("Incorrect Email or password!\n"+ "Please try again");
                }
*/
            }
            else
            {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(LoginActivity.this,"Check Your Internet Connection",Toast.LENGTH_LONG).show();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    private boolean isNetworkConnected()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


}

