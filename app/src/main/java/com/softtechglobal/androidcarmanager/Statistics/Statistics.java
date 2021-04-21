package com.softtechglobal.androidcarmanager.Statistics;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

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
//  barchart
    BarChart barChart;
    BarDataSet barDataSet1, barDataSet2, barDataSet3, barDataSet4, barDataSet5, barDataSet6;
    BarData barData;
    ArrayList<BarEntry> barEntries1, barEntries2, barEntries3, barEntries4, barEntries5, barEntries6;
//  spinner
    Spinner statisticsSpinner;
    private final String[] service = {"Select Duration","Last 7 Days", "Last 1 Month", "Last 12 Months"};
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
    Long queryDate, todayDate;
    String vehicleId;
    Double cost1 =  0.01,  cost2 =  0.01, cost3 =  0.01, cost4 =  0.01, cost5 =  0.01, cost6 =  0.01;

    private DatabaseReference databaseReference1;
    private Query databaseReference2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    ProgressDialog progressDialog;
    ImageButton searchBtn;
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
        searchBtn= (ImageButton) findViewById(R.id.searchBtn);
        statisticsSpinner = (Spinner) findViewById(R.id.statisticsSpinner);
        statisticsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, service);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statisticsSpinner.setAdapter(adapter);

        barChart = findViewById(R.id.chart);
        barChart.getDescription().setText("Statistics Variables");

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Statistics.this, Search.class);
                startActivity(i);
            }
        });

//      barChart.getXAxis().setEnabled(true);
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

        barEntries1.add(new BarEntry(1, cost1.floatValue()));
        barEntries2.add(new BarEntry(2, cost2.floatValue()));
        barEntries3.add(new BarEntry(3, cost3.floatValue()));
        barEntries4.add(new BarEntry(4, cost4.floatValue()));
        barEntries5.add(new BarEntry(5, cost5.floatValue()));
        barEntries6.add(new BarEntry(6, cost6.floatValue()));
        initializeBarGraph(false);

        progressDialog= ProgressDialog.show(Statistics.this, "","Please Wait, Loading...",true);
        getDataFromFirebase();
    }

//    get data before spinner get changed
    public void initializeBarGraph(Boolean value) {
        if (value) {
            barDataSet1.removeFirst();
            barDataSet2.removeFirst();
            barDataSet3.removeFirst();
            barDataSet4.removeFirst();
            barDataSet5.removeFirst();
            barDataSet6.removeFirst();

            barChart.notifyDataSetChanged();
            barChart.invalidate();
        }else{
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
        }
    }

    public void getDataFromFirebase() {

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
                                    Log.d("expensesDB(maintena)", String.valueOf(expensesDB.getCost()));
                                    maintenance.add(expensesDB.getCost());
                                    maintenanceDate.add(expensesDB.getDate());
                                }
                            }
                        });
                    } else {
                        Log.d("expensesDB(maintenance)", "Empty");
                    }

//                  fuel
                    if (dataSnapshot.child("Fuel").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Fuel");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    Log.d("expensesDB(fuel)", String.valueOf(expensesDB.getCost()));
                                    fuel.add(expensesDB.getCost());
                                    fuelDate.add(expensesDB.getDate());
                                }
                            }
                        });
                    } else {
                        Log.d("fuel", "Empty");
                    }

//                  purchase
                    if (dataSnapshot.child("Purchase").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Purchase");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    Log.d("expensesDB(purchase)", String.valueOf(expensesDB.getCost()));
                                    purchase.add(expensesDB.getCost());
                                    purchaseDate.add(expensesDB.getDate());
                                }
                            }
                        });
                    } else {
                        Log.d("purchase", "Empty");
                    }

//                  services
                    if (dataSnapshot.child("Service").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Service");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    Log.d("expensesDB(services)", String.valueOf(expensesDB.getCost()));
                                    services.add(expensesDB.getCost());
                                    servicesDate.add(expensesDB.getDate());
                                }
                            }
                        });
                    } else {
                        Log.d("services", "Empty");
                    }

//                  fine
                    if (dataSnapshot.child("Fine").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Fine");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                        Log.d("expensesDB(fine)", String.valueOf(expensesDB.getCost()));
                                        fine.add(expensesDB.getCost());
                                        fineDate.add(expensesDB.getDate());
                                }
                            }
                        });
                    } else {
                        Log.d("fine", "Empty");
                    }

//                  tax
                    if (dataSnapshot.child("Tax").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Tax");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                        Log.d("expensesDB(tax)", String.valueOf(expensesDB.getCost()));
                                        tax.add(expensesDB.getCost());
                                        taxDate.add(expensesDB.getDate());
                                }
                            }
                        });
                    } else {
                        Log.d("Tax", "Empty");
                    }
                    progressDialog.dismiss();
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Statistics.this,"Failed to Fetch Data try again",Toast.LENGTH_SHORT).show();
                    Log.d("dataSnapshot.exists()", "Empty");
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        duration = position;
        final Date currentDate = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        todayDate = calendar.getTimeInMillis();

        if (duration == 1) {
            calendar.add(Calendar.DATE, -7);
            queryDate = calendar.getTimeInMillis();

//          maintenance
            if(!maintenance.isEmpty() && !maintenanceDate.isEmpty()){
                for(int i=0;i<=maintenance.size()-1;i++){
                    if (maintenanceDate.get(i) >= queryDate){
                        cost1 = cost1 + maintenance.get(i);
                        Log.d("maintenance"+i, String.valueOf(cost1));
                    }else{
                        Log.d("maintenance", "out of filter");
                    }
                }
                barEntries1.add(new BarEntry(1,cost1.floatValue()));
                cost1=0.1;
            }else{
                Log.d("cost1", "empty");
                barEntries1.add(new BarEntry(1, 0));
            }
//          fuel
            if(!fuel.isEmpty() && !fuelDate.isEmpty()){
                for(int i=0;i<=fuel.size()-1;i++){
                    if (fuelDate.get(i) >= queryDate){
                        cost2 = cost2 + fuel.get(i);
                        Log.d("fuel"+i, String.valueOf(cost2));
                    }else{
                        Log.d("fuel", "out of filter");
                    }
                }
                barEntries2.add(new BarEntry(2,cost2.floatValue()));
                cost2=0.1;
            }else{
                Log.d("fuel", "empty");
                barEntries2.add(new BarEntry(2, 0));
            }
//          purchase
            if(!purchase.isEmpty() && !purchaseDate.isEmpty()){
                for(int i=0;i<=purchase.size()-1;i++){
                    if (purchaseDate.get(i) >= queryDate){
                        cost3 = cost3 + purchase.get(i);
                        Log.d("purchase"+i, String.valueOf(cost3));
                    }else{
                        Log.d("purchase", "out of filter");
                    }
                }
                barEntries3.add(new BarEntry(3,cost3.floatValue()));
                cost3=0.1;
            }else{
                Log.d("purchase", "empty");
                barEntries3.add(new BarEntry(3, 0));
            }
//          service
            if(!services.isEmpty() && !servicesDate.isEmpty()){
                for(int i=0;i<=services.size()-1;i++){
                    if (servicesDate.get(i) >= queryDate){
                        cost4 = cost4 + services.get(i);
                        Log.d("service"+i, String.valueOf(cost4));
                    }else{
                        Log.d("service", "out of filter");
                    }
                }
                barEntries4.add(new BarEntry(4,cost4.floatValue()));
                cost4=0.1;
            }else{
                Log.d("services", "empty");
                barEntries4.add(new BarEntry(4, 0));
            }
//          fine
            if(!fine.isEmpty() && !fineDate.isEmpty()){
                for(int i=0;i<=fine.size()-1;i++){
                    if (fineDate.get(i) >= queryDate){
                        cost5 = cost5 + fine.get(i);
                        Log.d("fine"+i, String.valueOf(cost5));
                    }else{
                        Log.d("fine", "out of filter");
                    }
                }
                barEntries5.add(new BarEntry(5,cost5.floatValue()));
                cost5=0.1;
            }else{
                Log.d("fine", "empty");
                barEntries5.add(new BarEntry(5, 0));
            }
//          tax
            if(!tax.isEmpty() && !taxDate.isEmpty()){
                for(int i=0;i<=taxDate.size()-1;i++){
                    if (taxDate.get(i) >= queryDate){
                        cost6 = cost6 + tax.get(i);
                        Log.d("tax"+i, String.valueOf(cost6));
                    }else{
                        Log.d("tax", "out of filter");
                    }
                }
                barEntries6.add(new BarEntry(6,cost6.floatValue()));
                cost6=0.1;
            }else{
                Log.d("tax", "empty");
                barEntries6.add(new BarEntry(6, 0));
            }
            initializeBarGraph(true);
            initializeBarGraph(false);

        } else if (duration == 2) {
            calendar.add(Calendar.MONTH, -1);
            queryDate = calendar.getTimeInMillis();

//          maintenance
            if(!maintenance.isEmpty() && !maintenanceDate.isEmpty()){
                for(int i=0;i<=maintenance.size()-1;i++){
                    if (maintenanceDate.get(i) >= queryDate){
                        cost1 = cost1 + maintenance.get(i);
                        Log.d("maintenance"+i, String.valueOf(cost1));
                    }else{
                        Log.d("maintenance", "out of filter");
                    }
                }
                barEntries1.add(new BarEntry(1,cost1.floatValue()));
                cost1=0.1;
            }else{
                Log.d("cost1", "empty");
                barEntries1.add(new BarEntry(1, 0));
            }
//          fuel
            if(!fuel.isEmpty() && !fuelDate.isEmpty()){
                for(int i=0;i<=fuel.size()-1;i++){
                    if (fuelDate.get(i) >= queryDate){
                        cost2 = cost2 + fuel.get(i);
                        Log.d("fuel"+i, String.valueOf(cost2));
                    }else{
                        Log.d("fuel", "out of filter");
                    }
                }
                barEntries2.add(new BarEntry(2,cost2.floatValue()));
                cost2=0.1;
            }else{
                Log.d("fuel", "empty");
                barEntries2.add(new BarEntry(2, 0));
            }
//          purchase
            if(!purchase.isEmpty() && !purchaseDate.isEmpty()){
                for(int i=0;i<=purchase.size()-1;i++){
                    if (purchaseDate.get(i) >= queryDate){
                        cost3 = cost3 + purchase.get(i);
                        Log.d("purchase"+i, String.valueOf(cost3));
                    }else{
                        Log.d("purchase", "out of filter");
                    }
                }
                barEntries3.add(new BarEntry(3,cost3.floatValue()));
                cost3=0.1;
            }else{
                Log.d("purchase", "empty");
                barEntries3.add(new BarEntry(3, 0));
            }
//          service
            if(!services.isEmpty() && !servicesDate.isEmpty()){
                for(int i=0;i<=services.size()-1;i++){
                    if (servicesDate.get(i) >= queryDate){
                        cost4 = cost4 + services.get(i);
                        Log.d("service"+i, String.valueOf(cost4));
                    }else{
                        Log.d("service", "out of filter");
                    }
                }
                barEntries4.add(new BarEntry(4,cost4.floatValue()));
                cost4=0.1;
            }else{
                Log.d("services", "empty");
                barEntries4.add(new BarEntry(4, 0));
            }
//          fine
            if(!fine.isEmpty() && !fineDate.isEmpty()){
                for(int i=0;i<=fine.size()-1;i++){
                    if (fineDate.get(i) >= queryDate){
                        cost5 = cost5 + fine.get(i);
                        Log.d("fine"+i, String.valueOf(cost5));
                    }else{
                        Log.d("fine", "out of filter");
                    }
                }
                barEntries5.add(new BarEntry(5,cost5.floatValue()));
                cost5=0.1;
            }else{
                Log.d("fine", "empty");
                barEntries5.add(new BarEntry(5, 0));
            }
//          tax
            if(!tax.isEmpty() && !taxDate.isEmpty()){
                for(int i=0;i<=taxDate.size()-1;i++){
                    if (taxDate.get(i) >= queryDate){
                        cost6 = cost6 + tax.get(i);
                        Log.d("tax"+i, String.valueOf(cost6));
                    }else{
                        Log.d("tax", "out of filter");
                    }
                }
                barEntries6.add(new BarEntry(6,cost6.floatValue()));
                cost6=0.1;
            }else{
                Log.d("tax", "empty");
                barEntries6.add(new BarEntry(6, 0));
            }
            initializeBarGraph(true);
            initializeBarGraph(false);

        } else if(duration==3) {
            calendar.add(Calendar.YEAR, -1);
            queryDate = calendar.getTimeInMillis();

//          maintenance
            if(!maintenance.isEmpty() && !maintenanceDate.isEmpty()){
                for(int i=0;i<=maintenance.size()-1;i++){
                    if (maintenanceDate.get(i) >= queryDate){
                        cost1 = cost1 + maintenance.get(i);
                        Log.d("maintenance"+i, String.valueOf(cost1));
                    }else{
                        Log.d("maintenance", "out of filter");
                    }
                }
                barEntries1.add(new BarEntry(1,cost1.floatValue()));
                cost1=0.1;
            }else{
                Log.d("cost1", "empty");
                barEntries1.add(new BarEntry(1, 0));
            }
//          fuel
            if(!fuel.isEmpty() && !fuelDate.isEmpty()){
                for(int i=0;i<=fuel.size()-1;i++){
                    if (fuelDate.get(i) >= queryDate){
                        cost2 = cost2 + fuel.get(i);
                        Log.d("fuel"+i, String.valueOf(cost2));
                    }else{
                        Log.d("fuel", "out of filter");
                    }
                }
                barEntries2.add(new BarEntry(2,cost2.floatValue()));
                cost2=0.1;
            }else{
                Log.d("fuel", "empty");
                barEntries2.add(new BarEntry(2, 0));
            }
//          purchase
            if(!purchase.isEmpty() && !purchaseDate.isEmpty()){
                for(int i=0;i<=purchase.size()-1;i++){
                    if (purchaseDate.get(i) >= queryDate){
                        cost3 = cost3 + purchase.get(i);
                        Log.d("purchase"+i, String.valueOf(cost3));
                    }else{
                        Log.d("purchase", "out of filter");
                    }
                }
                barEntries3.add(new BarEntry(3,cost3.floatValue()));
                cost3=0.1;
            }else{
                Log.d("purchase", "empty");
                barEntries3.add(new BarEntry(3, 0));
            }
//          service
            if(!services.isEmpty() && !servicesDate.isEmpty()){
                for(int i=0;i<=services.size()-1;i++){
                    if (servicesDate.get(i) >= queryDate){
                        cost4 = cost4 + services.get(i);
                        Log.d("service"+i, String.valueOf(cost4));
                    }else{
                        Log.d("service", "out of filter");
                    }
                }
                barEntries4.add(new BarEntry(4,cost4.floatValue()));
                cost4=0.1;
            }else{
                Log.d("services", "empty");
                barEntries4.add(new BarEntry(4, 0));
            }
//          fine
            if(!fine.isEmpty() && !fineDate.isEmpty()){
                for(int i=0;i<=fine.size()-1;i++){
                    if (fineDate.get(i) >= queryDate){
                        cost5 = cost5 + fine.get(i);
                        Log.d("fine"+i, String.valueOf(cost5));
                    }else{
                        Log.d("fine", "out of filter");
                    }
                }
                barEntries5.add(new BarEntry(5,cost5.floatValue()));
                cost5=0.1;
            }else{
                Log.d("fine", "empty");
                barEntries5.add(new BarEntry(5, 0));
            }
//          tax
            if(!tax.isEmpty() && !taxDate.isEmpty()){
                for(int i=0;i<=tax.size()-1;i++){
                    if (taxDate.get(i) >= queryDate){
                        cost6 = cost6 + tax.get(i);
                        Log.d("tax"+i, String.valueOf(cost6));
                    }else{
                        Log.d("tax", "out of filter");
                    }
                }
                barEntries6.add(new BarEntry(6,cost6.floatValue()));
                cost6=0.1;
            }else{
                Log.d("tax", "empty");
                barEntries6.add(new BarEntry(6, 0));
            }
            initializeBarGraph(true);
            initializeBarGraph(false);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override
    public void onBackPressed() {
        progressDialog.dismiss();
        super.onBackPressed();
    }
}
