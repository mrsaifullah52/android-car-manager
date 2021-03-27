package com.softtechglobal.androidcarmanager.UserManagement;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Database.UserInfoDB;
import com.softtechglobal.androidcarmanager.R;

public class Signup extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    Button btnSignUp;
    EditText nameEt,emailEt,userPhoneEt,passwordEt,confirmPEt;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
//        changing title of activity
        setTitle("Sign Up");

//      Get Firebase auth instance
        auth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("users");
//      initialization
        btnSignUp=(Button) findViewById(R.id.btnSignUp);
        nameEt=(EditText) findViewById(R.id.userName);
        emailEt=(EditText) findViewById(R.id.userEmail);
        userPhoneEt=(EditText) findViewById(R.id.userPhone);
        passwordEt=(EditText) findViewById(R.id.userPassword);
        confirmPEt=(EditText) findViewById(R.id.confirmPassword);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = nameEt.getText().toString().trim();
                final String email = emailEt.getText().toString().trim();
                final String phone = userPhoneEt.getText().toString().trim();
                String password = passwordEt.getText().toString().trim();
                String confirmPassword = confirmPEt.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(Signup.this, "Enter Name!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Signup.this, "Enter Email address!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(Signup.this, "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Signup.this, "Enter Password!", Toast.LENGTH_SHORT).show();
                }else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(Signup.this, "Enter Confirm Password!", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirmPassword)) {
                    Toast.makeText(Signup.this, "Confirm Password doesn't matched!", Toast.LENGTH_SHORT).show();
                }else if (password.length() < 6) {
                    Toast.makeText(Signup.this, "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                }else{
                    progressDialog=ProgressDialog.show(Signup.this, "","Signing you Up, please wait...");
                    auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            UserInfoDB userInfoDB=new UserInfoDB(name, email, phone);
//                          store information in realtime database
                            databaseReference.child(authResult.getUser().getUid()).child("profile").setValue(userInfoDB)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            emptyEt();
                                            progressDialog.dismiss();
                                            Toast.makeText(Signup.this,"User Successfully Registered.",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Signup.this,"Registration failed. try again",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public void emptyEt(){
        nameEt.setText("");
        emailEt.setText("");
        userPhoneEt.setText("");
        passwordEt.setText("");
        confirmPEt.setText("");
    }
}
