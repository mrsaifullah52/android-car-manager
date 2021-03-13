package com.softtechglobal.androidcarmanager.add;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.softtechglobal.androidcarmanager.R;

public class Notes extends AppCompatActivity {

    EditText title,message;
    Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        title=(EditText) findViewById(R.id.addTitleEt);
        message=(EditText) findViewById(R.id.addMessageEt);
        save=(Button) findViewById(R.id.addNoteBtn);
        setTitle("Add Notes");


        Bundle i = getIntent().getExtras();
        if(i.getString("type").equals("edit")){

            title.setText(i.getString("title"));
            message.setText(i.getString("message"));

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                edit the info
//                    finish();



                }
            });
        }else{
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                  save the info
                    finish();
                }
            });
        }

    }
}
