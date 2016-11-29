package com.example.a100524371.traveltimelog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by 100524371 on 11/2/2016.
 */
//class that determines behaviour of database
public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User.db";
    private  static final String SQL_CREATE_ENTRIES = "CREATE TABLE User(userName varchar(100) not null, " + "password varchar(100) not null, " +"latitude decimal not null, " +"longitude decimal not null);";
    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS User";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
    //array list to store users
    public ArrayList<User> getAllUsers() {
        ArrayList<User> array_list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        try {
            Cursor res =  db.rawQuery( "select * from User", null );
            Log.d("DB", "Count: " + res.getCount());

            try {

                // looping through all rows and adding to list
                if (res.moveToFirst()) {
                    do {
                        User user = new User(res.getString(res.getColumnIndex("userName")),res.getString(res.getColumnIndex("password")), Double.parseDouble(res.getString(res.getColumnIndex("latitude"))), Double.parseDouble(res.getString(res.getColumnIndex("longitude"))));
                        array_list.add(user);
                    } while (res.moveToNext());
                }

            } finally {
                try { res.close(); } catch (Exception ignore) {}
            }

        } finally {
            try { db.close(); } catch (Exception ignore) {}
        }

        return array_list;
    }
    //method to insert user into database
    public boolean insertUser (String userName, String password, double longitude, double latitude ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("userName", userName);
        contentValues.put("password", password);
        contentValues.put("longitude", longitude);
        contentValues.put("latitude", latitude);
        db.insert("User", null, contentValues);
        return true;
    }
    //method to delete user from database
    public Integer deleteUser (String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("User",
                "userName = ?",
                new String[] { name });
    }
    //methods to delete all users from database
    public boolean deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("User",null,null);
        return true;
    }
}
