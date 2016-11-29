package com.example.a100524371.traveltimelog;

import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by 100524371 on 11/27/2016.
 */
//class to store location data
public class Location {
    //variables of a location
    private int timesVisited;
    private double latitude;
    private double longitude;
    private String name="";
    private String address1 = "";
    private String city = "";
    private String prov = "";
    private String country = "";
    private String postalCode = "";
    private String phone = "";
    private String url = "";

    //constructor
    public Location(double lat, double lon, int times){
        this.latitude = lat;
        this.longitude = lon;
        this.timesVisited = times;
    }
    //set methods
    public void addTimes(){
        timesVisited += 1;
    }
    public int getTimesVisited(){
        return timesVisited;
    }
    public double getLatitude(){
        return latitude;
    }
    public double getLongitude(){
        return longitude;
    }
    public String getName(){return name;}
    public String getAddress1(){return  address1;}
    public String getCity(){return  city;}
    public String getProv(){return prov;}
    public String getCountry(){return country;}
    public String getPostalCode(){return postalCode;}
    public String getPhone(){return phone;}
    public String getUrl(){return url;}

    public  void setName(String input){name = input;}
    public  void setAddress1(String input){address1 = input;}
    public  void setCity(String input){city=input;}
    public  void setProv(String input){prov = input;}
    public  void setCountry(String input){ country= input;}
    public  void setPostalCode(String input){postalCode=input;}
    public  void setPhone(String input){ phone=input;}
    public  void setUrl(String input){url=input;}


}
