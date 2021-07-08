package com.softtechglobal.androidcarmanager.Statistics;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Views.BaseAdapterForList;
import com.softtechglobal.androidcarmanager.Database.ExpensesDB;
import com.softtechglobal.androidcarmanager.Views.ModelForList;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;

public class Search extends AppCompatActivity {

    EditText startDateEt, endDateEt, searchTitleEt;
    ListView expensesList;
    ImageButton searchListBtn;

    BaseAdapterForList adapter;
//  values from firebase

    ArrayList<String> maintenanceKey = new ArrayList<String>();
    ArrayList<String> maintenance = new ArrayList<String>();
    ArrayList<Long> maintenanceDate = new ArrayList<Long>();
    ArrayList<String> fuelKey = new ArrayList<String>();
    ArrayList<String> fuel = new ArrayList<String>();
    ArrayList<Long> fuelDate = new ArrayList<Long>();
    ArrayList<String> purchaseKey = new ArrayList<String>();
    ArrayList<String> purchase = new ArrayList<String>();
    ArrayList<Long> purchaseDate = new ArrayList<Long>();
    ArrayList<String> servicesKey = new ArrayList<String>();
    ArrayList<String> services = new ArrayList<String>();
    ArrayList<Long> servicesDate = new ArrayList<Long>();
    ArrayList<String> fineKey = new ArrayList<String>();
    ArrayList<String> fine = new ArrayList<String>();
    ArrayList<Long> fineDate = new ArrayList<Long>();
    ArrayList<String> taxKey = new ArrayList<String>();
    ArrayList<String> tax = new ArrayList<String>();
    ArrayList<Long> taxDate = new ArrayList<Long>();


    ArrayList<String>index=new ArrayList<String>();
    ArrayList<String>category=new ArrayList<String>();
    ArrayList<String>titles=new ArrayList<String>();
    ArrayList<Long>dates=new ArrayList<Long>();
    ArrayList<ModelForList> listModel= new ArrayList<ModelForList>();

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
        searchListBtn=(ImageButton) findViewById(R.id.searchListBtn);


        getDataFromFirebase();

//      listview click
        expensesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("title", titles.get(position));
                Log.d("category", category.get(position));
                Log.d("date", String.valueOf(dates.get(position)));
                Log.d("index", String.valueOf(index.get(position)));

                Intent i=new Intent(Search.this, SearchDetails.class);
                i.putExtra("category", category.get(position));
                i.putExtra("index", index.get(position));
                startActivity(i);
            }
        });

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
                    progressDialog= ProgressDialog.show(Search.this, "","Please Wait, Loading...",true);
//                  maintenance
                    if (dataSnapshot.child("Maintenance").exists()) {
                        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/Maintenance");
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    ExpensesDB expensesDB = ds.getValue(ExpensesDB.class);
                                    Log.d("expensesDB(maintena)", expensesDB.getExpenseTitle());
                                    maintenanceKey.add(ds.getKey());
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
                                    fuel.add(expensesDB.getExpenseTitle());
                                    fuelDate.add(expensesDB.getDate());
                                    fuelKey.add(ds.getKey());
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
                                    purchase.add(expensesDB.getExpenseTitle());
                                    purchaseDate.add(expensesDB.getDate());
                                    purchaseKey.add(ds.getKey());
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
                                    services.add(expensesDB.getExpenseTitle());
                                    servicesDate.add(expensesDB.getDate());
                                    servicesKey.add(ds.getKey());
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
                                    fine.add(expensesDB.getExpenseTitle());
                                    fineDate.add(expensesDB.getDate());
                                    fineKey.add(ds.getKey());
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
                                    tax.add(expensesDB.getExpenseTitle());
                                    taxDate.add(expensesDB.getDate());
                                    taxKey.add(ds.getKey());
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
                }
            }
        });
    }

    public void setListforAdapter(){
        Calendar c=Calendar.getInstance();
//      maintenance
        if (!maintenance.isEmpty() && !maintenanceDate.isEmpty()){
            for(int i=0;i<maintenance.size();i++){
                category.add("Maintenance");
                titles.add(maintenance.get(i));
                dates.add(maintenanceDate.get(i));
                index.add(String.valueOf(i));
            }
        }
//      fuel
        if (!fuel.isEmpty() && !fuelDate.isEmpty()){
            for(int i=0;i<fuel.size();i++){
                category.add("Fuel");
                titles.add(fuel.get(i));
                dates.add(fuelDate.get(i));
                index.add(String.valueOf(i));
            }
        }
//      purchase
        if (!purchase.isEmpty() && !purchaseDate.isEmpty()){
            for(int i=0;i<purchase.size();i++){
                category.add("Purchase");
                titles.add(purchase.get(i));
                dates.add(purchaseDate.get(i));
                index.add(String.valueOf(i));
            }
        }
//      service
        if (!services.isEmpty() && !servicesDate.isEmpty()){
            for(int i=0;i<services.size();i++){
                category.add("Service");
                titles.add(services.get(i));
                dates.add(servicesDate.get(i));
                index.add(String.valueOf(i));
            }
        }
//      fine
        if (!fine.isEmpty() && !fineDate.isEmpty()){
            for(int i=0;i<fine.size();i++){
                category.add("Fine");
                titles.add(fine.get(i));
                dates.add(fineDate.get(i));
                index.add(String.valueOf(i));
            }
        }
//      tax
        if (!tax.isEmpty() && !taxDate.isEmpty()){
            for(int i=0;i<tax.size();i++){
                category.add("Tax");
                titles.add(tax.get(i));
                dates.add(taxDate.get(i));
                index.add(String.valueOf(i));
            }
        }
        setAdapter();
    }

    public void setAdapter(){
        if(!titles.isEmpty() && !dates.isEmpty()){
            for(int i=0;i<titles.size()-1;i++){
                ModelForList modelAdapter=new ModelForList(titles.get(i), dates.get(i));
                listModel.add(modelAdapter);
            }
            adapter = new BaseAdapterForList(Search.this, listModel);
            expensesList.setAdapter(adapter);
        }else{
            Toast.makeText(Search.this, "Failed in Retrieving Data",Toast.LENGTH_SHORT).show();
        }
    }
}
