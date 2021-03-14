package com.softtechglobal.androidcarmanager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.softtechglobal.androidcarmanager.add.AddNotes;
import com.softtechglobal.androidcarmanager.add.AddReminder;
import com.softtechglobal.androidcarmanager.capture.Capture;
import com.softtechglobal.androidcarmanager.compute.Compute;
import com.softtechglobal.androidcarmanager.expenses.AddExpenses;
import com.softtechglobal.androidcarmanager.statistics.Statistics;
import com.softtechglobal.androidcarmanager.usermanagement.Profile;
import com.softtechglobal.androidcarmanager.usermanagement.Signin;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Dashboard");
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.profile: {
                Intent i = new Intent(MainActivity.this, Profile.class);
                i.putExtra("type","create");
                i.putExtra("type","update");
                startActivity(i);
            }break;
            case R.id.logout:{
                mAuth.signOut();
//              this listener will be called when there is change in firebase user session
//                FirebaseAuth.AuthStateListener authListener = new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                        FirebaseUser user = firebaseAuth.getCurrentUser();
//                        if (firebaseAuth.getCurrentUser() == null) {
                            // user auth state is changed - user is null
                            // launch login activity
                            startActivity(new Intent(MainActivity.this, Signin.class));
                            finish();
//                        }
//                    }
//                };


            }break;
        }
        return(super.onOptionsItemSelected(item));
    }

    public void moveToNextActivity(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.card1:{
                intent = new Intent(MainActivity.this, AddExpenses.class);
                startActivity(intent);
            }break;
            case R.id.card2:{
//                ask for camera permission if haven't
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
                }else{
                    intent = new Intent(MainActivity.this, Capture.class);
                    startActivity(intent);
                }
            }break;
            case R.id.card3:{
                intent = new Intent(MainActivity.this, Statistics.class);
                startActivity(intent);
            }break;
            case R.id.card4:{
                intent = new Intent(MainActivity.this, Compute.class);
                startActivity(intent);
            }break;
            case R.id.card5:{
                intent = new Intent(MainActivity.this, AddNotes.class);
                startActivity(intent);
            }break;
            case R.id.card6:{
                intent = new Intent(MainActivity.this, AddReminder.class);
                startActivity(intent);
            }break;
            default:{
                Log.d("error: ","Next Activity not Specified");
            }
        }
    }
}
