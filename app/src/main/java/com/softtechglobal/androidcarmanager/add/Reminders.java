package com.softtechglobal.androidcarmanager.add;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

import java.util.Calendar;

public class Reminders extends AppCompatActivity {

    EditText title, date, time;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminders);
        setTitle("Add Reminder");

        title=(EditText)findViewById(R.id.reminderTitle);
        date=(EditText)findViewById(R.id.reminderDate);
        time=(EditText)findViewById(R.id.reminderTime);



        Bundle i = getIntent().getExtras();
//      check type
        if(i.getString("type").equals("edit")){
            title.setText(i.getString("title"));
            date.setText(i.getString("date"));
            time.setText(i.getString("time"));
        }


        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mYear=calendar.get(Calendar.YEAR);
                int mMonth=calendar.get(Calendar.MONTH);
                int mDay=calendar.get(Calendar.DAY_OF_MONTH);

//                setting date picker
                datePickerDialog = new DatePickerDialog(Reminders.this, new DatePickerDialog.OnDateSetListener() {
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

                timePickerDialog = new TimePickerDialog(Reminders.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(hourOfDay+" : "+minute);
                    }
                },mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
    }
}
