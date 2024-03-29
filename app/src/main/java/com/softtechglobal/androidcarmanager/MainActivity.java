package com.softtechglobal.androidcarmanager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.softtechglobal.androidcarmanager.Capture.Gallery;
import com.softtechglobal.androidcarmanager.Expenses.AddExpenses;
import com.softtechglobal.androidcarmanager.Statistics.Statistics;
import com.softtechglobal.androidcarmanager.UserManagement.Profile;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;
import com.softtechglobal.androidcarmanager.Vehicles.Vehicles;
import com.softtechglobal.androidcarmanager.add.Notes;
import com.softtechglobal.androidcarmanager.add.Reminder;
import com.softtechglobal.androidcarmanager.Compute.Compute;

import java.io.File;


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

    @Override
    protected void onDestroy() {
        super.onDestroy();
//      Deleting Cache memory so we can get updated values each time
        deleteCache(MainActivity.this);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

}
