package com.example.sdproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private  static final int DATABASE_VERSION=1;
    private static final String DATABASE_NAME="usermanager";
    private static final String TABLE_USER ="Users";
    private static final String KEY_EMAIL="email";
    private static final String KEY_NAME="name";
    private static final String KEY_PHONE="phone";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_BIRTH_DATE = "birthdate";
    private static final String KEY_BLOOD_GROUP = "bloodgroup";
    private static final String KEY_AVAILABILITY = "availability";
    private static final String KEY_DONATION_DATE = "donationdate";


    public DatabaseHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        /*String  CREATE_USERS_TABLE="CREATE TABLE IF NOT EXISTS "+ TABLE_USER +" ("
                +KEY_EMAIL+"TEXT PRIMARY KEY,"+KEY_NAME+"TEXT,"
                +KEY_PHONE+"TEXT,"+ KEY_GENDER+"TEXT ,"
                +KEY_ADDRESS+"TEXT,"+ KEY_BIRTH_DATE+"TEXT ,"+
                KEY_BLOOD_GROUP+"TEXT,"+KEY_AVAILABILITY+"TEXT,"+
                KEY_DONATION_DATE+"TEXT,"+KEY_PASSWORD+"TEXT"+")";*/

        String CREATE_USERS_TABLE = "create table "+ TABLE_USER + "( EMAIL PRIMARY KEY, NAME, PHONE, GENDER, ADDRESS, PASSWORD, BIRTHDATE," +
                "BLOODGROUP, AVAILABILITY, DONATIONDATE)";

        db.execSQL(CREATE_USERS_TABLE);
        //db.close();

    }

    //Called when the database needs to be upgraded.
    // The implementation should use this method to drop tables, add tables,
    // or do anything else it needs to upgrade to the new schema version.

    //public abstract void onUpgrade (SQLiteDatabase db,
    //                int oldVersion,
    //                int newVersion)

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_USER);
        onCreate(db);
    }


    //CRUD = Create , Read, Update, Delete
    //Create - Write in Table
    //Read - Retrieve Table
    //Update - Modify Row
    //Delete - Dispose Row


    void addUser(User user)
    {
        try
        {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();

                /*String  CREATE_USERS_TABLE="CREATE TABLE "+ TABLE_USER +"("
                +KEY_EMAIL+"TEXT PRIMARY KEY,"+KEY_NAME+"TEXT,"
                +KEY_PHONE+"TEXT,"+ KEY_GENDER+"TEXT,"
                +KEY_ADDRESS+"TEXT,"+ KEY_BIRTH_DATE+"TEXT,"+
                KEY_BLOOD_GROUP+"TEXT,"+KEY_AVAILABILITY+"TEXT,"+
                KEY_DONATION_DATE+"TEXT,"+KEY_PASSWORD+"TEXT"+")";*/

            values.put(KEY_NAME, user.getName());
            values.put(KEY_EMAIL,user.getEmail());
            values.put(KEY_PHONE,user.getPhone());
            values.put(KEY_GENDER,user.getGender());
            values.put(KEY_ADDRESS, user.getAddress());
            values.put(KEY_BIRTH_DATE,user.getBirthDate());
            values.put(KEY_BLOOD_GROUP,user.getBloodGroup());
            values.put(KEY_AVAILABILITY,user.getAvailability());
            values.put(KEY_DONATION_DATE,user.getDonationDate());
            values.put(KEY_PASSWORD,user.getPassword());


            try{
                db.insert(TABLE_USER,null,values);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            db.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    User getUser(String Email){
        SQLiteDatabase db=this.getReadableDatabase();

       // (String name, String email, String phone, String gender, String address, String donationDate, String birthDate, String bloodGroup, String availability, String password)

        Cursor cursor=db.query(TABLE_USER,new String[]{KEY_NAME,KEY_EMAIL,KEY_PHONE,KEY_GENDER,KEY_ADDRESS,KEY_DONATION_DATE,KEY_BIRTH_DATE,KEY_BLOOD_GROUP,KEY_AVAILABILITY,KEY_PASSWORD},KEY_EMAIL+"=?",
                new String[]{Email},null,null,null,null);
        if(cursor!=null)
        {
            cursor.moveToFirst();
        }

        User user=new User();

        user.setEmail(cursor.getString(1));
        user.setName(cursor.getString(0));
        user.setPhone(cursor.getString(2));
        user.setGender(cursor.getString(3));
        user.setAddress(cursor.getString(4));
        user.setBirthDate(cursor.getString(5));
        user.setBloodGroup(cursor.getString(6));
        user.setAvailability(cursor.getString(7));
        user.setDonationDate(cursor.getString(8));
        user.setPassword(cursor.getString(9));

        return user;
    }


    public List<User>getAllContacts(){
        List<User> userlist=new ArrayList<>();

        String selectQuery ="SELECT *FROM " + TABLE_USER;

        SQLiteDatabase db=this.getWritableDatabase();

        //Runs the provided SQL and returns a Cursor over the result set.
        Cursor cursor=db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{

               /*String  CREATE_USERS_TABLE="CREATE TABLE "+ TABLE_USER +"("
                +KEY_EMAIL+"TEXT PRIMARY KEY,"+KEY_NAME+"TEXT,"
                +KEY_PHONE+"TEXT,"+ KEY_GENDER+"TEXT,"
                +KEY_ADDRESS+"TEXT,"+ KEY_BIRTH_DATE+"TEXT,"+
                KEY_BLOOD_GROUP+"TEXT,"+KEY_AVAILABILITY+"TEXT,"+
                KEY_DONATION_DATE+"TEXT,"+KEY_PASSWORD+"TEXT"+")";*/

                User user=new User();

                user.setEmail(cursor.getString(1));
                user.setName(cursor.getString(0));
                user.setPhone(cursor.getString(2));
                user.setGender(cursor.getString(3));
                user.setAddress(cursor.getString(4));
                user.setBirthDate(cursor.getString(5));
                user.setBloodGroup(cursor.getString(6));
                user.setAvailability(cursor.getString(7));
                user.setDonationDate(cursor.getString(8));
                user.setPassword(cursor.getString(9));

                userlist.add(user);

            }while (cursor.moveToNext());
        }

        db.close();
        return  userlist;
    }


    public int updateUser(User user){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues  values=new ContentValues();

        values.put(KEY_NAME, user.getName());
        //values.put(KEY_EMAIL,user.getEmail());
        values.put(KEY_PHONE, user.getPhone());
        values.put(KEY_GENDER,user.getGender());
        values.put(KEY_ADDRESS, user.getAddress());
        values.put(KEY_BIRTH_DATE,user.getBirthDate());
        values.put(KEY_BLOOD_GROUP,user.getBloodGroup());
        values.put(KEY_AVAILABILITY,user.getAvailability());
        values.put(KEY_DONATION_DATE,user.getDonationDate());
        values.put(KEY_PASSWORD,user.getPassword());


        return db.update(TABLE_USER,values,KEY_EMAIL+"=?",
                new String[]{String.valueOf(user.getEmail())});
    }




    public void deleteUser(User user){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_USER,KEY_EMAIL+"=?",
                new String[]{String.valueOf(user.getEmail())});
        db.close();
    }

    public int getUserCount(){
        String countQuery="SELECT *FROM " + TABLE_USER;
        SQLiteDatabase db =this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);

        cursor.close();

        return cursor.getCount();
    }


    public boolean checkUser(String email, String password) {
        // array of columns to fetch
        String[] columns = {
                KEY_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KEY_EMAIL + " = ?" + " AND " + KEY_PASSWORD + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};

        // query user table with conditions
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }

    public boolean ifUserAlreadyExits(String email) {
        // array of columns to fetch
        String[] columns = {
                KEY_EMAIL
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = KEY_EMAIL + " = ?";

        // selection arguments
        String[] selectionArgs = {email};

        // query user table with conditions
        Cursor cursor = db.query(TABLE_USER, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();

        cursor.close();
        db.close();
        if (cursorCount > 0) {
            return true;
        }
        return false;
    }


}