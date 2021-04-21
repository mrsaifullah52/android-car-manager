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
import java.util.Calendar;

public class Reminder extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;
    BaseAdapterForList adapter;

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


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                try {
                    Calendar cDate = Calendar.getInstance();
                    Calendar cTime = Calendar.getInstance();
                    cDate.setTimeInMillis(date.get(position));
                    cTime.setTimeInMillis(time.get(position));
                    cDate.add(Calendar.MONTH, 1);
                    String dateString = cDate.get(Calendar.DAY_OF_MONTH) + "/" + cDate.get(Calendar.MONTH) + "/" + cDate.get(Calendar.YEAR);
                    String timeString = cTime.get(Calendar.HOUR_OF_DAY) + " : " + cTime.get(Calendar.MINUTE) + " : " + cTime.get(Calendar.SECOND)  ;
                    new AlertDialog.Builder(Reminder.this)
                            .setTitle("Reminder Details")
//                           display message
                            .setMessage("----------------------------------\n\n"
                                    + "Title: " + title.get(position) + "\n\n"
                                    + "Date: " + dateString + "\n\n"
                                    + "Time: " + timeString + "\n\n")
                            .setCancelable(false)
                            .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            }).show();
                } catch (Exception e) {
                    Log.d("Notifications: ", e.getMessage());
                }
            }
        });

    }

}
