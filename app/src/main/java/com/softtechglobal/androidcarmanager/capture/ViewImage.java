package com.softtechglobal.androidcarmanager.capture;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.softtechglobal.androidcarmanager.R;

public class ViewImage extends AppCompatActivity {
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        imageView=(ImageView) findViewById(R.id.viewimage);

        Bundle intent=getIntent().getExtras();
        String url=intent.getString("imageurl");
        Glide.with(ViewImage.this).load(url).into(imageView);
    }
}