package com.example.sdproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SignUpActivity extends AppCompatActivity {

    private TextView textViewDateOfBirth;
    private TextView textViewBloodDonationDate;

    EditText EditTextEmail, EditTextName, EditTextMobile, EditTextPassword, EditTextAddress;

    private ImageView backButton;
    private Button signUpButton;

    private DatePickerDialog datePickerDialog;

    private ProgressBar progressBar;

    RadioGroup radioGroup;
    int buttonID;
    RadioButton genderButton;

    String genderValue;
    Spinner spinner;

    boolean isDateOfBirthValid = true, isDonateDateValid = true, isNeverDonatedBlood = false;

    private  CheckBox checkBox;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        radioGroup = findViewById(R.id.genderGroup);

        buttonID = radioGroup.getCheckedRadioButtonId();
        genderButton =  findViewById(buttonID);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    genderValue = checkedRadioButton.getText().toString();
                }
            }
        });

        progressBar = findViewById(R.id.progressBar);
        EditTextAddress = findViewById(R.id.address);


        textViewDateOfBirth = findViewById(R.id.DateOfBirth);
        textViewDateOfBirth.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int currentDay = c.get(Calendar.DAY_OF_MONTH);
                int currentMonth = c.get(Calendar.MONTH);
                int currentYear = c.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewDateOfBirth.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        if(!checkDate(month+1,dayOfMonth,year))
                        {
                            Toast.makeText(SignUpActivity.this,"Invalid date",Toast.LENGTH_LONG).show();
                            isDateOfBirthValid = false;
                        }
                        else
                        {
                            isDateOfBirthValid = true;
                        }

                    }
                }, currentYear, currentMonth, currentDay);

                datePickerDialog.show();


            }
        });


        textViewBloodDonationDate = findViewById(R.id.BloodDonationDateSelect);
        textViewBloodDonationDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                int currentDay = c.get(Calendar.DAY_OF_MONTH);
                int currentMonth = c.get(Calendar.MONTH);
                int currentYear = c.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(SignUpActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textViewBloodDonationDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        if(!checkDate(month+1,dayOfMonth,year))
                        {
                            Toast.makeText(SignUpActivity.this,"Invalid date",Toast.LENGTH_LONG).show();
                            isDonateDateValid = false;
                        }
                        else
                        {
                            isDonateDateValid = true;
                        }

                    }
                }, currentYear, currentMonth, currentDay);

                datePickerDialog.show();
            }
        });



        backButton =  findViewById(R.id.back);
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        EditTextEmail = findViewById(R.id.Email);
        EditTextName = findViewById(R.id.name);
        EditTextMobile = findViewById(R.id.MoobileNo);
        EditTextPassword = findViewById(R.id.password);
        signUpButton = findViewById(R.id.signUp);
        checkBox = findViewById(R.id.neverdonatecheck);

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try
                {
                    registerUser();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });



        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    textViewBloodDonationDate.setClickable(false);
                    textViewBloodDonationDate.setTextColor(Color.parseColor("#8BFFFFFF"));
                    isNeverDonatedBlood = true;
                }
                else
                {
                    textViewBloodDonationDate.setClickable(true);
                    try{
                        textViewBloodDonationDate.setTextColor(Color.WHITE);
                        isNeverDonatedBlood = false;
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }

                }
            }
        });


        progressBar.setVisibility(View.GONE);
        spinner = findViewById(R.id.spinner);

        String bloodgroups[] ={
                "A(+ve)", "A(-ve)", "B(+ve)","B(-ve)","O(+ve)","O(-ve)","AB(+ve)","AB(-ve)"
        };


        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,R.layout.custom_spiner,bloodgroups);
        spinnerArrayAdapter.setDropDownViewResource(R.layout.custom_spiner);
        spinner.setAdapter(spinnerArrayAdapter);
    }





    boolean checkDate(int month, int day, int year)
    {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Calendar c = Calendar.getInstance();

            Date date1 = sdf.parse(year+"-"+month+"-"+day);

            //current date
            Date date2 = sdf.parse(sdf.format(c.getTime()));

            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(date1);
            cal2.setTime(date2);

            if (cal1.after(cal2)) {
                return false;
            }

        }
        catch (Exception e)
        {

        }

        return true;
    }


    boolean fixAvailability(int day, int month, int year)
    {
        Calendar c = Calendar.getInstance();

        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentYear = c.get(Calendar.YEAR);

        int dontaionDay = day;
        int donationMonth = month;
        int donationYear = year;

        return ( Math.abs(donationMonth-currentMonth) + Math.abs(donationYear*12 - currentYear*12))>=4;
    }



    private void registerUser() {
         final String name = EditTextName.getText().toString().trim();
         final String email = EditTextEmail.getText().toString().trim();
         final String password = EditTextPassword.getText().toString().trim();
         final String phone = EditTextMobile.getText().toString().trim();
         final String bloodGroup = spinner.getSelectedItem().toString();
         final String address = EditTextAddress.getText().toString().trim();


         String getDonateDate ;
         String availabilty = "No";


         if(isNeverDonatedBlood)
             getDonateDate = "Never donated Blood";
         else
         {
             getDonateDate = textViewBloodDonationDate.getText().toString();

             String split[] = getDonateDate.split("/");

             int day = Integer.parseInt(split[0]);
             int month = Integer.parseInt(split[1]);
             int year = Integer.parseInt(split[2]);
             
             if(fixAvailability(day,month,year))
             {
                 availabilty = "Yes";
             }
             
             else
             {
                 availabilty = "No";
             }
         }

         final String avail = availabilty;

         final String donateDate = getDonateDate;

         final String birthDate = textViewDateOfBirth.getText().toString();

         if (name.isEmpty()) {
            EditTextName.setError("EditTextName cannot be empty");
            EditTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            EditTextEmail.setError("Email cannot be empty");
            EditTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            EditTextEmail.setError("Invalid Email");
            EditTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            EditTextPassword.setError("EditTextPassword cannot be empty");
            EditTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            EditTextPassword.setError("EditTextPassword should be at least 6 character long");
            EditTextPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            EditTextMobile.setError("Phone Number cannot be empty");
            EditTextMobile.requestFocus();
            return;
        }

        if (phone.length() != 11) {
            EditTextMobile.setError("Invalid Phone number");
            EditTextMobile.requestFocus();
            return;
        }

        if(address.isEmpty())
        {
            EditTextAddress.setError("EditTextAddress cannot be empty");
            EditTextAddress.requestFocus();
            return;
        }

        if(genderValue==null)
        {
            Toast.makeText(SignUpActivity.this, "Select Gender", Toast.LENGTH_LONG).show();
            radioGroup.requestFocus();
            return;
        }

        if(!isDateOfBirthValid)
        {
            Toast.makeText(SignUpActivity.this,"Invalid Birth date",Toast.LENGTH_LONG).show();
            return;
        }

        if(!isDonateDateValid && !isNeverDonatedBlood)
        {
            Toast.makeText(SignUpActivity.this,"Invalid Last Donation date",Toast.LENGTH_LONG).show();
            return;
        }

        if(birthDate.equals("Select date ...."))
        {
            Toast.makeText(SignUpActivity.this,"Select Date of Birth",Toast.LENGTH_LONG).show();
            return;
        }

        if(donateDate.equals("Select date ...."))
        {
            Toast.makeText(SignUpActivity.this,"Select Blood Donation Date",Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
        //String name, String email, String phone, String gender, String textViewAddress, String donationDate, String birthDate, String bloodGroup

                            User user = new User(
                                    name,
                                    email,
                                    phone,
                                    genderValue,
                                    address,
                                    donateDate,
                                    birthDate,
                                    bloodGroup,
                                    avail,
                                    password
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_LONG).show();
                                        FirebaseAuth.getInstance().signOut();
                                        resetValue();
                                    } else {
                                        //display a failure message

                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

        /*User user = new User(
                name,
                email,
                phone,
                genderValue,
                textViewAddress,
                donateDate,
                birthDate,
                bloodGroup,
                avail
        );

        DatabaseHandler databaseHandler = new DatabaseHandler();
        databaseHandler.CreateUser(SignUpActivity.this,user,password);
        progressBar.setVisibility(View.GONE);
        resetValue();*/

    }



    void resetValue()
    {
        EditTextEmail.setText("");
        EditTextName.setText("");
        EditTextMobile.setText("");
        EditTextPassword.setText("");
        EditTextAddress.setText("");
        textViewBloodDonationDate.setText("Select Date ....");
        radioGroup.setSelected(false);
        textViewDateOfBirth.setText("Select Date ....");
    }
}
