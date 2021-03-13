package com.softtechglobal.androidcarmanager.statistics;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.softtechglobal.androidcarmanager.R;

import java.util.ArrayList;

public class Statistics extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    BarChart barChart;
    BarData barData;
    BarDataSet barDataSet;
    ArrayList barEntries;

    Spinner statisticsSpinner;
    private final String[] service={"Expenses","Maintenance","Mileage"};
    private final String[] mMonths = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        setTitle("View Statistics");


        statisticsSpinner=(Spinner)findViewById(R.id.statisticsSpinner);
        statisticsSpinner.setOnItemSelectedListener(this);
        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,service);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statisticsSpinner.setAdapter(adapter);


        barChart = findViewById(R.id.chart);
        getEntries();
        barDataSet = new BarDataSet(barEntries, "Fuel Expenses");
        barData = new BarData(barDataSet);
        barChart.setData(barData);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLUE);
        barDataSet.setValueTextSize(12);
    }

    private void getEntries() {
        barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(1, 2));
        barEntries.add(new BarEntry(2, 4));
        barEntries.add(new BarEntry(3f, 3));
        barEntries.add(new BarEntry(4f, 3));
        barEntries.add(new BarEntry(5f, 1));
        barEntries.add(new BarEntry(6f, 5));
        barEntries.add(new BarEntry(7f, 2));
        barEntries.add(new BarEntry(8f, 3));
        barEntries.add(new BarEntry(9f, 3));
        barEntries.add(new BarEntry(10f, 1));
        barEntries.add(new BarEntry(11f, 5));
        barEntries.add(new BarEntry(12f, 2));
    }

    public  void DayAxisValueFormatter(BarChart myBarChart){
        this.barChart=myBarChart;
    }




























    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
