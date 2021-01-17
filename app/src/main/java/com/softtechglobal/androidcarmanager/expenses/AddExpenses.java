package com.softtechglobal.androidcarmanager.expenses;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

public class AddExpenses extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner expenseSpinner;
    String[] service={"Maintenance","Fuel","Purchase","Service","Engine Tunning","Fine","Tax"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexpenses);
        setTitle("Add Expenses");


        expenseSpinner=(Spinner)findViewById(R.id.expenseSpinner);

        expenseSpinner.setOnItemSelectedListener(this);

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
