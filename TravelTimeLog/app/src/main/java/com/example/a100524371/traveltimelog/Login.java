package com.example.a100524371.traveltimelog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
//activity for the login screen
public class Login extends AppCompatActivity implements LocationListener {
    //declares variables
    ArrayList<User> userArrayList;
    DBHelper databaseHelper;
    EditText userNameInput;
    EditText passwordInput;
    CheckBox rememberMe;

    double longitude=0;
    double latitude=0;

    private static final int MY_PERMISSIONS_REQUEST_GET_COARSE_LOCATION= 1;
    private static final int MY_PERMISSIONS_REQUEST_GET_FINE_LOCATION= 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //initialize variables
        databaseHelper = new DBHelper(this);
        userArrayList = new ArrayList<>();
        userArrayList = databaseHelper.getAllUsers();

        userNameInput = (EditText) findViewById(R.id.userNameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        rememberMe = (CheckBox) findViewById(R.id.rememberMeCheckBox);
        readSavedUser();

        //media player that playes song on loop
        MediaPlayer mp = MediaPlayer.create(this, R.raw.lobbymusic);
        mp.setLooping(true);
        mp.start();
        //used to track users location based on GPS
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(Login.this,
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_COARSE_LOCATION);
            ActivityCompat.requestPermissions(Login.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_GET_FINE_LOCATION);

                    //asking for permission
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 60000, 0, this);
        //checks GPS every 60 seconds



    }

    public void checkLogin(View view) {
        String userName = userNameInput.getText().toString();
        String password = passwordInput.getText().toString();
        boolean found = false;
        String conName = "";
        String conPass = "";

        //check if login credentials match
        for(User user: userArrayList){
            if(userName.equals(user.getName())  &&  user.checkPassword(password) ){
                found = true;
                conName = userName;
                conPass = password;
            }
        }
        //if credentials match
        if(found){
            //if save user is selected save their credentials to a file
            if(rememberMe.isChecked()){
                //write to save user file
                saveUser(conName, conPass);
            }
            //start new activity showlocations
            //send user data
            Intent intent = new Intent(this, ShowLocations.class);
            intent.putExtra("name", conName);
            intent.putExtra("pass", conPass);
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);
            setResult(5, intent);
            startActivityForResult(intent, 100);

        }
        else{
            Toast.makeText(this, R.string.notFoundWarning,Toast.LENGTH_SHORT).show();
        }

    }
    //start activity register user to create new login
    public void newLogin(View view) {
        Intent intent = new Intent(this, registerUser.class);
        startActivityForResult(intent, 100);
    }
    //handles when returning to this activity
    @Override
    protected  void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        //create a new user and store to database and user list updates view
        if(resultCode == 1){
            if(data != null){
                String userName = data.getStringExtra(getString(R.string.susername));
                String password = data.getStringExtra(getString(R.string.spassword));

                if(userName != null && password != null){
                    User user = new User(userName,password,longitude,latitude);
                    userArrayList.add(user);
                    databaseHelper.insertUser(user.getName(), password, longitude, latitude);
                }
                userArrayList = databaseHelper.getAllUsers();
            }
        }
        //deletes specified user from database and array list updates view
        else if(resultCode == 2){
            if(data != null){
                String userName = data.getStringExtra(getString(R.string.susername));
                String password = data.getStringExtra(getString(R.string.spassword));
                if(userName != null && password != null){
                    for(User user: userArrayList){
                        if(userName.equals(user.getName())){
                            userArrayList.remove(user);
                            databaseHelper.deleteUser(userName);
                        }
                    }
                }
            }
        }
    }
    //updates location variables when GPS changes
    @Override
    public void onLocationChanged(Location location) {
        longitude = location.getLongitude();
        latitude = location.getLatitude();
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
    //writes user to file specified from textboxes
    public void saveUser(String name, String password){
        {
            File file = new File(this.getFilesDir(), getString(R.string.saved_file));
            FileOutputStream fileOutputStream;

            try {
                fileOutputStream = openFileOutput(getString(R.string.saved_file), Context.MODE_PRIVATE);

                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

                bw.write(name);
                bw.newLine();
                bw.write(password);

                bw.close();



                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    //method to erase user from file
    public void eraseSavedUser(){
        {
            File file = new File(this.getFilesDir(), getString(R.string.saved_file));
            FileOutputStream fileOutputStream;

            try {
                fileOutputStream = openFileOutput(getString(R.string.saved_file), Context.MODE_PRIVATE);

                String toPrint = "";
                fileOutputStream.write(toPrint.getBytes());

                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    //read user from saved user file puts it
    public void readSavedUser(){


            FileInputStream fileInputStream;

            try {
                fileInputStream = this.openFileInput(getString(R.string.saved_file));
                BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
                String name = br.readLine();
                String pass = br.readLine();
                if(name!= null && pass != null){
                    userNameInput.setText(name);
                    passwordInput.setText(pass);
                }
                br.close();
            }
            catch (IOException e) {
        }


    }
    //handles asking for permisions
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GET_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(Login.this, R.string.denied_coarse, Toast.LENGTH_SHORT).show();

                }
                return;
            }
            case MY_PERMISSIONS_REQUEST_GET_FINE_LOCATION:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {

                    Toast.makeText(Login.this, R.string.denied_fine, Toast.LENGTH_SHORT).show();

                }
                return;
            }

        }
    }
}
