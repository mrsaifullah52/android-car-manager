package com.softtechglobal.androidcarmanager.capture;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

public class Capture extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ImageView imageView;
    Button captureImageBtn;
    Spinner spinner;
    String[] service={"Maintenance","Fuel","Purchase","Service","Engine Tunning","Fine","Tax"};
    private static final int CAMERA_REQUEST = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        setTitle("Upload Image");


        imageView=(ImageView)findViewById(R.id.imgView);
        spinner=(Spinner)findViewById(R.id.spinner);
        captureImageBtn=(Button) findViewById(R.id.captureImageBtn);
        spinner.setOnItemSelectedListener(this);

        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,service);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });

        capture();
    }

    public  void capture(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo=(Bitmap)data.getExtras().get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(photo);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
