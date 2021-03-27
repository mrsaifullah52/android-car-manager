package com.softtechglobal.androidcarmanager.UserManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.softtechglobal.androidcarmanager.MainActivity;
import com.softtechglobal.androidcarmanager.R;

public class Signin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailEt, passwordEt;
    Button SigninBtn;

    ProgressDialog progressDialog;
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
        emailEt=(EditText)findViewById(R.id.email);
        passwordEt=(EditText)findViewById(R.id.pass);
        SigninBtn=(Button)findViewById(R.id.btnSignIn);
        mAuth = FirebaseAuth.getInstance();

        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog= ProgressDialog.show(Signin.this, "","Please Wait, Authenticating...",true);

                String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(password)) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            progressDialog.dismiss();
                            Intent intent = new Intent(Signin.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // there was an error
                            if (password.length() < 6) {
                                passwordEt.setError("Password must have 6 Characters.");
                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(Signin.this, "Email or Password not Matched.", Toast.LENGTH_LONG).show();
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

    @Override
    public void onBackPressed() {
        progressDialog.dismiss();
        super.onBackPressed();
    }
}
