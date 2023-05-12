package com.example.myapplication.view;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Login.db";

    public DBHelper(Context context) {
        super(context, "Login.db", null, 1);
    }

    // Called when the database is created for the first time
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        // Create a table called "users" with two columns: "username" and "password"
        MyDB.execSQL("create Table users(username TEXT primary key, password TEXT)");
    }

    // Called when the database needs to be upgraded
    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        // Drop the existing "users" table if it exists
        MyDB.execSQL("drop Table if exists users");
    }

    // Insert a new user into the database
    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues= new ContentValues();
        contentValues.put("username", username);
        contentValues.put("password", password);
        long result = MyDB.insert("users", null, contentValues);
        if(result==-1)
            return false; // Insertion failed
        else
            return true; // Insertion successful
    }

    // Check if a username already exists in the database
    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ?", new String[]{username});
        if (cursor.getCount() > 0)
            return true; // Username already exists
        else
            return false; // Username does not exist
    }

    // Check if the combination of username and password exists in the database
    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("Select * from users where username = ? and password = ?", new String[] {username,password});
        if(cursor.getCount()>0)
            return true; // Combination of username and password exists
        else
            return false; // Combination of username and password does not exist
    }
}
