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

public class SignIn extends Activity {

    Button registerButton,loginButton;
    EditText uName, pWord;
    String nameVal,passVal,Name,Pass;
    SharedPreferences pref;
    TextView header,quote;
    public static final String DEFAULT = "NULL";

    private boolean isEmpty(TextView etText) {              //this function checks if the txtview is empty or not
        return etText.getText().toString().trim().length() <= 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        uName = (EditText) findViewById(R.id.uname);
        pWord = (EditText) findViewById(R.id.passw);
        header = (TextView) findViewById(R.id.textView6);
        quote = (TextView) findViewById(R.id.editText5);
        registerButton = (Button) findViewById(R.id.regButton);
        loginButton = (Button) findViewById(R.id.loginbutton);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/amabold.ttf");
        header.setTypeface(type);

        pref = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
       edit. apply();
        uName.setText(pref.getString("key_username",Name));
        pWord.setText(pref.getString("key_password",Pass));

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameVal = uName.getText().toString();
                passVal = pWord.getText().toString();

                if (isEmpty(uName) || isEmpty(pWord)) {
                    Toast.makeText(getBaseContext(), "Please enter values in both the fields", Toast.LENGTH_LONG).show();
                } else {
                    String getName = pref.getString("key_username", Name);
                    String getPass = pref.getString("key_password", Pass);

                    if(getName.equals(nameVal) && getPass.equals( passVal)) {
                        Toast.makeText(getBaseContext(), "Logged in successfully", Toast.LENGTH_LONG).show();
                        toNotes(view);
                        finish();
                    }
                    else{
                        Toast.makeText(getBaseContext(), "Wrong credentials entered", Toast.LENGTH_LONG).show();
                    }
                }
            }


        });
    }

    public void toNotes(View view){
        Intent tonotes;
        tonotes = new Intent(this,ClusterMain.class);
        startActivity(tonotes);
    }

    public void toSignUp(View view){
        Intent tosignup;
        tosignup = new Intent(this,SignUp.class);
        startActivity(tosignup);
    }
}

