package com.softtechglobal.androidcarmanager.usermanagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

public class Forgotpassword extends AppCompatActivity {

    EditText etAns1, etPass,etConfirmPass;
    LinearLayout questionsLayout,resetLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
//        changing title of activity
        setTitle("Reset Password");

//        initializing Values
        etAns1=(EditText) findViewById(R.id.ans1);
        etPass=(EditText) findViewById(R.id.password);
        etConfirmPass=(EditText) findViewById(R.id.confrimPassword);

        questionsLayout=(LinearLayout) findViewById(R.id.questionLayout);
        resetLayout=(LinearLayout) findViewById(R.id.resetLayout);

    }

    public  void submitAns(View v){
        String ans=etAns1.getText().toString();
        if(ans.equals("chick")){
            Log.d("answer",etAns1.getText().toString());
            questionsLayout.setVisibility(View.GONE);
            resetLayout.setVisibility(View.VISIBLE);
        }else{
            Toast.makeText(Forgotpassword.this,"You gave wrong Ans, Try Again.",Toast.LENGTH_SHORT).show();
            Log.d("answer",etAns1.getText().toString());
        }
    }

    public  void resetPass(View v){
        String pass = etPass.getText().toString();
        String confPass = etConfirmPass.getText().toString();

        if(pass.equals(confPass)){
            Intent i= new Intent(Forgotpassword.this, Signin.class);
            startActivity(i);
            Toast.makeText(Forgotpassword.this,"Your Password Successfully Reset.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(Forgotpassword.this,"Password Mismatch",Toast.LENGTH_SHORT).show();
        }
    }
}
