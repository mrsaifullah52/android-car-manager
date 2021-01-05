package com.softtechglobal.androidcarmanager.usermanagement;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//        changing title of activity
        setTitle("Sign Up");
    }
}
