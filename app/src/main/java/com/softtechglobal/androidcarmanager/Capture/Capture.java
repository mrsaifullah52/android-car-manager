package com.softtechglobal.androidcarmanager.Capture;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softtechglobal.androidcarmanager.Database.MyGallery;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;

public class Capture extends AppCompatActivity {
    ImageView imageView;
    Button captureImageBtn, uploadImageBtn;
    private static final int CAMERA_REQUEST = 0;

    private Uri filePath;
    String downloadLink;
    String key;
    int expensesIndex;
    ArrayList<Uri> pathList = new ArrayList<Uri>();
    private DatabaseReference databaseReference1;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        setTitle("Upload Image");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Capture.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");
        databaseReference1= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/gallery/");

        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference("gallery/"+user.getUid());


        imageView=(ImageView)findViewById(R.id.imgView);
        captureImageBtn=(Button) findViewById(R.id.captureImageBtn);
        uploadImageBtn=(Button) findViewById(R.id.uploadImage);

        captureImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Capture.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Capture.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 790);
                    capture();
                }

            }
        });

        uploadImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(key.equals("-1")) {
                    Toast.makeText(Capture.this, "Please Select a Car Before Adding Expense!", Toast.LENGTH_LONG).show();
                }else{
                        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                        expensesIndex = Integer.parseInt(ds.getKey());
                                        Log.d("expensesIndex1", String.valueOf(expensesIndex));
                                    }
                                    expensesIndex++;
                                    Log.d("expensesIndex2", String.valueOf(expensesIndex));
                                    addIntoFirebase(expensesIndex);
                                }else{
                                    Log.d("expensesIndex3", String.valueOf(expensesIndex));
                                    expensesIndex=0;
                                    addIntoFirebase(expensesIndex);
                                }
                            }
                        });
                }
            }
        });
            capture();

    }

    public void addIntoFirebase(int i){
        final int index=i;
        if(pathList.get(0) != null) {
            storageReference
                    .child(String.valueOf(index))
                    .putFile(pathList.get(0))
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference
                                    .child(String.valueOf(index))
                                    .getDownloadUrl()
                                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            downloadLink = String.valueOf(uri);
                                            uploadIntoDb(downloadLink, index);
                                        }
                                    });
                        }
                    });
        }else{
            Toast.makeText(Capture.this, "File Path not set!!", Toast.LENGTH_LONG).show();
        }
    }

    public void uploadIntoDb(String url, int i){
        MyGallery myGallery=new MyGallery(url);
        databaseReference1.child(String.valueOf(i)).setValue(myGallery).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Capture.this, "Image Uploaded", Toast.LENGTH_LONG).show();
            }
        });
    }

    public  void capture(){
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            filePath= data.getData();
            pathList.add(filePath);
            Bitmap photo=(Bitmap)data.getExtras().get("data");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(photo);
        }
    }
}
