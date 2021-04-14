package com.softtechglobal.androidcarmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.softtechglobal.androidcarmanager.Expenses.AddExpenses;
import com.softtechglobal.androidcarmanager.Statistics.Statistics;
import com.softtechglobal.androidcarmanager.UserManagement.Profile;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;
import com.softtechglobal.androidcarmanager.Vehicles.Vehicles;
import com.softtechglobal.androidcarmanager.add.Notes;
import com.softtechglobal.androidcarmanager.add.Reminder;
import com.softtechglobal.androidcarmanager.capture.Gallery;
import com.softtechglobal.androidcarmanager.compute.Compute;


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
//    int vehicleId;
    Boolean isFirstRun;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//      initializatin of firebaseauth
        mAuth = FirebaseAuth.getInstance();

//      show vehicles list only first time
        isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            setTitle("Dashboard");
            startActivity(new Intent(MainActivity.this, Vehicles.class));
        }else{
//          vehicle name will be dynamic
            setTitle("Dashboard"+"("+
                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                            .getString("vehicle","Nothing Selected")
                    +")");
//          selected vehicle id
//            vehicleId = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
//                    .getInt("vehicleId", 0);
        }

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
            case R.id.vehicle:{
                Intent i = new Intent(MainActivity.this, Vehicles.class);
                startActivity(i);
            }break;
            case R.id.logout:{
                mAuth.signOut();
                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                        .edit()
                        .putString("vehicle", "Nothing Selected")
                        .putString("key", "null")
                        .putBoolean("isFirstRun", true)
                        .commit();
                startActivity(new Intent(MainActivity.this, Signin.class));
                finish();
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

                intent = new Intent(MainActivity.this, Gallery.class);
                startActivity(intent);


//                ask for camera permission if haven't
//                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//                        == PackageManager.PERMISSION_DENIED){
//                    ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, 100);
//                }else{
//                    intent = new Intent(MainActivity.this, Capture.class);
//                    startActivity(intent);
//                }
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
                intent = new Intent(MainActivity.this, Notes.class);
                startActivity(intent);
            }break;
            case R.id.card6:{
                intent = new Intent(MainActivity.this, Reminder.class);
                startActivity(intent);
            }break;
            default:{
                Log.d("error: ","Next Activity not Specified");
            }
        }
    }
}
