package com.softtechglobal.androidcarmanager.UserManagement;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.royrodriguez.transitionbutton.TransitionButton;
import com.softtechglobal.androidcarmanager.MainActivity;
import com.softtechglobal.androidcarmanager.R;

public class Signin extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText emailEt, passwordEt;
//    Button SigninBtn;
    TransitionButton SigninBtn;

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
        SigninBtn=(TransitionButton)findViewById(R.id.btnSignIn);
        mAuth = FirebaseAuth.getInstance();

        SigninBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SigninBtn.startAnimation();

//                progressDialog= ProgressDialog.show(Signin.this, "","Please Wait, Authenticating...",true);

                String email = emailEt.getText().toString();
                final String password = passwordEt.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    SigninBtn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
//                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                    return;
                }else if (TextUtils.isEmpty(password)) {
//                    progressDialog.dismiss();
                    SigninBtn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {

//                            progressDialog.dismiss();
//                            Intent intent = new Intent(Signin.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();


                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    boolean isSuccessful = true;
                                    // Choose a stop animation if your call was succesful or not
                                    if (isSuccessful) {
                                        SigninBtn.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                                            @Override
                                            public void onAnimationStopEnd() {
                                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                                startActivity(intent);
                                            }
                                        });
                                    } else {
                                        SigninBtn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                                    }
                                }
                            }, 1000);



                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // there was an error
                            if (password.length() < 6) {
                                passwordEt.setError("Password must have 6 Characters.");
//                                progressDialog.dismiss();
                                SigninBtn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                            } else {
                                SigninBtn.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
//                                progressDialog.dismiss();
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
        FirebaseUser currentUser = mAuth.getCurrentUser();
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
