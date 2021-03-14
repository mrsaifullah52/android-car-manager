package com.softtechglobal.androidcarmanager.usermanagement;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softtechglobal.androidcarmanager.R;

public class Profile extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    EditText nameEt,phoneEt,genderEt;
    TextView emailTv;
    Button updateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(Profile.this,Signin.class));
        }

        databaseReference= FirebaseDatabase.getInstance().getReference("users");
        FirebaseUser user=firebaseAuth.getCurrentUser();

//        initialize views
        nameEt=(EditText)findViewById(R.id.userName);
        phoneEt=(EditText)findViewById(R.id.userPhone);
        genderEt=(EditText)findViewById(R.id.userGender);
        emailTv=(TextView)findViewById(R.id.userEmailTv);
//        set Email from user object of Firebase
        emailTv.setText(user.getEmail());
        updateBtn=(Button)findViewById(R.id.btnUpdate);


        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name=nameEt.getText().toString().trim();
                String phone=phoneEt.getText().toString().trim();
                String gender=genderEt.getText().toString().trim();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(Profile.this,"Please Enter Name!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(phone)){
                    Toast.makeText(Profile.this,"Please Enter Phone!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(gender)){
                    Toast.makeText(Profile.this,"Please Enter Gender!",Toast.LENGTH_SHORT).show();
                }else{
                    UserInfo userInfo=new UserInfo(name,gender,phone);
                    FirebaseUser user = firebaseAuth.getCurrentUser();
                    databaseReference.child(user.getUid()).setValue(userInfo);
                    Toast.makeText(getApplicationContext(),"User information updated", Toast.LENGTH_LONG).show();
                }
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(snapshot.hasChild(user.getUid())){UserInfo userProfile = snapshot.child(user.getUid()).getValue(UserInfo.class);
                    nameEt.setText(userProfile.getFullname());
                    phoneEt.setText(userProfile.getPhone());
                    genderEt.setText(userProfile.getGender());
                }else{
                    Log.d("snapshot","not exists");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("snapshot",error.toString());
            }
        });
    }


    public void editTextBoxs(View view){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        alert.setView(input);

        switch (view.getId()){
            case R.id.userName:{
                alert.setTitle("Enter Name");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        nameEt.setText(input.getText());
                    }
                });
            }break;
            case R.id.userPhone:{
                alert.setTitle("Enter Phone");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        phoneEt.setText(input.getText());
                    }
                });
            }break;
            case R.id.userGender:{
                alert.setTitle("Enter Gender");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        genderEt.setText(input.getText());
                    }
                });
            }break;
        }

//      alert.setMessage("Message");
//      Set an EditText view to get user input

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

//    private void UserInformation(){
//        String name=nameEt.getText().toString().trim();
//        String phone=phoneEt.getText().toString().trim();
//        String gender=genderEt.getText().toString().trim();
//
//        UserInfo userInfo=new UserInfo(name,gender,phone);
//        FirebaseUser user = firebaseAuth.getCurrentUser();
//        databaseReference.child(user.getUid()).setValue(userInfo);
//        Toast.makeText(getApplicationContext(),"User information updated", Toast.LENGTH_LONG).show();
//    }
}