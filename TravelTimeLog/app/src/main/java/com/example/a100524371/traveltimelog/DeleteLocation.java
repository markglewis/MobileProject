package com.example.a100524371.traveltimelog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//activity to delete location specified by user
public class DeleteLocation extends AppCompatActivity {
    //spinner to display locations
    Spinner locationList;
    String name;
    //array list to store all the address of locations
    ArrayList<String> locationArrayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_location);
        Intent intent = getIntent();
        name = intent.getStringExtra(getString(R.string.name));
        //gets name of user
        //initializes view and arraylist
        locationList = (Spinner) findViewById(R.id.spinner);
        locationArrayList = new ArrayList<>();
        readFile();
        //reads file
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, locationArrayList);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locationList.setAdapter(spinnerArrayAdapter);
    }
    public void readFile(){


        FileInputStream fileInputStream;

        locationArrayList.clear();
        //clear list
        //uses regex to parse locations
        try {
            fileInputStream = this.openFileInput(name+getString(R.string.file));
            BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            String pattern = "([-+]?[0-9]*\\.?[0-9]+) ([-+]?[0-9]*\\.?[0-9]+) ([0-9]+)";
            while ((line = br.readLine()) != null) {
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(line);
                if(m.find()) {locationArrayList.add(m.group(1) + " " + m.group(2) + " " + m.group(3));
                }
            }
            br.close();
        }
        catch (IOException e) {
        }

    }
    //sends the selected by spinner array locations and sends it back to show location to be deleted
    public void deleteCon(View view) {
        if(locationList.getSelectedItemPosition() != -1) {
            Intent intent = new Intent();
            intent.putExtra(getString(R.string.position), locationList.getSelectedItemPosition());
            setResult(100, intent);
            finish();
        }
    }
    //cancels activity and returns to showlocation acativity
    public void cancel(View view) {
        finish();
    }
}
