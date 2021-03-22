package com.softtechglobal.androidcarmanager.add;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

import java.util.Calendar;

public class AddReminders extends AppCompatActivity {

    EditText titleEt, dateEt, timeEt;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Button addReminderBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminders);
        setTitle("Add Reminder");

        titleEt=(EditText)findViewById(R.id.reminderTitleEt);
        dateEt=(EditText)findViewById(R.id.reminderDateEt);
        timeEt=(EditText)findViewById(R.id.reminderTimeEt);
        addReminderBtn=(Button)findViewById(R.id.addReminderBtn);

        Bundle i = getIntent().getExtras();
//      check type
        if(i.getString("type").equals("edit")){
            titleEt.setText(i.getString("title"));
            dateEt.setText(i.getString("date"));
            timeEt.setText(i.getString("time"));
        }


        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mYear=calendar.get(Calendar.YEAR);
                int mMonth=calendar.get(Calendar.MONTH);
                int mDay=calendar.get(Calendar.DAY_OF_MONTH);

//                setting date picker
                datePickerDialog = new DatePickerDialog(AddReminders.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEt.setText(dayOfMonth+"/"+(month+1)+"/"+year);
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

                timePickerDialog = new TimePickerDialog(AddReminders.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEt.setText(hourOfDay+" : "+minute);
                    }
                },mHour, mMinute, false);
                timePickerDialog.show();
            }
        });

        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now=Calendar.getInstance();

                AlarmManager manager = (AlarmManager) getSystemService(AddReminders.ALARM_SERVICE);
//                Date dat = new Date();
                Calendar cal_alarm = Calendar.getInstance();
                Calendar cal_now = Calendar.getInstance();
                cal_alarm.add(Calendar);
//                cal_now.setTime(dat);
//                cal_alarm.setTime(dat);
                cal_alarm.set(Calendar.HOUR_OF_DAY,14);
                cal_alarm.set(Calendar.MINUTE,18);
                cal_alarm.set(Calendar.SECOND,0);
                if(cal_alarm.before(cal_now)){
                    cal_alarm.add(Calendar.DATE,1);
                }

                Intent myIntent = new Intent(AddReminders.this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminders.this, 0, myIntent, 0);

                manager.set(AlarmManager.RTC_WAKEUP,cal_alarm.getTimeInMillis(), pendingIntent);


            }
        });

    }
}
