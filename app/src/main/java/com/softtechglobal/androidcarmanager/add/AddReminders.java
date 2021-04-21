package com.softtechglobal.androidcarmanager.add;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Database.RemindersDB;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class AddReminders extends AppCompatActivity {

    EditText titleEt, dateEt, timeEt;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    Button addReminderBtn;

    String key;
    ArrayList<String> keys = new ArrayList<String>();
    private DatabaseReference databaseReference1;
    private FirebaseAuth firebaseAuth;
    int mYear;
    int mMonth;
    int mDay;

    Long date, time;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_reminders);
        setTitle("Add Reminder");


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(AddReminders.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");

//      creating channel
        createNotificationChannel();

        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/reminders/");

        titleEt=(EditText)findViewById(R.id.reminderTitleEt);
        dateEt=(EditText)findViewById(R.id.reminderDateEt);
        timeEt=(EditText)findViewById(R.id.reminderTimeEt);
        addReminderBtn=(Button)findViewById(R.id.addReminderBtn);

//      setting empty value by default
        titleEt.setText("");
        dateEt.setText("");
        timeEt.setText("");

        Bundle i = getIntent().getExtras();
//      check type
        if(i.getString("type").equals("edit")){
            titleEt.setText(i.getString("title"));
            dateEt.setText(i.getString("date"));
            timeEt.setText(i.getString("time"));
        }

//      datepicker
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

//                setting date picker
                datePickerDialog = new DatePickerDialog(AddReminders.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        calendar.set(year, month, dayOfMonth);
                        date = calendar.getTimeInMillis();
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });
//      timepicker
        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Date dateObjForTime = new Date();

                final Calendar calendar=Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                int mHour=calendar.get(Calendar.HOUR_OF_DAY);
                final int mMinute=calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddReminders.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEt.setText(hourOfDay+" : "+minute);

                        Log.d("chours", String.valueOf( calendar.get(Calendar.HOUR_OF_DAY)));
                        Log.d("cminute", String.valueOf( calendar.get(Calendar.MINUTE)));

                        Log.d("hours", String.valueOf(hourOfDay));
                        Log.d("minute", String.valueOf(minute));

                        if(hourOfDay == calendar.get(Calendar.HOUR_OF_DAY)){
//                            calendar.add(Calendar.HOUR_OF_DAY, hourOfDay);
                        }else{
                            calendar.add(Calendar.HOUR_OF_DAY, (calendar.get(Calendar.HOUR_OF_DAY) - hourOfDay));
                        }
                        if(minute == calendar.get(Calendar.MINUTE)){
//                            calendar.add(Calendar.MINUTE, minute);
                        }else{
                            calendar.add(Calendar.MINUTE, (minute-calendar.get(Calendar.MINUTE)));
                        }

                        Log.d("ahours", String.valueOf( calendar.get(Calendar.HOUR_OF_DAY)));
                        Log.d("aminute", String.valueOf( calendar.get(Calendar.MINUTE)));

                        time=calendar.getTimeInMillis();
//                        time=calendar.get(Calendar.HOUR) + calendar.get(Calendar.MINUTE) + dateObjForTime.getTime();
                    }
                },mHour, mMinute, false);
                timePickerDialog.show();
            }
        });


//       getting keys from firebase
        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()){
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    keys.add(ds.getKey());
                }
                Log.d("databaseReference1", "Called Successfully" );
            }else{
//                keys.add(String.valueOf(0));
            }
            }
        });




        addReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            String index;

            if(keys.size() > 0){
                index = String.valueOf(Integer.parseInt(keys.get(keys.size()-1)) + 1);
                Log.d("index1 is: ", index );
            }else{
                index = String.valueOf(0);
                Log.d("index2 is: ", index);
            }


            getValuesFromEt();

            RemindersDB remindersDB=new RemindersDB(title, date, time);


                Date dateObj = new Date();
                Calendar cal_now = Calendar.getInstance();
                cal_now.setTime(dateObj);

                if (date > cal_now.getTimeInMillis() || time > cal_now.getTimeInMillis()){
                    final String in=index;
                    databaseReference1.child(in).setValue(remindersDB).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddReminders.this, "Reminder added to Firebase", Toast.LENGTH_SHORT).show();


                            Calendar cal_set = Calendar.getInstance();
                            Calendar cal_trigger_date = Calendar.getInstance();
                            Calendar cal_trigger_time = Calendar.getInstance();

                            int day, month, year, hours, minutes;
                            cal_trigger_date.setTimeInMillis(date);
                            cal_trigger_time.setTimeInMillis(time);

                            day = cal_trigger_date.get(Calendar.DAY_OF_MONTH);
                            month = cal_trigger_date.get(Calendar.MONTH);
                            year = cal_trigger_date.get(Calendar.YEAR);

                            hours = cal_trigger_time.get(Calendar.HOUR_OF_DAY);
                            minutes = (cal_trigger_time.get(Calendar.MINUTE) ) ;

                            cal_set.set(year, month, day, hours, minutes);


                            Log.d("cal_set", cal_set.get(Calendar.MONTH) + 1 + "/" + cal_set.get(Calendar.DAY_OF_MONTH)
                                    + " | " + cal_set.get(Calendar.HOUR_OF_DAY) + ":" + (cal_set.get(Calendar.MINUTE))
                            );

                            AlarmManager manager = (AlarmManager) getSystemService(AddReminders.ALARM_SERVICE);
                            Intent myIntent = new Intent(AddReminders.this, ReminderBroadcast.class);
                            myIntent.putExtra("Title", "Android Car Manager Reminds you!!");
                            myIntent.putExtra("Description", title);
                            myIntent.putExtra("index", in);
                            PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminders.this, 0, myIntent, Intent.FILL_IN_DATA);

                            manager.set(AlarmManager.RTC_WAKEUP, cal_set.getTimeInMillis(), pendingIntent);
                            Toast.makeText(AddReminders.this, "Alarm setuped", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(AddReminders.this, "Date & Time should be greater than Current Date & Time.", Toast.LENGTH_SHORT).show();
                }
            }






//                AlarmManager manager = (AlarmManager) getSystemService(AddReminders.ALARM_SERVICE);
//
//                Date dat = new Date();
//                Calendar cal_alarm = Calendar.getInstance();
//                Calendar cal_now = Calendar.getInstance();
//
//                cal_now.setTime(dat);
//                cal_alarm.setTime(dat);
//
////                cal_alarm.set(Calendar.HOUR_OF_DAY,12);
//                cal_alarm.set(Calendar.MINUTE,0);
//                cal_alarm.set(Calendar.SECOND,30);
//                if(cal_alarm.before(cal_now)){
//                    cal_alarm.add(Calendar.DATE,1);
//                }
//
//                Intent myIntent = new Intent(AddReminders.this, ReminderBroadcast.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddReminders.this, 0, myIntent, 0);
//
//                manager.set(AlarmManager.RTC_WAKEUP, cal_now.getTimeInMillis() + (1000*10), pendingIntent);
//                Toast.makeText(AddReminders.this,"Alarm setuped",Toast.LENGTH_SHORT).show();
//            }


        });

    }
    public void getValuesFromEt() {
        title=titleEt.getText().toString().trim();
    }

    public void createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "Android Car Manager";
            String description = "Channel for Android Car Manager";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("AndroidCarManager",name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
