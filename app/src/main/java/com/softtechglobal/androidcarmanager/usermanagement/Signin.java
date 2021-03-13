package com.softtechglobal.androidcarmanager.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softtechglobal.androidcarmanager.MainActivity;
import com.softtechglobal.androidcarmanager.R;

public class Signin extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        setTitle("Sign In");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Intent i = new Intent(Signin.this, MainActivity.class);
        i.putExtra("token",currentUser);
        startActivity(i);
    }

    public void forgotPassword(View v){
        Intent i = new Intent(Signin.this,Forgotpassword.class);
        startActivity(i);
    }
    public void signIn(View v){
        Intent i = new Intent(Signin.this, MainActivity.class);
        startActivity(i);
        Log.i("Sign In","Signed In Successfully.");
    }
    public void signUp(View v){
        Intent i = new Intent(Signin.this,Signup.class);
        startActivity(i);
    }
}
