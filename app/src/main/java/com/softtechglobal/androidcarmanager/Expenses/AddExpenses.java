package com.softtechglobal.androidcarmanager.Expenses;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
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

import java.util.ArrayList;
import java.util.Calendar;

public class AddExpenses extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner expenseSpinner;
    String[] expenses={"Maintenance","Fuel","Purchase","Service","Engine Tunning","Fine","Tax"};
    String selectedExpenses;
//  data to put
    Long date, time;
    Double meter, cost;

    EditText dateEt,timeEt, meterReadingEt,costEt;
    Button saveBtn;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private DatabaseReference databaseReference1, databaseReference2;
    private FirebaseAuth firebaseAuth;

    String key;
    int expensesIndex;
    ArrayList<String> keys=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexpenses);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(AddExpenses.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");
        databaseReference1= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/vehicles/");
        databaseReference2=FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/expenses/"+key);

        setTitle("Add Expenses");
        expenseSpinner=(Spinner)findViewById(R.id.expenseTitleSp);
        dateEt=(EditText) findViewById(R.id.expenseDateEt);
        timeEt=(EditText) findViewById(R.id.expenseTimeEt);
        meterReadingEt=(EditText) findViewById(R.id.meterEt);
        costEt=(EditText) findViewById(R.id.costEt);
        saveBtn=(Button) findViewById(R.id.saveExpenseBtn);

        expenseSpinner.setOnItemSelectedListener(this);

//      seting date and time picker
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mYear=calendar.get(Calendar.YEAR);
                int mMonth=calendar.get(Calendar.MONTH);
                int mDay=calendar.get(Calendar.DAY_OF_MONTH);

//                setting date picker
                datePickerDialog = new DatePickerDialog(AddExpenses.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEt.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        date=calendar.getTimeInMillis();
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mHour=calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute=calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddExpenses.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEt.setText(hourOfDay+" : "+minute);
                        time=calendar.getTimeInMillis();
                    }
                },mHour, mMinute, true);
                timePickerDialog.show();
            }
        });


        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,expenses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseSpinner.setAdapter(adapter);

        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    keys.add(ds.getKey());
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromEt();
                if(key.equals("-1")) {
                    Toast.makeText(AddExpenses.this, "Please Select a Car Before Adding Expense!", Toast.LENGTH_LONG).show();
                }else if (selectedExpenses.isEmpty()){
                    Toast.makeText(AddExpenses.this,"Select Expense Category!",Toast.LENGTH_SHORT).show();
                }else if(date==null){
                    Toast.makeText(AddExpenses.this,"Enter Date!",Toast.LENGTH_SHORT).show();
                }else if(time==null){
                    Toast.makeText(AddExpenses.this,"Enter Time!",Toast.LENGTH_SHORT).show();
                }else if(meter==null){
                    Toast.makeText(AddExpenses.this,"Enter Meter Reading!",Toast.LENGTH_SHORT).show();
                }else if(cost==null){
                    Toast.makeText(AddExpenses.this,"Enter Cost!",Toast.LENGTH_SHORT).show();
                }else{
                    int position=keys.indexOf(key);
                    if(position==-1){
                        Toast.makeText(AddExpenses.this, "Please Select a Existing Car!", Toast.LENGTH_LONG).show();
                    }else {
                        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                            @Override
                            public void onSuccess(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.child(selectedExpenses).exists()){
                                    for (DataSnapshot ds : dataSnapshot.child(selectedExpenses).getChildren()) {
                                        expensesIndex = Integer.parseInt(ds.getKey());
                                        Log.d("expensesIndex1", String.valueOf(expensesIndex));
                                    }
                                    expensesIndex++;
                                    Log.d("expensesIndex2", String.valueOf(expensesIndex));
                                    addIntoDb(expensesIndex);
//                                    expensesIndex = Integer.parseInt(dataSnapshot.child(selectedExpenses).getKey());
                                }else{
                                    Log.d("expensesIndex3", String.valueOf(expensesIndex));
                                    expensesIndex=0;
                                    addIntoDb(expensesIndex);
                                }
                            }
                        });

                    }
                }

            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedExpenses=expenses[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void addIntoDb(int index){
        ExpensesDB expensesDB = new ExpensesDB(selectedExpenses, date, time, meter, cost);
        databaseReference2.child(selectedExpenses).child(String.valueOf(index)).setValue(expensesDB);
        clearEtValue();
        Toast.makeText(AddExpenses.this, "Expense Added!", Toast.LENGTH_SHORT).show();

    }

    public void clearEtValue(){
        dateEt.setText("");
        timeEt.setText("");
        meterReadingEt.setText("");
        costEt.setText("");
    }

    public void getValuesFromEt(){
        meter= Double.valueOf(meterReadingEt.getText().toString());
        cost= Double.valueOf(costEt.getText().toString().trim());
    }
}
