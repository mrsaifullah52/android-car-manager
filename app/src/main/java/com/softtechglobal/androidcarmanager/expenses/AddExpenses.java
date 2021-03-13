package com.softtechglobal.androidcarmanager.expenses;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

import java.util.Calendar;

public class AddExpenses extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner expenseSpinner;
    String[] service={"Maintenance","Fuel","Purchase","Service","Engine Tunning","Fine","Tax"};
    EditText date,time;

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexpenses);
        setTitle("Add Expenses");


        expenseSpinner=(Spinner)findViewById(R.id.expenseSpinner);
        date=(EditText) findViewById(R.id.expenseDate);
        time=(EditText) findViewById(R.id.expenseTime);

        expenseSpinner.setOnItemSelectedListener(this);


//      seting date and time picker
        date.setOnClickListener(new View.OnClickListener() {
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
                        date.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mHour=calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute=calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddExpenses.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+" : "+minute);
                    }
                },mHour, mMinute, true);
                timePickerDialog.show();
            }
        });


        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,service);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseSpinner.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
