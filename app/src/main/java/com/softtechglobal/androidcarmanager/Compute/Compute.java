package com.softtechglobal.androidcarmanager.Compute;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Database.ExpensesDB;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Compute extends AppCompatActivity {

    TextView lastOdometerEt, distanceEt, fillupsEt, fuelCostEt, costKmEt, kmRouteEt, consumedFuelEt, avgKmLtrEt;

    Double odometer=0.0, distance=0.0, fillups=0.0, fuelcost=0.0, avgkm=0.0, avgkmcost=0.0, consumedFuel=0.0, avgkmltr=0.0;

    private DatabaseReference databaseReference1,databaseReference2;
    private FirebaseAuth firebaseAuth;
    String key;
    ArrayList<Double> costList=new ArrayList<Double>();
    ArrayList<Double> distanceList=new ArrayList<Double>();
    ArrayList<Double> fuellist=new ArrayList<Double>();

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compute);
        setTitle("Consumption");

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Compute.this, Signin.class));
        }
        progressDialog= ProgressDialog.show(Compute.this, "","Please Wait, Loading...",true);

        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/expenses/"+key);
        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid());
//      initialize all textviews
        lastOdometerEt=(TextView)findViewById(R.id.lastOdometerEt);
        distanceEt=(TextView)findViewById(R.id.distanceEt);
        fillupsEt=(TextView)findViewById(R.id.fillupsEt);
        fuelCostEt=(TextView)findViewById(R.id.fuelCostEt);
        kmRouteEt=(TextView)findViewById(R.id.kmRouteEt);
        costKmEt=(TextView)findViewById(R.id.costKmEt);
        consumedFuelEt=(TextView)findViewById(R.id.consumedFuelEt);
        avgKmLtrEt=(TextView)findViewById(R.id.avgKmLtrEt);

        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    Date date=new Date();
                    Calendar calendar=Calendar.getInstance();
                    calendar.setTimeInMillis(date.getTime());
                    calendar.add(Calendar.MONTH, -1);
                    Long duration=calendar.getTimeInMillis();

                    for (DataSnapshot ds: dataSnapshot.child("Fuel").getChildren()){
                        ExpensesDB expensesDB= ds.getValue(ExpensesDB.class);
                        if(expensesDB.getDate() >= duration){
                            costList.add(expensesDB.getCost());
                            distanceList.add(expensesDB.getOdometer());
                            fuellist.add(expensesDB.getLtr());
                        }
                    }

                    if(dataSnapshot.child("Oddometer").exists()){
                        for(DataSnapshot ds:dataSnapshot.child("Oddometer").getChildren()){
                            ExpensesDB expensesDB= ds.getValue(ExpensesDB.class);
                            if(expensesDB.getDate() >= duration) {
                                distanceList.add(expensesDB.getOdometer());
                            }
                        }
                    }

                    if(!costList.isEmpty() && !distanceList.isEmpty() && !fuellist.isEmpty()){
                        calculateDbValues();
                    }else{
                        Toast.makeText(Compute.this, "No Records Found for last Month!!",Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Compute.this, "Please add some data Before checking Consumptions.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void calculateDbValues(){
        databaseReference2.child("vehicles").child(key).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                Collections.sort(distanceList);
                Double lastVal=0.0, newVal=0.0;;
                Boolean isFirst=true;
                for (Double val:distanceList){
                    if(isFirst){
                        isFirst=false;
                        lastVal=val;
                    }
                    newVal=val;
                }
                odometer=newVal;
                distance=-1*(lastVal-newVal);

                Double fuelVal=0.0;
                Collections.sort(fuellist);
                for (Double val:fuellist){
                    fuelVal=fuelVal+val;
                    Log.d("fuellist", String.valueOf(val));
                }
                fillups= Double.valueOf(fuellist.size());
                consumedFuel=fuelVal;

                Collections.sort(costList);
                for (Double val:costList){
                    fuelcost = val+fuelcost;
                    Log.d("costlist", String.valueOf(val));
                }

                avgkm=(distance/distanceList.size());
                avgkmltr=(fuelcost/distance);
                avgkmcost=(fuelcost/consumedFuel)/avgkmltr;

                setValues();
            }
        });

    }

    public void setValues(){
        lastOdometerEt.setText(new DecimalFormat("##.##").format(odometer)+" km");
        distanceEt.setText(new DecimalFormat("##.##").format(distance)+" km");
        fillupsEt.setText(new DecimalFormat("##.##").format(fillups)+" Time");
        fuelCostEt.setText("Rs: "+new DecimalFormat("##.##").format(fuelcost));
        kmRouteEt.setText(new DecimalFormat("##.##").format(avgkm)+" km");
        costKmEt.setText("Rs: "+new DecimalFormat("##.##").format(avgkmcost));
        consumedFuelEt.setText(new DecimalFormat("##.##").format(consumedFuel)+" Ltr");
        avgKmLtrEt.setText(new DecimalFormat("##.##").format(avgkmltr)+" KM");

        progressDialog.dismiss();
    }
}
