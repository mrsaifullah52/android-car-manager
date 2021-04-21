package com.softtechglobal.androidcarmanager.Capture;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Database.MyGallery;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;
import com.softtechglobal.androidcarmanager.Views.BaseAdapterForGallery;
import com.softtechglobal.androidcarmanager.Views.ModelForGallery;

import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    GridView gridview;
    ArrayList<String> urls = new ArrayList<String>();

    ModelForGallery modelForGallery;
    ArrayList<ModelForGallery> modelList=new ArrayList<ModelForGallery>();
    BaseAdapterForGallery adapterForGallery;
    ImageButton addImage;

    private DatabaseReference databaseReference1;
    private FirebaseAuth firebaseAuth;

    private int READ_PERMISSION=787;
    private int CAMERA_PERMISSION=789;
    private int WRITE_PERMISSION=790;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        getSupportActionBar().hide();

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Gallery.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference1= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/gallery");

        gridview=(GridView) findViewById(R.id.gridview);
        addImage=(ImageButton) findViewById(R.id.capture);

        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Gallery.this, Capture.class);

//              ask for permission if haven't
                if (ContextCompat.checkSelfPermission(Gallery.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Gallery.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION);

                }else
                    if (ContextCompat.checkSelfPermission(Gallery.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Gallery.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                }else{
                    if (ContextCompat.checkSelfPermission(Gallery.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Gallery.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_PERMISSION);
                    }else{
                        startActivity(i);
                    }
                }
            }
        });

        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {

                String key;
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    MyGallery myGallery=ds.getValue(MyGallery.class);
                    Log.d("myGallery", myGallery.getUrl());
                    urls.add(myGallery.getUrl());
                }
                setArrayValue();
            }
        });


    }

    public void setArrayValue(){
        for (int i=0; i<urls.size();i++){
            modelForGallery=new ModelForGallery (urls.get(i));
            modelList.add(modelForGallery);
        }
        adapterForGallery=new BaseAdapterForGallery(Gallery.this, modelList);
        gridview.setAdapter(adapterForGallery);
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(Gallery.this, ViewImage.class);
                i.putExtra ( "imageurl", urls.get(position) );
                startActivity(i);
            }
        });
    }
}