package com.softtechglobal.androidcarmanager.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseUser;
import com.softtechglobal.androidcarmanager.MainActivity;
import com.softtechglobal.androidcarmanager.R;

public class Signin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText emailEt, passwordEt;
    Button SigninBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      initialize and check firebase
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(Signin.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_signin);
        setTitle("Sign In");
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
        emailEt=(EditText)findViewById(R.id.email);
        passwordEt=(EditText)findViewById(R.id.pass);
        SigninBtn=(Button)findViewById(R.id.btnSignIn);
        mAuth = FirebaseAuth.getInstance();

        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressBar.setVisibility(View.GONE);
                            if (!task.isSuccessful()) {
                                // there was an error
                                if (password.length() < 6) {
                                    passwordEt.setError("Please Enter Corrent Passowrd.");
                                } else {
                                    Toast.makeText(Signin.this, "Authentication Failed", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Intent intent = new Intent(Signin.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
//        Intent i = new Intent(Signin.this, MainActivity.class);
//        i.putExtra("token",currentUser);
//        startActivity(i);
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
