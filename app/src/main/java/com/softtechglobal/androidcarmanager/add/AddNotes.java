package com.softtechglobal.androidcarmanager.add;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Database.NotesDB;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.Calendar;

public class AddNotes extends AppCompatActivity {

    EditText titleEt, messageEt, dateEt;
    Button saveBtn;

    String title="",message="";
    Long date;

    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseAuth firebaseAuth;
    String key="";
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notes);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(AddNotes.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");
        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/notes/"+key);
//        databaseReference2=FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/notes/"+key);

        titleEt=(EditText) findViewById(R.id.notesTitleEt);
        messageEt=(EditText) findViewById(R.id.notesMessageEt);
        dateEt=(EditText) findViewById(R.id.notesDateEt);
        saveBtn=(Button) findViewById(R.id.addNoteBtn);
        setTitle("Add Note");


        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int mYear = calendar.get(Calendar.YEAR);
                int mMonth = calendar.get(Calendar.MONTH);
                int mDay = calendar.get(Calendar.DAY_OF_MONTH);

                new DatePickerDialog(AddNotes.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        calendar.set(year, month, dayOfMonth);
                        date=calendar.getTimeInMillis();
                    }
                }, mYear, mMonth, mDay).show();
            }
        });




        Bundle i = getIntent().getExtras();
        final String type=i.getString("type");
        if(type.equals("edit")){
            titleEt.setText(i.getString("title"));
            messageEt.setText(i.getString("message"));
        }

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title=titleEt.getText().toString().trim();
                message=messageEt.getText().toString().trim();
                if(key.equals("-1")){
                    Toast.makeText(AddNotes.this,"Please Select a Car Before Adding Note!",Toast.LENGTH_SHORT).show();
                }else if(title.isEmpty()){
                    Toast.makeText(AddNotes.this,"Enter Title!",Toast.LENGTH_SHORT).show();
                }else if(message.isEmpty()){
                    Toast.makeText(AddNotes.this,"Enter Message!",Toast.LENGTH_SHORT).show();
                }else if(date == null){
                    Toast.makeText(AddNotes.this,"Select Date!",Toast.LENGTH_SHORT).show();
                }else{
                    addNoteIntoFirebase(type);
                }
            }
        });

    }

    public void addNoteIntoFirebase(final String type){
        final NotesDB notesDB = new NotesDB(title, message, date);
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        position= Integer.parseInt(ds.getKey());
                    }
                    position++;
                }else{
                    position=0;
                }

                if(type.equals("add")){
                    databaseReference.child(String.valueOf(position)).setValue(notesDB);
                    clearEtValue();
                    Toast.makeText(AddNotes.this, "Note Added!", Toast.LENGTH_SHORT).show();
                }else if(type.equals("edit")) {
                    Bundle bundle=getIntent().getExtras();
                    String noteKey=bundle.getString("key");
                    databaseReference.child(noteKey).setValue(notesDB);
                    clearEtValue();
                    Toast.makeText(AddNotes.this, "Note Updated!", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void clearEtValue(){
        titleEt.setText("");
        messageEt.setText("");
        dateEt.setText("");
    }
}
