package com.softtechglobal.androidcarmanager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Database.ExpensesDB;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;

public class Search extends AppCompatActivity {

    EditText startDateEt, endDateEt, searchTitleEt;
    ListView expensesList;
    Button searchListBtn;

    CustomBaseAdapter adapter;
//  values from firebase
    ArrayList<String> maintenance = new ArrayList<String>();
    ArrayList<Long> maintenanceDate = new ArrayList<Long>();
    ArrayList<String> fuel = new ArrayList<String>();
    ArrayList<Long> fuelDate = new ArrayList<Long>();
    ArrayList<String> purchase = new ArrayList<String>();
    ArrayList<Long> purchaseDate = new ArrayList<Long>();
    ArrayList<String> services = new ArrayList<String>();
    ArrayList<Long> servicesDate = new ArrayList<Long>();
    ArrayList<String> fine = new ArrayList<String>();
    ArrayList<Long> fineDate = new ArrayList<Long>();
    ArrayList<String> tax = new ArrayList<String>();
    ArrayList<Long> taxDate = new ArrayList<Long>();


    ArrayList<String>titles=new ArrayList<String>();
    ArrayList<Long>dates=new ArrayList<Long>();
    ArrayList<ModelForAdapter> listModel= new ArrayList<ModelForAdapter>();

    Long startDate, endDate;
    String query="";

    private DatabaseReference databaseReference1, databaseReference2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    String vehicleId;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Search.this, Signin.class));
        }
        vehicleId = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key", "-1");

        setTitle("Search Expenses");
        startDateEt=(EditText) findViewById(R.id.startDateEt);
        endDateEt=(EditText) findViewById(R.id.endDateEt);
        searchTitleEt=(EditText) findViewById(R.id.searchTitleEt);
        expensesList=(ListView) findViewById(R.id.searchList);
        searchListBtn=(Button) findViewById(R.id.searchListBtn);

        progressDialog= ProgressDialog.show(Search.this, "","Please Wait, Loading...",true);
        getDataFromFirebase();
//        setListforAdapter();


        startDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(Search.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        startDateEt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        calendar.set(year, month, dayOfMonth);
                        startDate=calendar.getTimeInMillis();
//                        startDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    }
                }, mYear, mMonth, mDay).show();
            }
        });

        endDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mYear=calendar.get(Calendar.YEAR);
                int mMonth=calendar.get(Calendar.MONTH);
                int mDay=calendar.get(Calendar.DAY_OF_MONTH);


                new DatePickerDialog(Search.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        endDateEt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        calendar.set(year, month, dayOfMonth);
                        endDate=calendar.getTimeInMillis();
//                        endDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                    }
                }, mYear, mMonth, mDay).show();
            }
        });

        searchListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query=searchTitleEt.getText().toString().trim();

                if (startDate == null){
                    Toast.makeText(Search.this,"Select Starting Date!",Toast.LENGTH_SHORT).show();
                }else if(endDate == null){
                    Toast.makeText(Search.this,"Select Ending Date!",Toast.LENGTH_SHORT).show();
                }else if(query.isEmpty()){
                    Toast.makeText(Search.this,"Enter your Query!",Toast.LENGTH_SHORT).show();
                }else {
                    adapter.titleDateFilter(startDate, endDate, query);
                }
            }
        });
    }

    public void getDataFromFirebase(){
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId);

        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
//                  maintenance
                    if (dataSnapshot.child("Maintenance").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Maintenance");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    Log.d("expensesDB(maintena)", expensesDB.getExpenseTitle());
                                    maintenance.add(expensesDB.getExpenseTitle());
                                    maintenanceDate.add(expensesDB.getDate());
                                }

                            }
                        });
                        for (String ti:maintenance){
                            Log.d("maintenance", ti);
                        }
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
                                    Log.d("expensesDB(fuel)", expensesDB.getExpenseTitle());
                                    fuel.add(expensesDB.getExpenseTitle());
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
                                    Log.d("expensesDB(purchase)", expensesDB.getExpenseTitle());
                                    purchase.add(expensesDB.getExpenseTitle());
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
                                    Log.d("expensesDB(services)", expensesDB.getExpenseTitle());
                                    services.add(expensesDB.getExpenseTitle());
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
                                    Log.d("expensesDB(fine)", expensesDB.getExpenseTitle());
                                    fine.add(expensesDB.getExpenseTitle());
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
                                    Log.d("expensesDB(tax)", expensesDB.getExpenseTitle());
                                    Log.d("expensesDB(tax)", String.valueOf(expensesDB.getDate()));
                                    tax.add(expensesDB.getExpenseTitle());
                                    taxDate.add(expensesDB.getDate());
                                }
                            }
                        });
                    } else {
                        Log.d("Tax", "Empty");
                    }


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressDialog.dismiss();
                            setListforAdapter();
                        }
                    },5*1000);

                }else{
                    progressDialog.dismiss();
                    Toast.makeText(Search.this,"Failed to Fetch Data try again",Toast.LENGTH_SHORT).show();
                    Log.d("dataSnapshot.exists()", "Empty");
                }




            }
        });
    }

    public void setListforAdapter(){
        Calendar c=Calendar.getInstance();
//      maintenance
        if (!maintenance.isEmpty() && !maintenanceDate.isEmpty()){
            for (String title:maintenance){
                titles.add(title);
                Log.d("setAdapter", title);
            }
            for (Long date:maintenanceDate){
//                c.setTimeInMillis(date);
//                dates.add(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                dates.add(date);
            }
        }
//      fuel
        if (!fuel.isEmpty() && !fuelDate.isEmpty()){
            for (String title:fuel){
                titles.add(title);
                Log.d("setAdapter", title);
            }
            for (Long date:fuelDate){
//                c.setTimeInMillis(date);
//                dates.add(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                dates.add(date);
            }
        }
//      purchase
        if (!purchase.isEmpty() && !purchaseDate.isEmpty()){
            for (String title:purchase){
                titles.add(title);
                Log.d("setAdapter", title);
            }
            for (Long date:purchaseDate){
//                c.setTimeInMillis(date);
//                dates.add(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                dates.add(date);
            }
        }
//      service
        if (!services.isEmpty() && !servicesDate.isEmpty()){
            for (String title:services){
                titles.add(title);
                Log.d("setAdapter", title);
            }
            for (Long date:servicesDate){
//                c.setTimeInMillis(date);
//                dates.add(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                dates.add(date);
            }
        }
//      fine
        if (!fine.isEmpty() && !fineDate.isEmpty()){
            for (String title:fine){
                titles.add(title);
                Log.d("setAdapter", title);
            }
            for (Long date:fineDate){
//                c.setTimeInMillis(date);
//                dates.add(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                dates.add(date);
            }
        }
//      tax
        if (!tax.isEmpty() && !taxDate.isEmpty()){

            for(int i=0;i<=tax.size()-1;i++){

                titles.add(tax.get(i));
//                c.setTimeInMillis(taxDate.get(i));
//                dates.add(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
                dates.add(taxDate.get(i));

            }

//            for (String title:tax){
//                titles.add(title);
//                Log.d("setAdapter", title);
//            }
//            for (Long date:taxDate){
//                c.setTimeInMillis(date);
//                dates.add(c.get(Calendar.DAY_OF_MONTH)+"/"+(c.get(Calendar.MONTH)+1)+"/"+c.get(Calendar.YEAR));
//            }
        }

        setAdapter();
    }

    public void setAdapter(){
        if(!titles.isEmpty() && !dates.isEmpty()){

//        Log.d("setAdapter", String.valueOf(titles.size()));
            for(int i=0;i<titles.size()-1;i++){
//                Log.d("setAdapter", titles.get(i)+ "/" + dates.get(i));
                ModelForAdapter modelAdapter=new ModelForAdapter(titles.get(i), dates.get(i));
                //bind all strings in an array
                listModel.add(modelAdapter);
            }
            adapter = new CustomBaseAdapter(Search.this, listModel);
            expensesList.setAdapter(adapter);

        }else{
            Toast.makeText(Search.this, "Failed in Retrieving Data",Toast.LENGTH_SHORT).show();
        }
    }
}
