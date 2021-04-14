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
import com.softtechglobal.androidcarmanager.Views.BaseAdapterForList;
import com.softtechglobal.androidcarmanager.Database.NotesDB;
import com.softtechglobal.androidcarmanager.Views.ModelForList;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;

public class Notes extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;
    BaseAdapterForList adapter;

    ArrayList<String> title= new ArrayList<String>();
    ArrayList<Long> date= new ArrayList<Long>();
    ArrayList<String> message= new ArrayList<String>();
    ArrayList<ModelForList> listModel=new ArrayList<ModelForList>();

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    String key="";
    ArrayList<String> keys=new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(Notes.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");
        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/notes/"+key);

        getSupportActionBar().hide();
        imageButton=(ImageButton)findViewById(R.id.addNotes);
        listView=(ListView)findViewById(R.id.noteslist);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Notes.this, AddNotes.class);
                i.putExtra("type","add");
                startActivity(i);
            }
        });


        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        NotesDB notesDB=ds.getValue(NotesDB.class);
                        keys.add(ds.getKey());
                        title.add(notesDB.getTitle());
                        message.add(notesDB.getMessage());
                        date.add(notesDB.getDate());
                    }

                }else {
                    Toast.makeText(Notes.this, "Please Select a Vehicle before adding Note.",Toast.LENGTH_SHORT).show();
                }

                if (!title.isEmpty() && !date.isEmpty() && !message.isEmpty()){
                    for (int i=0;i<=title.size()-1;i++){
                        ModelForList model=new ModelForList(title.get(i),date.get(i));
                        listModel.add(model);
                    }
                    adapter = new BaseAdapterForList(Notes.this, listModel);
                    listView.setAdapter(adapter);
                }else{
                    Log.d("listmodel","failed to get values");
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(Notes.this);
                    builder.setTitle("Choose an option");
                    String[] options={"View","Edit","Delete"};
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:{
                                    try {
                                        Calendar c = Calendar.getInstance();
                                        c.setTimeInMillis(date.get(position));
                                        c.add(Calendar.MONTH,1);
                                        String dateString= c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
                                        new AlertDialog.Builder(Notes.this)
                                                .setTitle(title.get(position))
                                                .setMessage(
                                                        "Date: "+dateString+"\n\n"
                                                        +"----------------------------------\n\n"
                                                        +"Message: "+message.get(position)+" \n"
                                                )
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
                                    Intent i = new Intent(Notes.this, AddNotes.class);
                                    i.putExtra("type", "edit");
                                    i.putExtra("key", keys.get(position) );
                                    i.putExtra("title", title.get(position));
                                    i.putExtra("message", message.get(position));
                                    startActivity(i);
                                }break;
                                case 2:{
                                    databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                        ArrayList<String> index=new ArrayList<String>();
                                        @Override
                                        public void onSuccess(DataSnapshot dataSnapshot) {
                                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                                String key=ds.getKey();
                                                if(!key.isEmpty()){
                                                    index.add(key);
                                                }
                                            }
                                            removeItem(Integer.parseInt(index.get(position)), position);
                                        }
                                    });
                                }break;
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
            }
        });

    }

    public boolean removeItem(final int dbPosition, final int listPosition){
        Log.d("position", String.valueOf(dbPosition));
        boolean status;
        if(dbPosition<0){
            status=false;
        }else{
            status=true;
            databaseReference.child(String.valueOf(dbPosition)).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    listModel.remove(listPosition);
                    title.remove(listPosition);
                    message.remove(listPosition);
                    date.remove(listPosition);
                    adapter.notifyDataSetChanged();
                }
            });
        }
        return status;
    }
}
