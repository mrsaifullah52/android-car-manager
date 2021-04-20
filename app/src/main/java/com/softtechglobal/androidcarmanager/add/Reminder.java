package com.softtechglobal.androidcarmanager.add;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import com.softtechglobal.androidcarmanager.Views.BaseAdapterForList;
import com.softtechglobal.androidcarmanager.Views.ModelForList;

import java.util.ArrayList;

public class Reminder extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;
    BaseAdapterForList adapter;
    ModelForList modelForList;

    ArrayList<String> title= new ArrayList<String>();
    ArrayList<Long> date= new ArrayList<Long>();
    ArrayList<Long> time= new ArrayList<Long>();
    ArrayList<ModelForList> list=new ArrayList<ModelForList>();

    private DatabaseReference databaseReference1;
    private FirebaseAuth firebaseAuth;

    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().hide();


        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Reminder.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/reminders/");


        imageButton=(ImageButton)findViewById(R.id.addReminders);
        listView=(ListView)findViewById(R.id.remainderslist);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Reminder.this, AddReminders.class);
                i.putExtra("type", "add");
                startActivity(i);
            }
        });

//        AlarmManager mAlarmManager = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//
//        this.registerReceiver(new AlarmReceiver(), new IntentFilter("AlarmAction"));
//        PendingIntent broadcast = PendingIntent.getBroadcast(this, 0, new Intent(Reminder.this, ReminderBroadcast.class), 0);
//
//        // Add dynamic time calculations. For testing just +100 milli.
//        mAlarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000*10), broadcast);
        ;


        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds: dataSnapshot.getChildren()){
                        RemindersDB remindersDB = ds.getValue(RemindersDB.class);
                        title.add(remindersDB.getTitle());
                        date.add(remindersDB.getDate());
                        time.add(remindersDB.getTime());
                    }
                }

                if(!title.isEmpty() && !date.isEmpty()){
                    for (int i=0; i < title.size(); i++){
                        ModelForList modelForList=new ModelForList(title.get(i), date.get(i));
                        list.add(modelForList);
                    }

                    adapter = new BaseAdapterForList(Reminder.this, list);
                    listView.setAdapter(adapter);
                }else{
                    Toast.makeText(Reminder.this, "Failed to fetch data, try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


//        adding titles
//        title.add("I've to do Car Service in this week.");

//        adding dates
//        date.add("06/03/2021");

//        adding messages
//        time.add("20:14");




        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Reminder.this);
                builder.setTitle("Choose an option");
                String[] options={"View","Edit","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                Toast.makeText(Reminder.this,"Viewed",Toast.LENGTH_SHORT).show();
                                try {
                                    new AlertDialog.Builder(Reminder.this)
                                            .setTitle("Detail of Reminder")
                                            .setMessage("Date: "+date.get(position)+"\n\n"+title.get(position))
                                            .setCancelable(false)
                                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            }).show();
                                } catch (Exception e) {
                                    Log.d("Notifications: ", e.getMessage());
                                }
                            }break;
                            case 1:{
                                Intent i = new Intent(Reminder.this, AddReminders.class);
                                i.putExtra("type", "edit");
                                i.putExtra("title", title.get(position));
                                i.putExtra("date", date.get(position));
                                i.putExtra("time", time.get(position));
                                startActivity(i);
                            }break;
                            case 2:{
                                boolean status = removeItem(position);
                                if(status){
                                    adapter.notifyDataSetChanged();
                                }else{
                                    Toast.makeText(Reminder.this, "Failed to Delete, try again", Toast.LENGTH_SHORT).show();
                                }
                            }break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    public boolean removeItem(int position){
        boolean status;
        if(position<0){
            status=false;
        }else{
            status=true;
            title.remove(position);
            date.remove(position);
            time.remove(position);
        }
        return status;
    }
}
