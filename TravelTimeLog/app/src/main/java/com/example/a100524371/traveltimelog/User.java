package com.example.a100524371.traveltimelog;

import java.util.ArrayList;

/**
 * Created by 100524371 on 11/1/2016.
 */
//class used to store users
public class User {
    //attributes for login and to track location
    private String name;
    private String password;
    private double longitude;
    private double latitude;

    //constructor
    User(String n, String p, double lon, double lat){

        this.name = n;
        this.password = p;
        this.longitude = lon;
        this.latitude = lat;
    }
    //returns name
    public String getName(){
        return name;
    }
    //checks if inputed string matches password
    public Boolean checkPassword(String i){

        if(this.password.equals(i))return  true;
        else return false;
    }
    //return longitude value
    public double getLongitude(){
        return longitude;
    }
    //return latitude value
    public double getLatitude(){
        return latitude;
    }


}
