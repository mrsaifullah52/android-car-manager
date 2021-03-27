package com.softtechglobal.androidcarmanager.UserManagement;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softtechglobal.androidcarmanager.Database.UserInfoDB;
import com.softtechglobal.androidcarmanager.R;

import java.io.IOException;

public class Profile extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    private FirebaseAuth firebaseAuth;

    EditText nameEt,phoneEt,genderEt,emailEt;
    TextView emailTv;
    Button updateBtn, btnPickImg, btnUploadImg;
    ImageView userDp;

    private int PICK_IMAGE=786;
    private int READ_PERMISSION=787;
    private Uri filePath;
    private String downloadPath;

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(Profile.this, Signin.class));
        }

        FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/profile" );
        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference("profiles/"+user.getUid());


//        initialize views
        nameEt=(EditText)findViewById(R.id.userNameEt);
        phoneEt=(EditText)findViewById(R.id.userPhoneEt);
        emailEt=(EditText)findViewById(R.id.userEmailEt);
//        emailTv=(TextView)findViewById(R.id.userEmailEt);
//        set Email from user object of Firebase
//        emailTv.setText(user.getEmail());
        updateBtn=(Button)findViewById(R.id.btnUpdate);
//        btnPickImg=(Button)findViewById(R.id.btnPickImg);
//        btnUploadImg=(Button)findViewById(R.id.btnUploadImg);
        userDp=(ImageView)findViewById(R.id.userDp);


        userDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(Profile.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    Intent i=new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select an Picture"), PICK_IMAGE);
                } else {
                    ActivityCompat.requestPermissions((Activity) Profile.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                }
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToDatabase();
            }
        });

//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
////                FirebaseUser user = firebaseAuth.getCurrentUser();
//
//                if(snapshot.exists()){
//                    if(snapshot.child("profile").exists()) {
////                        for (DataSnapshot ds:snapshot.child("profile").getChildren()){
//
//                            UserInfoDB userProfile = snapshot.child("profile").getValue(UserInfoDB.class);
//                            nameEt.setText(userProfile.getFullname());
//                            phoneEt.setText(userProfile.getPhone());
//                            emailEt.setText(userProfile.getEmail());
//                            if (!TextUtils.isEmpty(userProfile.getDpUrl())){
//                                Glide.with(Profile.this)
//                                        .load(userProfile.getDpUrl())
//                                        .into(userDp);
//                            }
////                            Log.d("snapshot.child", ds.getKey());
////                        }
//
//                        progressDialog.dismiss();
//
//
//                    }else{
//                        Log.d("snapshot.child","not exists");
//                    }
//                }else{
//                    progressDialog.dismiss();
//                    Log.d("snapshot","not exists");
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d("snapshot",error.toString());
//            }
//        });

        progressDialog = ProgressDialog.show(Profile.this, "", "Loading Profile...");
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    UserInfoDB userProfile = dataSnapshot.getValue(UserInfoDB.class);
                    nameEt.setText(userProfile.getFullname());
                    phoneEt.setText(userProfile.getPhone());
                    emailEt.setText(userProfile.getEmail());
                    if (!TextUtils.isEmpty(userProfile.getDpUrl())){
                        Glide.with(Profile.this)
                                .load(userProfile.getDpUrl())
                                .addListener(new RequestListener<Drawable>() {
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Profile.this, "Failed to load image, try again!", Toast.LENGTH_SHORT).show();
                                        return false;
                                    }
                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        progressDialog.dismiss();
                                        return false;
                                    }
                                })
                                .apply(RequestOptions.circleCropTransform())
                                .into(userDp);
                    }else{
                        progressDialog.dismiss();
                    }
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null){
            filePath=data.getData();
            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
//                userDp.setImageBitmap(bitmap);
                uploadToFirebase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public  void uploadToFirebase(){
        if (filePath != null){
            progressDialog = ProgressDialog.show(Profile.this, "", "Uploading...", true);
            storageReference.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                  another on success needed for downloadurl
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadPath = uri.toString();
                            Glide.with(Profile.this)
                                    .load(downloadPath)
                                    .addListener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Profile.this,"Failed to load image, try again!", Toast.LENGTH_SHORT).show();
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            progressDialog.dismiss();
                                            return false;
                                        }
                                    })
                                    .apply(RequestOptions.circleCropTransform())
                                    .into(userDp);
                            uploadToDatabase();
                        }
                    });
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                    Log.d("progress: ", snapshot.getBytesTransferred()+"/"+snapshot.getTotalByteCount());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e( "imageuploading: ", e.toString());
                }
            });
        }
    }

    public void uploadToDatabase(){
        String name=nameEt.getText().toString().trim();
        String phone=phoneEt.getText().toString().trim();
        String email=emailEt.getText().toString().trim();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(Profile.this,"Please Enter Name!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(phone)){
            Toast.makeText(Profile.this,"Please Enter Phone!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(email)){
            Toast.makeText(Profile.this,"Please Enter Email!",Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(downloadPath)){
            UserInfoDB userInfoDB =new UserInfoDB(name, email, phone);
            databaseReference.setValue(userInfoDB);
            Toast.makeText(getApplicationContext(),"User information updated", Toast.LENGTH_LONG).show();
        }else{
            UserInfoDB userInfoDB =new UserInfoDB(name, email, phone, downloadPath);
            databaseReference.setValue(userInfoDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"User information updated with Image", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    public void editTextBoxs(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setView(input);

        switch (view.getId()){
            case R.id.userNameEt:{
                alert.setTitle("Enter Name");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        nameEt.setText(input.getText());
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
            }break;
            case R.id.userPhoneEt:{
                alert.setTitle("Enter Phone");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        phoneEt.setText(input.getText());
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
            }break;
            case R.id.userEmailEt:{
                Toast.makeText(Profile.this, "Email Cant be changed", Toast.LENGTH_SHORT).show();
            }break;
        }
    }
}