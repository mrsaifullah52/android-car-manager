package com.softtechglobal.androidcarmanager.compute;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

public class Compute extends AppCompatActivity {

    TextView costTotal,costKM,costMile;
    TextView distanceTotal,distanceKM,distanceMile;
    TextView fuelTotal,fuelKM,fuelMile;

    double costTotalVal=0, costKMVal=0, costMileVal=0;
    double distanceTotalVal=0,distanceKMVal=0,distanceMileVal=0;
    double fuelTotalVal=0,fuelKMVal=0,fuelMileVal=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute);
        setTitle("Consumption");

//      initialize all textviews
        costTotal=(TextView)findViewById(R.id.costTotal);
        costKM=(TextView)findViewById(R.id.costKM);
        costMile=(TextView)findViewById(R.id.costMile);

        distanceTotal=(TextView)findViewById(R.id.distanceTotal);
        distanceKM=(TextView)findViewById(R.id.distanceKM);
        distanceMile=(TextView)findViewById(R.id.distanceMile);

        fuelTotal=(TextView)findViewById(R.id.fuelTotal);
        fuelKM=(TextView)findViewById(R.id.fuelKM);
        fuelMile=(TextView)findViewById(R.id.fuelMile);

//      calculate consumption
        costTotalVal=3928.02;
        costKMVal=785.71;
        costMileVal=12.36;

        distanceTotalVal=500;
        distanceKMVal=100;
        distanceMileVal=312.5;

        fuelTotalVal=35.71;
        fuelKMVal=7.14;
        fuelMileVal=4.46;

//      set values to display
        costTotal.setText("RS: "+costTotalVal);
        costKM.setText("RS: "+costKMVal);
        costMile.setText("RS: "+costMileVal);

        distanceTotal.setText(distanceTotalVal+" km");
        distanceKM.setText(distanceKMVal+" km");
        distanceMile.setText(distanceMileVal+" km");

        fuelTotal.setText(fuelTotalVal+" Ltr");
        fuelKM.setText(fuelKMVal+" Ltr");
        fuelMile.setText(fuelMileVal+" Ltr");

    }
}
