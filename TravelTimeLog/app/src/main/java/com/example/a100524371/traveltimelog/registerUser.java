package com.example.a100524371.traveltimelog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
//class to register user
public class registerUser extends AppCompatActivity {

    EditText username1;
    EditText username2;
    EditText password1;
    EditText password2;
    ArrayList<User> userArrayList;
    DBHelper databaseHelper;
    //declare variables to store info

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        databaseHelper = new DBHelper(this);
        userArrayList = new ArrayList<>();
        userArrayList = databaseHelper.getAllUsers();
        //initialize
        username1 = (EditText) findViewById(R.id.userNameCreate1);
        username2 = (EditText) findViewById(R.id.userNameCreate2);
        password1 = (EditText) findViewById(R.id.passwordCreate1);
        password2 = (EditText) findViewById(R.id.passwordCreate2);
    }

    public void Create(View view) {
        //if user has a matching name and password
        if(username1.getText().toString().equals(username2.getText().toString()) && password1.getText().toString().equals(password2.getText().toString()))
        {
            //check to see if name doest exist in array list
            boolean check = true;
            for(User user: userArrayList){
                if(user.getName() == username1.getText().toString()){
                    check = false;
                }
            }
            //if criteria is met send back to add user
            if (check){
                //send user to main activity

                Intent intent = new Intent();
                intent.putExtra(getString(R.string.susername), username1.getText().toString());
                intent.putExtra(getString(R.string.spassword), password1.getText().toString());
                setResult(1, intent);
                finish();
            }
            //warns user of already existing name
            else{Toast.makeText(this, R.string.match_warning,Toast.LENGTH_SHORT).show();}
        }
        //warns user to check input
        else{Toast.makeText(this, R.string.InputWarning,Toast.LENGTH_SHORT).show();}


    }
    //cancels activity and returns to login activity
    public void cancel(View view) {
        Intent intent = new Intent();
        setResult(0,intent);
        finish();
    }
}
