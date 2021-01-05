package com.softtechglobal.androidcarmanager.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

public class Signin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
//        changing title of activity
        setTitle("Sign In");


    }

    public void forgotPassword(View v){
        Intent i = new Intent(Signin.this,Forgotpassword.class);
        startActivity(i);
    }
    public void signIn(View v){
        Log.i("Sign In","Signed In Successfully.");
    }
    public void signUp(View v){
        Intent i = new Intent(Signin.this,Signup.class);
        startActivity(i);
    }
}
