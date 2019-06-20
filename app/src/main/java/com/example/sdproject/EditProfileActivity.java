package com.example.sdproject;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfileActivity extends AppCompatActivity {

    private TextView textView;
    private TextView bloodDonationDate;
    EditText emails, Name, mobile, Address;
    String passwordFromDatabase = "";
    private ImageView backButton;
    private Button signUpButton;
    private DatePickerDialog datePickerDialog;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private ProgressBar progressBar;

    RadioGroup radioGroup;
    RadioGroup avail;

    int buttonID, availID;
    RadioButton genderButton, availButton;

    String genderValue;
    String availValue;
    Spinner spinner;

    User values;


    boolean isBDvalid = true, isDonateDateValid = true, isNeverDonatedBlood = false;

    CheckBox checkBox;


    String bloodgroups[] ={
            "A(+ve)", "A(-ve)", "B(+ve)","B(-ve)","O(+ve)","O(-ve)","AB(+ve)","AB(-ve)"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser users = mAuth.getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users/"+mAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                values = dataSnapshot.getValue(User.class);
                setValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        radioGroup = findViewById(R.id.genderGroup);

        buttonID = radioGroup.getCheckedRadioButtonId();
        genderButton =  findViewById(buttonID);

        avail = findViewById(R.id.availabilityGroup);

        availID = avail.getCheckedRadioButtonId();
        availButton = findViewById(availID);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                boolean isChecked = checkedRadioButton.isChecked();
                if (isChecked)
                {
                    genderValue = checkedRadioButton.getText().toString();
                }
            }
        });


        avail.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = group.findViewById(checkedId);

                boolean isChecked = checkedRadioButton.isChecked();

                if(isChecked)
                {
                    availValue = checkedRadioButton.getText().toString();
                }
            }
        });

        progressBar = findViewById(R.id.progressBar);

        Address = findViewById(R.id.address);

        textView = findViewById(R.id.dateSelect);
        textView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                int currentDay = c.get(Calendar.DAY_OF_MONTH);
                int currentMonth = c.get(Calendar.MONTH);
                int currentYear = c.get(Calendar.YEAR);

                datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        textView.setText(dayOfMonth + "/" + (month + 1) + "/" + year);

                        if(!checkDate(month+1,dayOfMonth,year))
                        {
                            Toast.makeText(EditProfileActivity.this,"Invalid date",Toast.LENGTH_LONG).show();
                            isBDvalid = false;
                        }
                        else
                        {
                            isBDvalid = true;
                        }

                    }
                }, currentYear, currentMonth, currentDay);

                datePickerDialog.show();
            }
        });

        bloodDonationDate = findViewById(R.id.BloodDonationDateSelect);
        bloodDonationDate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();

                int currentDay = c.get(Calendar.DAY_OF_MONTH);
                int currentMonth = c.get(Calendar.MONTH);
                int currentYear = c.get(Calendar.YEAR);


                datePickerDialog = new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        bloodDonationDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        RadioButton yes = findViewById(R.id.Yes);

                        if(!checkDate(month+1,dayOfMonth,year))
                        {
                            Toast.makeText(EditProfileActivity.this,"Invalid date",Toast.LENGTH_LONG).show();
                            isDonateDateValid = false;
                        }
                        else
                        {
                            isDonateDateValid = true;

                            if(fixAvailability(dayOfMonth,month+1,year))
                            {
                                avail.getChildAt(0).setEnabled(true);
                                avail.getChildAt(1).setEnabled(true);

                                avail.check(R.id.Yes);
                                yes.setTextColor(Color.WHITE);
                            }
                            else
                            {
                                avail.getChildAt(0).setEnabled(false);
                                avail.check(R.id.No);

                                yes.setTextColor(Color.parseColor("#8BFFFFFF"));
                            }
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
                //Intent intent = new Intent(EditProfileActivity.this, UserProfileActivity.class);
                //startActivity(intent);
                finish();
            }
        });

        //EditTextEmail = findViewById(R.id.Email);
        Name = findViewById(R.id.name);
        mobile = findViewById(R.id.MoobileNo);
        signUpButton = findViewById(R.id.signUp);
        checkBox = findViewById(R.id.neverdonatecheck);

        signUpButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                try
                {
                    SaveChanges();
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
                    avail.getChildAt(0).setEnabled(true);
                    avail.getChildAt(1).setEnabled(true);

                    bloodDonationDate.setClickable(false);
                    bloodDonationDate.setTextColor(Color.parseColor("#8BFFFFFF"));
                    isNeverDonatedBlood = true;

                    RadioButton yes = findViewById(R.id.Yes);
                    yes.setTextColor(Color.WHITE);
                }
                else
                {
                    bloodDonationDate.setClickable(true);
                    try{
                        bloodDonationDate.setTextColor(Color.WHITE);
                        isNeverDonatedBlood = false;

                        if(fixAvailabilityWhenLoading(bloodDonationDate.getText().toString()) && !bloodDonationDate.getText().toString().equals("Never donated Blood"))
                        {
                            RadioButton yes = findViewById(R.id.Yes);
                            yes.setTextColor(Color.WHITE);
                            avail.getChildAt(0).setEnabled(true);
                            avail.getChildAt(1).setEnabled(true);
                        }

                        else if(!bloodDonationDate.getText().toString().equals("Never donated Blood"))
                        {
                            RadioButton yes = findViewById(R.id.Yes);
                            yes.setTextColor(Color.WHITE);
                            avail.getChildAt(0).setEnabled(true);
                            avail.getChildAt(1).setEnabled(true);
                        }

                        else
                        {
                            RadioButton yes = findViewById(R.id.Yes);
                            avail.getChildAt(0).setEnabled(false);
                            // yes.setHighlightColor(Color.parseColor("#8BFFFFFF"));
                            yes.setTextColor(Color.parseColor("#8BFFFFFF"));
                        }
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

    private void SaveChanges() {
        final String name = Name.getText().toString().trim();
        final String email = values.getEmail();
        final String phone = mobile.getText().toString().trim();
        final String bloodGroup = spinner.getSelectedItem().toString();
        final String address = Address.getText().toString().trim();
        final String password = passwordFromDatabase;

        String getDonateDate ;


        if(isNeverDonatedBlood)
            getDonateDate = "Never donated Blood";
        else
            getDonateDate = bloodDonationDate.getText().toString();

        final String donateDate = getDonateDate;

        final String birthDate = textView.getText().toString();

        if (name.isEmpty()) {
            Name.setError("EditTextName cannot be empty");
            Name.requestFocus();
            return;
        }

//        if (email.isEmpty()) {
//            EditTextEmail.setError("Email cannot be empty");
//            EditTextEmail.requestFocus();
//            return;
//        }
//
//        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//            EditTextEmail.setError("Invalid Email");
//            EditTextEmail.requestFocus();
//            return;
//        }



        if (phone.isEmpty()) {
            mobile.setError("Phone Number cannot be empty");
            mobile.requestFocus();
            return;
        }

        if (phone.length() != 11) {
            mobile.setError("Invalid Phone number");
            mobile.requestFocus();
            return;
        }

        if(address.isEmpty())
        {
            Address.setError("EditTextAddress cannot be empty");
            Address.requestFocus();
            return;
        }

        if(genderValue==null)
        {
            Toast.makeText(EditProfileActivity.this, "Select Gender", Toast.LENGTH_LONG).show();
            radioGroup.requestFocus();
            return;
        }

        if(!isBDvalid)
        {
            Toast.makeText(EditProfileActivity.this,"Invalid Birth date",Toast.LENGTH_LONG).show();
            return;
        }

        if(!isDonateDateValid && !isNeverDonatedBlood)
        {
            Toast.makeText(EditProfileActivity.this,"Invalid Last Donation date",Toast.LENGTH_LONG).show();
            return;
        }

        if(birthDate.equals("Select date ...."))
        {
            Toast.makeText(EditProfileActivity.this,"Select Date of Birth",Toast.LENGTH_LONG).show();
            return;
        }

        if(donateDate.equals("Select date ...."))
        {
            Toast.makeText(EditProfileActivity.this,"Select Blood Donation Date",Toast.LENGTH_LONG).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        User user = new User(
                name,
                email,
                phone,
                genderValue,
                address,
                donateDate,
                birthDate,
                bloodGroup,
                availValue,
                password
                );

        FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Change Successful", Toast.LENGTH_LONG).show();
                } else {
                    //display a failure message
                }
            }
        });

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


    boolean fixAvailabilityWhenLoading(String date)
    {
        Calendar c = Calendar.getInstance();

        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        int currentMonth = c.get(Calendar.MONTH)+1;
        int currentYear = c.get(Calendar.YEAR);

        String [] split = date.split("/");


        int donationDay = Integer.parseInt(split[0]);
        int donationMonth = Integer.parseInt(split[1]);
        int donationYear = Integer.parseInt(split[2]);

        Log.d("day",String.valueOf(donationDay));


        return ( Math.abs(donationMonth-currentMonth) + Math.abs(donationYear*12 - currentYear*12))>=4;
    }


    void setValue()
    {
        //EditTextEmail.setText(values.getEmail());
        Name.setText(values.getName());
        mobile.setText(values.getPhone());
        Address.setText(values.getAddress());
        bloodDonationDate.setText(values.getDonationDate());
        passwordFromDatabase = values.getPassword();

        if(values.getDonationDate().equals("Never Donated Blood"))
            isNeverDonatedBlood = true;


        for(int i=0;i<bloodgroups.length;i++)
        {
            if(bloodgroups[i].equals(values.getBloodGroup()))
                spinner.setSelection(i);
        }


        if(values.getGender().equals("Male"))
            radioGroup.check(R.id.male);
        else
            radioGroup.check(R.id.Female);

        textView.setText(values.getBirthDate());

        if(!values.getDonationDate().equals("Never donated Blood") && fixAvailabilityWhenLoading(values.getDonationDate()))
        {
            RadioButton yes = findViewById(R.id.Yes);
            yes.setTextColor(Color.WHITE);
            avail.getChildAt(0).setEnabled(true);
            avail.getChildAt(1).setEnabled(true);
        }
        else if(values.getDonationDate().equals("Never donated Blood"))
        {
            RadioButton yes = findViewById(R.id.Yes);
            yes.setTextColor(Color.WHITE);
            avail.getChildAt(0).setEnabled(true);
            avail.getChildAt(1).setEnabled(true);
        }
        else
        {

            RadioButton yes = findViewById(R.id.Yes);
            avail.getChildAt(0).setEnabled(false);
           // yes.setHighlightColor(Color.parseColor("#8BFFFFFF"));
            yes.setTextColor(Color.parseColor("#8BFFFFFF"));
        }

        try
        {
            if(values.getAvailability().equals("Yes"))
                avail.check(R.id.Yes);
            else
                avail.check(R.id.No);
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
