package com.softtechglobal.androidcarmanager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.add.AddNotes;
import com.softtechglobal.androidcarmanager.add.AddReminder;
import com.softtechglobal.androidcarmanager.capture.Capture;
import com.softtechglobal.androidcarmanager.compute.Compute;
import com.softtechglobal.androidcarmanager.expenses.AddExpenses;
import com.softtechglobal.androidcarmanager.statistics.Statistics;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
    public void moveToNextActivity(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.card1:{
                intent = new Intent(MainActivity.this, AddExpenses.class);
                startActivity(intent);
            }break;
            case R.id.card2:{
                intent = new Intent(MainActivity.this, Capture.class);
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
