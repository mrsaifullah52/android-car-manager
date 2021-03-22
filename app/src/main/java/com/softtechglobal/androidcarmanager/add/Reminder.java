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

import com.softtechglobal.androidcarmanager.CustomBaseAdapter;
import com.softtechglobal.androidcarmanager.R;

import java.util.ArrayList;

public class Reminder extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;
    CustomBaseAdapter adapter;

    ArrayList<String> title= new ArrayList<String>();
    ArrayList<String> date= new ArrayList<String>();
    ArrayList<String> time= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);
        getSupportActionBar().hide();

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

//        adding titles
        title.add("I've to do Car Service in this week.");

//        adding dates
        date.add("06/03/2021");

//        adding messages
        time.add("20:14");

//        adapter = new CustomBaseAdapter(AddReminder.this, title, date);
//        listView.setAdapter(adapter);


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
