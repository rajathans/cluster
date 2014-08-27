package com.example.rajat.cluster;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by rajat on 17/7/14.
 */

public class SignUp extends Activity {

    Button join, load;
    EditText userName, password, elecmail;
    SharedPreferences prf; //object relation
    String Name, Pass, EMail;
    TextView heading;

    private boolean isEmpty(TextView etText) {              //this function checks if the txtview is empty or not
        if (etText.getText().toString().trim().length() > 0) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        join = (Button) findViewById(R.id.joinButton);
        load = (Button) findViewById(R.id.loadButton);
        userName = (EditText) findViewById(R.id.editName);
        password = (EditText) findViewById(R.id.editPass);
        elecmail = (EditText) findViewById(R.id.editMail);
        heading = (TextView) findViewById(R.id.textView6);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/amabold.ttf");
        heading.setTypeface(type);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name = userName.getText().toString();
                Pass = password.getText().toString();
                EMail = elecmail.getText().toString();

                //create object of SP
                prf = getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor edit = prf.edit();

                if(isEmpty(userName) || isEmpty(password) || isEmpty(elecmail)){
                    Toast.makeText(getBaseContext(),"Enter all details carefully",Toast.LENGTH_LONG).show();
                }

                else{
                    //now insert values to SP file
                    edit.putString("key_username", Name);
                    edit.putString("key_password", Pass);
                    edit.putString("key_email", EMail);
                    edit.apply(); //saved
                    Toast.makeText(getBaseContext(), "Details saved successfully", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });

       load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                prf = getSharedPreferences("data",MODE_PRIVATE);
                String getName = prf.getString("key_username",Name);
                String getPass = prf.getString("key_password",Pass);
                String getMail = prf.getString("key_email",EMail);
                Toast.makeText(getBaseContext(),"Name : "+getName + "\nPass : "+getPass +"\nEMail ID : "+getMail,Toast.LENGTH_LONG).show();
            }
        });
      }

    public void toSignIn(View view){
        Intent tosignin = new Intent(this,SignIn.class);
        startActivity(tosignin);
    }
 }

