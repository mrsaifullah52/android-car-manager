package com.softtechglobal.androidcarmanager.UserManagement;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.softtechglobal.androidcarmanager.R;

public class Forgotpassword extends AppCompatActivity {

//    EditText etAns1, etPass, etConfirmPass;
//    LinearLayout questionsLayout, resetLayout;

    EditText resetEmailEt;
    Button resetBtn;
    private FirebaseAuth mAuth;
    ProgressBar progressBar3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
//        changing title of activity
        setTitle("Reset Password");

//        initializing Values
//        etAns1=(EditText) findViewById(R.id.ans1);
//        etPass=(EditText) findViewById(R.id.password);
//        etConfirmPass=(EditText) findViewById(R.id.confrimPassword);
//
//        questionsLayout=(LinearLayout) findViewById(R.id.questionLayout);
//        resetLayout=(LinearLayout) findViewById(R.id.resetLayout);

        resetEmailEt=(EditText)findViewById(R.id.resetEmailEt);
        resetBtn=(Button)findViewById(R.id.btnForgotPassword);
        progressBar3=(ProgressBar)findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmailEt.getText().toString().trim();
                progressBar3.setVisibility(View.VISIBLE);
                mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(Forgotpassword.this, "We have sent you instructions to reset your password on your Email!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Forgotpassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                        }
                        progressBar3.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

//    public  void submitAns(View v){
//        String ans=etAns1.getText().toString();
//        if(ans.equals("chick")){
//            Log.d("answer",etAns1.getText().toString());
//            questionsLayout.setVisibility(View.GONE);
//            resetLayout.setVisibility(View.VISIBLE);
//        }else{
//            Toast.makeText(Forgotpassword.this,"You gave wrong Ans, Try Again.",Toast.LENGTH_SHORT).show();
//            Log.d("answer",etAns1.getText().toString());
//        }
//    }
//
//    public  void resetPass(View v){
//        String pass = etPass.getText().toString();
//        String confPass = etConfirmPass.getText().toString();
//
//        if(pass.equals(confPass)){
//            Intent i= new Intent(Forgotpassword.this, Signin.class);
//            startActivity(i);
//            Toast.makeText(Forgotpassword.this,"Your Password Successfully Reset.",Toast.LENGTH_SHORT).show();
//        }else{
//            Toast.makeText(Forgotpassword.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
//        }
//    }
}
