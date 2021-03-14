package com.softtechglobal.androidcarmanager.usermanagement;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.softtechglobal.androidcarmanager.R;

public class Signup extends AppCompatActivity {

    ProgressBar progressBar2;
    private FirebaseAuth auth;
    Button btnSignUp;
    EditText emailEt,passwordEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//        changing title of activity
        setTitle("Sign Up");

//      Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
//      initialization
        progressBar2=(ProgressBar) findViewById(R.id.progressBar2);
        btnSignUp=(Button) findViewById(R.id.btnSignUp);
        emailEt=(EditText) findViewById(R.id.userEmail);
        passwordEt=(EditText) findViewById(R.id.userPassword);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter Password!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                progressBar2.setVisibility(View.VISIBLE);
                auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar2.setVisibility(View.GONE);
                        Toast.makeText(Signup.this,"User Successfully Registered.",Toast.LENGTH_SHORT).show();
                        if(!task.isSuccessful()){
                            Toast.makeText(Signup.this,"Registration failed.",Toast.LENGTH_SHORT).show();
                        }else{
//                            Intent i = new Intent(Signup.this,Signin.class);
//                            startActivity(i);
                            finish();
                        }

                    }
                });


                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar2.setVisibility(View.GONE);
    }
}
