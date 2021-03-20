package com.softtechglobal.androidcarmanager.Statistics;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.softtechglobal.androidcarmanager.Database.ExpensesDB;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Statistics extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    BarChart barChart;
    BarDataSet barDataSet1, barDataSet2, barDataSet3, barDataSet4, barDataSet5, barDataSet6;
    BarData barData;
    ArrayList<BarEntry> barEntries1, barEntries2, barEntries3, barEntries4, barEntries5, barEntries6;
    ArrayList<String> barEntriesLabel = new ArrayList<String>();

    Spinner statisticsSpinner;
    private final String[] service = {"Last 7 Days", "Last 1 Month", "Last 12 Months"};
    int duration;

    //  graph values from firebase
    ArrayList<Double> maintenance = new ArrayList<Double>();
    ArrayList<Long> maintenanceDate = new ArrayList<Long>();

    ArrayList<Double> fuel = new ArrayList<Double>();
    ArrayList<Long> fuelDate = new ArrayList<Long>();

    ArrayList<Double> purchase = new ArrayList<Double>();
    ArrayList<Long> purchaseDate = new ArrayList<Long>();

    ArrayList<Double> services = new ArrayList<Double>();
    ArrayList<Long> servicesDate = new ArrayList<Long>();

    ArrayList<Double> fine = new ArrayList<Double>();
    ArrayList<Long> fineDate = new ArrayList<Long>();

    ArrayList<Double> tax = new ArrayList<Double>();
    ArrayList<Long> taxDate = new ArrayList<Long>();

    private DatabaseReference databaseReference1;
    private Query databaseReference2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    Long queryDate, todayDate;

    String vehicleId;

    Boolean isFirtTime1 = false;
    Boolean isFirtTime2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setTitle("View Statistics");

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Statistics.this, Signin.class));
        }
        vehicleId = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key", "-1");

//        +"/Maintenance"


        statisticsSpinner = (Spinner) findViewById(R.id.statisticsSpinner);
        statisticsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, service);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statisticsSpinner.setAdapter(adapter);

        barChart = findViewById(R.id.chart);
//        barChart.setDescription("Percent(%)");
//        barChart.getXAxis().setEnabled(true);
        YAxis rightYAxis = barChart.getAxisRight();
        rightYAxis.setEnabled(false);
        XAxis topYAxis = barChart.getXAxis();
        topYAxis.setEnabled(false);
        barChart.animateXY(2000, 2000);

        barEntries1 = new ArrayList<BarEntry>();
        barEntries2 = new ArrayList<BarEntry>();
        barEntries3 = new ArrayList<BarEntry>();
        barEntries4 = new ArrayList<BarEntry>();
        barEntries5 = new ArrayList<BarEntry>();
        barEntries6 = new ArrayList<BarEntry>();


        barEntries1.add(new BarEntry(1, 0));
        barEntries2.add(new BarEntry(2, 0));
        barEntries3.add(new BarEntry(3, 0));
        barEntries4.add(new BarEntry(4, 0));
        barEntries5.add(new BarEntry(5, 0));
        barEntries6.add(new BarEntry(6, 0));
        initializeBarGraph();

    }

//    private void getLabels(){
//        barEntriesLabel.add("Maintenance");
//        barEntriesLabel.add("Fuel");
//        barEntriesLabel.add("Purchase");
//        barEntriesLabel.add("Services");
////        barEntriesLabel.add("Engine Tuning");
//        barEntriesLabel.add("Fine");
//        barEntriesLabel.add("Tax");
//    }


    public void initializeBarGraph() {

        //      Removing previous data
//        if(isFirtTime2){
//            Log.d("isFirtTime","removing");
//
//            barDataSet1.removeEntry(0);
//            barDataSet2.removeEntry(0);
//            barDataSet3.removeEntry(0);
//            barDataSet4.removeEntry(0);
//            barDataSet5.removeEntry(0);
//            barDataSet6.removeEntry(0);
//
//
//            barData.removeDataSet(0);
//            barData.removeDataSet(1);
//            barData.removeDataSet(2);
//            barData.removeDataSet(3);
//            barData.removeDataSet(4);
//            barData.removeDataSet(5);
//            barChart.removeAllViews();
//        }


        //      setting labels
        barDataSet1 = new BarDataSet(barEntries1, "Maintenance");
        barDataSet2 = new BarDataSet(barEntries2, "Fuel");
        barDataSet3 = new BarDataSet(barEntries3, "Purchase");
        barDataSet4 = new BarDataSet(barEntries4, "Services");
        barDataSet5 = new BarDataSet(barEntries5, "Fine");
        barDataSet6 = new BarDataSet(barEntries6, "Tax");

//      setting colours
        barDataSet1.setColors(ColorTemplate.COLORFUL_COLORS);
        barDataSet2.setColors(R.color.bar2);
        barDataSet3.setColors(ColorTemplate.JOYFUL_COLORS);
        barDataSet4.setColors(ColorTemplate.LIBERTY_COLORS);
        barDataSet5.setColors(ColorTemplate.VORDIPLOM_COLORS);
        barDataSet5.setColors(ColorTemplate.PASTEL_COLORS);


//      setting text colour
        barDataSet1.setValueTextColor(Color.BLUE);
        barDataSet2.setValueTextColor(Color.BLUE);
        barDataSet3.setValueTextColor(Color.BLUE);
        barDataSet4.setValueTextColor(Color.BLUE);
        barDataSet5.setValueTextColor(Color.BLUE);
        barDataSet6.setValueTextColor(Color.BLUE);

//      setting value textsize
        barDataSet1.setValueTextSize(12);
        barDataSet2.setValueTextSize(12);
        barDataSet3.setValueTextSize(12);
        barDataSet4.setValueTextSize(12);
        barDataSet5.setValueTextSize(12);
        barDataSet6.setValueTextSize(12);

        barData = new BarData(barDataSet1, barDataSet2, barDataSet3, barDataSet4, barDataSet5, barDataSet6);

        float groupSpace = 0.06f;
        float barSpace = 0.3f; // x2 dataset
        float barWidth = 0.7f; // x2 dataset

        barData.setBarWidth(barWidth);

        barChart.setData(barData);
        barChart.groupBars(0.5f, groupSpace, barSpace);

        barChart.notifyDataSetChanged();
        barChart.invalidate();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        duration = position;

        final Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        todayDate = calendar.getTimeInMillis();

        if (duration == 0) {
            calendar.add(Calendar.DATE, -7);
            queryDate = calendar.getTimeInMillis();
//            Log.d("duration", calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+"/"+calendar.get(Calendar.YEAR));
        } else if (duration == 1) {
            calendar.add(Calendar.MONTH, -1);
            queryDate = calendar.getTimeInMillis();
        } else {
            calendar.add(Calendar.YEAR, -1);
            queryDate = calendar.getTimeInMillis();
        }

        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId);

        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
//                  maintenance
                    if (dataSnapshot.child("Maintenance").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Maintenance");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    if (expensesDB.getDate() >= queryDate && expensesDB.getDate() <= todayDate) {
                                        Log.d("expensesDB(maintena)", String.valueOf(expensesDB.getCost()));
                                        maintenance.add(expensesDB.getCost());
                                        maintenanceDate.add(expensesDB.getDate());
                                    }
                                }
                            }
                        });
                        Double cost1 = 0.1;
                        for (Double val : maintenance) {
                            cost1 = val + cost1;
                        }
                        if (barEntries1.isEmpty()) {
                            Log.d("barEntries1", "Empty");
                        } else {
                            barEntries1.remove(0);
                        }
                        barEntries1.add(new BarEntry(1, cost1.floatValue()));
                    } else {
                        Log.d("maintenance", "Empty");
                        barEntries1.add(new BarEntry(1, 0));
                    }

//                  fuel
                    if (dataSnapshot.child("Fuel").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Fuel");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    if (expensesDB.getDate() >= queryDate) {
                                        Log.d("expensesDB(fuel)", String.valueOf(expensesDB.getCost()));
                                        fuel.add(expensesDB.getCost());
                                        fuelDate.add(expensesDB.getDate());
                                    }
                                }
                            }
                        });
                        Double cost2 = 0.1;
                        for (Double val : fuel) {
                            cost2 = val + cost2;
                        }
                        if (barEntries2.isEmpty()) {
                            Log.d("barEntries2", "Empty");
                        } else {
                            barEntries2.remove(0);
                        }
                        barEntries2.add(new BarEntry(2, cost2.floatValue()));
                    } else {
                        Log.d("fuel", "Empty");
                        barEntries2.add(new BarEntry(2, 0));
                    }

//                  purchase
                    if (dataSnapshot.child("Purchase").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Purchase");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    if (expensesDB.getDate() >= queryDate) {
                                        Log.d("expensesDB(purchase)", String.valueOf(expensesDB.getCost()));
                                        purchase.add(expensesDB.getCost());
                                        purchaseDate.add(expensesDB.getDate());
                                    }
                                }
                            }
                        });
                        Double cost3 = 0.1;
                        for (Double val : purchase) {
                            cost3 = val + cost3;
                        }
                        if (barEntries3.isEmpty()) {
                            Log.d("barEntries3", "Empty");
                        } else {
                            barEntries3.remove(0);
                        }
                        barEntries3.add(new BarEntry(3, cost3.floatValue()));
                    } else {
                        Log.d("purchase", "Empty");
                        barEntries3.add(new BarEntry(3, 0));
                    }

//                  services
                    if (dataSnapshot.child("Service").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Service");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    if (expensesDB.getDate() >= queryDate) {
                                        Log.d("expensesDB(services)", String.valueOf(expensesDB.getCost()));
                                        services.add(expensesDB.getCost());
                                        servicesDate.add(expensesDB.getDate());
                                    }
                                }
                            }
                        });
                        Double cost4 = 0.1;
                        for (Double val : services) {
                            cost4 = val + cost4;
                        }
                        if (barEntries4.isEmpty()) {
                            Log.d("barEntries4", "Empty");
                        } else {
                            barEntries4.remove(0);
                        }
                        barEntries4.add(new BarEntry(4, cost4.floatValue()));
                    } else {
                        Log.d("services", "Empty");
                        barEntries4.add(new BarEntry(4, 0));
                    }

//                  fine
                    if (dataSnapshot.child("Fine").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Fine");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    if (expensesDB.getDate() >= queryDate) {
                                        Log.d("expensesDB(fine)", String.valueOf(expensesDB.getCost()));
                                        fine.add(expensesDB.getCost());
                                        fineDate.add(expensesDB.getDate());
                                    }
                                }
                            }
                        });
                        Double cost5 = 0.1;
                        for (Double val : fine) {
                            cost5 = val + cost5;
                        }
                        if (barEntries5.isEmpty()) {
                            Log.d("barEntries5", "Empty");
                        } else {
                            barEntries5.remove(0);
                        }
                        barEntries5.add(new BarEntry(5, cost5.floatValue()));
                    } else {
                        Log.d("fine", "Empty");
                        barEntries5.add(new BarEntry(5, 0));
                    }

//                  tax
                    if (dataSnapshot.child("Tax").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Tax");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    if (expensesDB.getDate() >= queryDate) {
                                        Log.d("expensesDB(tax)", String.valueOf(expensesDB.getCost()));
                                        tax.add(expensesDB.getCost());
                                        taxDate.add(expensesDB.getDate());
                                    }
                                }
                            }
                        });
                        Double cost6 = 0.1;
                        for (Double val : tax) {
                            cost6 = val + cost6;
                        }
                        if (barEntries6.isEmpty()) {
                            Log.d("barEntries6", "Empty");
                        } else {
                            barEntries6.remove(0);
                        }
                        barEntries6.add(new BarEntry(6, cost6.floatValue()));
                    } else {
                        Log.d("fuel", "Empty");
                        barEntries6.add(new BarEntry(6, 0));
                    }


//                    settingValues();
                }


//                initializeBarGraph();

//                barDataSet1 = new BarDataSet(barEntries1, "Maintenance");
//                barDataSet2 = new BarDataSet(barEntries2, "Fuel");
//                barDataSet3 = new BarDataSet(barEntries3, "Purchase");
//                barDataSet4 = new BarDataSet(barEntries4, "Services");
//                barDataSet5 = new BarDataSet(barEntries5, "Fine");
//                barDataSet6 = new BarDataSet(barEntries6, "Tax");

                initializeBarGraph();
            }
        });

//        initializeBarGraph();


//        if(!isFirtTime1) {
//            isFirtTime1 = true;
//            isFirtTime2 = false;
//        }else{
//            isFirtTime1 = false;
//            isFirtTime2 = true;
//        }
//
//            barChart.notifyDataSetChanged();
//            barChart.invalidate();
//            initializeBarGraph();
//            initializeBarGraph();
//        }
//        else{
//            initializeBarGraph();
//            barChart.notifyDataSetChanged();
//            barChart.invalidate();
//            initializeBarGraph();
//        }
//        else{
//            if(!isFirtTime2){
//                isFirtTime2=true;
//                initializeBarGraph();
//            }
//        }

//        if(isFirtTime1){
//            initializeBarGraph();
//            isFirtTime1=false;
//        }

//        initializeBarGraph();
    }


//    public void settingValues(){
//        //                  setting bar entries
//        if(!maintenance.isEmpty()) {
//            Double cost1=0.0;
//            for(Double val:maintenance){
//                cost1=val+cost1;
//            }
//            float val=cost1.floatValue();
//            barEntries1.add(new BarEntry(1f, cost1.floatValue()));
////                        getEntries("Maintenance", cost.floatValue());
//        }else{
//            Log.d("maintenance", "Empty");
//            barEntries1.add(new BarEntry(1f, 0));
//        }
//
//        if(!fuel.isEmpty()) {
//            Double cost2=0.0;
//            for(Double val:fuel){
//                cost2=val+cost2;
//            }
//            barEntries2.add(new BarEntry(2f, cost2.floatValue()));
////                        getEntries("Fuel", cost.floatValue());
//        }else{
//            Log.d("fuel", "Empty");
//            barEntries2.add(new BarEntry(2f, 0));
//        }
//
//        if(!purchase.isEmpty()) {
//            Double cost3=0.0;
//            for(Double val:purchase){
//                cost3=val+cost3;
//            }
//            barEntries3.add(new BarEntry(3f, cost3.floatValue()));
////                        getEntries("Purchase", cost.floatValue());
//        }else{
//            Log.d("purchase", "Empty");
//            barEntries3.add(new BarEntry(3f, 0));
//        }
//
//        if(!services.isEmpty()) {
//            Double cost4=0.0;
//            for(Double val:services){
//                cost4=val+cost4;
//            }
//            barEntries4.add(new BarEntry(4f, cost4.floatValue()));
////                        getEntries("Services", cost.floatValue());
//        }else{
//            Log.d("services", "Empty");
//            barEntries4.add(new BarEntry(4f, 0));
//        }
//
//        if(!fine.isEmpty()) {
//            Double cost5=0.0;
//            for(Double val:fine){
//                cost5=val+cost5;
//            }
//            barEntries5.add(new BarEntry(6f, cost5.floatValue()));
////                        getEntries("Fine", cost.floatValue());
//        }else{
//            Log.d("fine", "Empty");
//            barEntries6.add(new BarEntry(6f, 0));
//        }
//
//        if(!tax.isEmpty()) {
//            Double cost6=0.0;
//            for(Double val:tax){
//                cost6=val+cost6;
//            }
//            barEntries6.add(new BarEntry(7f, cost6.floatValue()));
////                        getEntries("Tax", cost.floatValue());
//        }else{
//            Log.d("tax", "Empty");
//            barEntries6.add(new BarEntry(7f, 0));
//        }
//    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
