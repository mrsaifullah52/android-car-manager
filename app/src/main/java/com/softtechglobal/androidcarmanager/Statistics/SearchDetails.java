package com.softtechglobal.androidcarmanager.Statistics;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Database.ExpensesDB;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;
import com.softtechglobal.androidcarmanager.ViewImage;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchDetails extends AppCompatActivity {

    private DatabaseReference databaseReference1, databaseReference2;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;

    String vehicleId;

    String category;
    String index;
    ArrayList<String> indexList=new ArrayList<String>();

    TextView titleTv, categoryTv, meterReadingTv, costTv, dateTv, timeTv;
    ImageView imageView;
    LinearLayout imageContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_details);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        if (user == null) {
            finish();
            startActivity(new Intent(SearchDetails.this, Signin.class));
        }
        vehicleId = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key", "-1");
        Bundle intent=getIntent().getExtras();
        category=intent.getString("category");
        index = intent.getString("index");
        titleTv=(TextView) findViewById(R.id.titleTv);
        categoryTv=(TextView) findViewById(R.id.categoryTv);
        meterReadingTv=(TextView) findViewById(R.id.meterReadingTv);
        costTv=(TextView) findViewById(R.id.costTv);
        dateTv=(TextView) findViewById(R.id.dateTv);
        timeTv=(TextView) findViewById(R.id.timeTv);
        imageContainer=(LinearLayout) findViewById(R.id.imagesParent);
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/" + category);
        getData();
    }
    public void getData(){
        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                        indexList.add(ds.getKey());
                    }
                    String dbIndex=indexList.get(Integer.parseInt(index));
                    databaseReference2 = FirebaseDatabase.getInstance().getReference("users/" + user.getUid() + "/expenses/" + vehicleId + "/" + category);
                    databaseReference2.child(dbIndex).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){
                                ExpensesDB expensesDB=dataSnapshot.getValue(ExpensesDB.class);
                                titleTv.setText(expensesDB.getExpenseTitle());
                                categoryTv.setText(expensesDB.getExpenseType());
                                meterReadingTv.setText(expensesDB.getOdometer().toString());
                                costTv.setText(expensesDB.getCost().toString());
                                final Calendar date = Calendar.getInstance();
                                date.setTimeInMillis(expensesDB.getDate());
                                dateTv.setText(date.get(Calendar.DAY_OF_MONTH)+"/"+date.get(Calendar.MONTH)+"/"+date.get(Calendar.YEAR));
                                final Calendar time = Calendar.getInstance();
                                time.setTimeInMillis(expensesDB.getTime());
                                timeTv.setText(time.get(Calendar.HOUR_OF_DAY)+":"+time.get(Calendar.MINUTE)+":"+time.get(Calendar.MILLISECOND));
                                if(dataSnapshot.child("images").exists()){
                                    for (final DataSnapshot img : dataSnapshot.child("images").getChildren()){
                                        imageView=new ImageView(SearchDetails.this);
                                        imageView.setLayoutParams(new ViewGroup.LayoutParams(150,250));
                                        imageView.setMaxHeight(250);
                                        imageView.setMaxWidth(150);
                                        imageView.setPadding(10,0,10,0);
                                        Glide.with(SearchDetails.this)
                                                .load(img.getValue(String.class))
                                                .transition(DrawableTransitionOptions.withCrossFade())
                                                .transform(new RoundedCorners(20))
                                                .into(imageView);
                                        imageContainer.addView(imageView);
                                        imageView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent i=new Intent(SearchDetails.this, ViewImage.class);
                                                i.putExtra("imageurl", img.getValue(String.class));
                                                startActivity(i);
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("category child","doesnot exist");
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("category","doesnot exist 1");
            }
        });
    }
}