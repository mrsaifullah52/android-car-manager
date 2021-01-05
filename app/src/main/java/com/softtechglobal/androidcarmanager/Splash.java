package com.softtechglobal.androidcarmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Splash extends AppCompatActivity {

    LinearLayout llayout;
    Animation animFadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        llayout = findViewById(R.id.linearSplash);
        animFadeOut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        llayout.setVisibility(View.VISIBLE);

        llayout.startAnimation(animFadeOut);


        getSupportActionBar().hide();

        llayout.startAnimation(animFadeOut);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                llayout.setVisibility(View.INVISIBLE);

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
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
