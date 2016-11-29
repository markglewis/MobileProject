package com.example.a100524371.traveltimelog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//activity to display locations for a user
public class ShowLocations extends AppCompatActivity implements LocationListener {
    //stores current users values
    String name;
    String password;
    Double currentLat;
    Double currentLon;
    ArrayList<Location> locationsArrayList;
    //declaring views to display locations
    LocationListAdapter arrayAdapter;
    ListView listView;
    //values for permisions
    private static final int MY_PERMISSIONS_REQUEST_GET_COARSE_LOCATION= 1;
    private static final int MY_PERMISSIONS_REQUEST_GET_FINE_LOCATION= 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_locations);
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name));
        password = intent.getStringExtra(getString(R.string.pass));
        //stores current users values
        //array to store list of locations stored
        locationsArrayList = new ArrayList<>();

        //initialize listview to display locations and location list array
        listView = (ListView) findViewById(R.id.LocationView);
        arrayAdapter = new LocationListAdapter(this,locationsArrayList );
        //set adapter
        listView.setAdapter(arrayAdapter);

        //crete location manager to track the users location
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(ShowLocations.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_COARSE_LOCATION);
            ActivityCompat.requestPermissions(ShowLocations.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_FINE_LOCATION);
            //if user hasnt enabled permissions prompt to grant permissions

            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 30, this);
        //contackt gps every 1 s and 30 m
    }


    public void logout(View view) {
        finish();
    }

    public void deleteAccount(View view) {
        //method to delete user and end activity to return to login activity
        Intent intent = new Intent();
        intent.putExtra(getString(R.string.susername), name);
        intent.putExtra(getString(R.string.spassword), password);
        setResult(2, intent);
        finish();

    }

    public void addLoc(View view) {
        //start activity to add location
        Intent intent = new Intent(this, AddLocation.class);
        startActivityForResult(intent, 100);
    }

    public void deleteLoc(View view) {
        //start activity to delete location
        File file = new File(this.getFilesDir(), name+getString(R.string.address_file));
        FileOutputStream fileOutputStream;
        //store user address a txt file to be read by the delete activity
        try {
            fileOutputStream = openFileOutput( name+getString(R.string.address_file), Context.MODE_PRIVATE);

            for(Location location: locationsArrayList) {
                if(location != null) {
                    fileOutputStream = openFileOutput(name+getString(R.string.address_file), Context.MODE_PRIVATE);

                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

                    bw.write(location.getName() + ": " + location.getAddress1());
                    bw.newLine();

                    bw.close();



                    fileOutputStream.close();
                }
            }

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, DeleteLocation.class);
        intent.putExtra("name", name);
        startActivityForResult(intent, 200);
    }

    public void readFile(){
    //reads list of locations and stors them in the location array

        FileInputStream fileInputStream;

        locationsArrayList.clear();

        try {
            fileInputStream = this.openFileInput(name+".txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            String pattern = "([-+]?[0-9]*\\.?[0-9]+) ([-+]?[0-9]*\\.?[0-9]+) ([0-9]+)";
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(line);
                if(m.find()) {
                    locationsArrayList.add(new Location(Double.parseDouble(m.group(1)), Double.parseDouble(m.group(2)), Integer.parseInt(m.group(3))));
                }
            }
            br.close();
        }
        catch (IOException e) {
        }
        //declare location info for all locatitons
        callibrate();
    }
    public void writeFile(){
        File file = new File(this.getFilesDir(), name +getString(R.string.file));
        FileOutputStream fileOutputStream;

        try {
            fileOutputStream = openFileOutput(name+getString(R.string.file), Context.MODE_PRIVATE);

            for(Location location: locationsArrayList) {
                if(location != null) {
                    String toPrint = location.getLatitude() + " " + location.getLongitude() + " " + location.getTimesVisited() + "\n";
                    fileOutputStream.write(toPrint.getBytes());
                }
            }

            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //declare location info for all locatitons
        callibrate();
    }
    @Override
    protected void onStart() {
        super.onStart();
        //read file on start
        readFile();

    }

    @Override
    protected void onStop() {
        super.onStop();
        //at end write to file
        writeFile();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //handles data from other activies
        if(requestCode == 100) {
            if(resultCode == 1){
                //add current location to array list
                if(currentLat != null && currentLon != null){
                    locationsArrayList.add(new Location(currentLat,currentLon,1));
                    writeFile();
                    arrayAdapter.notifyDataSetChanged();
                }
                else
                    Toast.makeText(this, R.string.sattelite_warning,Toast.LENGTH_SHORT).show();

            }
            //adds specified locations to array list
            if(resultCode == 2 || resultCode == 3){
                if(data != null) {
                    Double latitude = data.getDoubleExtra(getString(R.string.slatitude),0);
                    Double longitude = data.getDoubleExtra(getString(R.string.slongitude),0);
                    int time = data.getIntExtra(getString(R.string.times), -1);

                    if(latitude != null && longitude != null && time != -1) {
                        Location location = new Location(latitude,longitude,time);
                        locationsArrayList.add(location);
                        writeFile();
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }
            //deletes selected locations to array list
        } else if(requestCode == 200) {
            if(resultCode == 100) {
                int pos = data.getIntExtra(getString(R.string.position), -1);
                if(pos != -1) {
                    locationsArrayList.remove(pos);
                    writeFile();
                    readFile();
                    arrayAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        currentLat = location.getLatitude();
        currentLon = location.getLongitude();
        //when GPS updates location up date variables

        //geocoder used to check if user enters an area again and adds to that
        if(Geocoder.isPresent()){

            Geocoder geocoder = new Geocoder(this , Locale.getDefault());

            String address="";
            try{
                List<Address> ls = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                for(Address addr: ls){
                    address = addr.getAddressLine(0);
                }




                for(Location temp: locationsArrayList){
                    if(temp.getAddress1().equals(address)){
                        temp.addTimes();
                    }
                }
            }
            catch(IOException e){

            }
        }

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
    //calls google api to set the finer details of a location
    public void callibrate(){
        if(Geocoder.isPresent()){

            Geocoder geocoder = new Geocoder(this , Locale.getDefault());

            try{
                for(Location location: locationsArrayList){
                    List<Address> ls = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 1);
                    for(Address addr: ls){
                        location.setName(addr.getFeatureName());
                        location.setAddress1(addr.getAddressLine(0));
                        location.setCity(addr.getLocality());
                        location.setProv(addr.getAdminArea());
                        location.setCountry(addr.getCountryName());
                        location.setPostalCode(addr.getPostalCode());
                        location.setPhone(addr.getPhone());
                        location.setUrl(addr.getUrl());
                    }
                }

            }
            catch(IOException e){

            }
        }
    }
}
