package com.example.a100524371.traveltimelog;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
//activity for adding locations
public class AddLocation extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
    }

    public void addCurrentLocation(View view) {
        //if user clicks add current location return to previous activity
        //setresult to 1 to tell showlocations to add by current location
        Intent intent = new Intent();
        setResult(1,intent);
        finish();
    }

    public void addByAddress(View view) {
        //user adds by address
        //use geocoder to determine longitude and latitude
        //sends data back to previous activity to store as location
        if(Geocoder.isPresent()){
            Intent intent = new Intent();
            EditText addressinput = (EditText) findViewById(R.id.addressTxt);
            String address = addressinput.getText().toString();
            Geocoder geocoder = new Geocoder(this , Locale.getDefault());

            try{
                List<Address> ls = geocoder.getFromLocationName(address, 10);
                for(Address a: ls) {
                    intent.putExtra(getString(R.string.slatitude),a.getLatitude());
                    intent.putExtra(getString(R.string.slongitude), a.getLongitude());
                    intent.putExtra(getString(R.string.times), 1);
                }

                }
            catch(IOException e){

            }

            setResult(2,intent);
            finish();

        }


    }
    //mwthod that checks if a given string can be converted into a double
    //returns as bool
    boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    //takes inputed coordinates of latitude and longitude
    //returns data to location activity to as a location
    public void addByCoordinates(View view) {
        EditText lat = (EditText) findViewById(R.id.latitudeTxt);
        EditText lon = (EditText) findViewById(R.id.longitudeTxt);

        if(isDouble(lat.getText().toString())  &&  isDouble(lon.getText().toString())){
            Double la = Double.parseDouble(lat.getText().toString());
            Double lo = Double.parseDouble(lon.getText().toString());


            Intent intent = new Intent();
            intent.putExtra(getString(R.string.slatitude),la);
            intent.putExtra(getString(R.string.slongitude),lo);
            intent.putExtra(getString(R.string.times), 0);
            setResult(3,intent);
            finish();
        }
        else
            Toast.makeText(this, R.string.lat_lon_input_warning,Toast.LENGTH_SHORT).show();


    }
    //ends activity if user wants to back out
    public void cancel(View view) {
        finish();
    }
}
