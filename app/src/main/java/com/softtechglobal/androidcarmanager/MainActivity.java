package com.softtechglobal.androidcarmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.card.MaterialCardView;
import com.softtechglobal.androidcarmanager.usermanagement.Signin;
import com.softtechglobal.androidcarmanager.usermanagement.Signup;


public class MainActivity extends AppCompatActivity {

    MaterialCardView cardView1,cardView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cardView1=(MaterialCardView)findViewById(R.id.card1);
        cardView2=(MaterialCardView)findViewById(R.id.card2);

        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Signin.class);
                startActivity(i);
            }
        });

        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this, Signup.class);
                startActivity(i);
            }
        });
    }
}
