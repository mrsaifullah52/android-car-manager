package com.softtechglobal.androidcarmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.UserManagement.Signin;

public class Splash extends AppCompatActivity {

//    LinearLayout llayout;
//    Animation animFadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                llayout.setVisibility(View.INVISIBLE);
                Intent i = new Intent(getApplicationContext(), Signin.class);
                startActivity(i);
                finish();
            }
        },4*900);



//        Thread background = new Thread(){
//            public void run(){
//                try {
//                    llayout.startAnimation(animFadeOut);
//                    sleep(4*1000);
//                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
//                    startActivity(i);
//                    finish();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//        background.start();
    }


}
