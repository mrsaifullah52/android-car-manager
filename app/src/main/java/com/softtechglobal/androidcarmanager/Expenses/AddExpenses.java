package com.softtechglobal.androidcarmanager.Expenses;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.softtechglobal.androidcarmanager.Database.ExpensesDB;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class AddExpenses extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LinearLayout linearLayout;
    Spinner expenseSpinner;
    String[] expenses={"Add Odometer","Maintenance","Fuel","Purchase","Service","Fine","Tax"};
    String selectedExpenses;
//  data to put
    String title;
    Long date, time;
    Double meter, cost, ltr;

    TextInputLayout fuelContainer;
    EditText titleEt, dateEt,timeEt, meterReadingEt, costEt, ltrEt;
    Button saveBtn;
    ImageView addImgCamera, addImgGallery;
    LinearLayout imagesContainer;

    private int PICK_IMAGE=786;
    private int READ_PERMISSION=787;
    private int CAMERA_REQUEST=788;
    private int CAMERA_PERMISSION=789;
    private Uri filePath;
    ArrayList<Uri> imagesPathList=new ArrayList<Uri>();
    ArrayList<String> imagesDbPathList=new ArrayList<String>();

    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;

    private DatabaseReference databaseReference1, databaseReference2;
    private FirebaseAuth firebaseAuth;
    private StorageReference storageReference;
    private FirebaseStorage storage;

    String key;
    int expensesIndex;
    ArrayList<String> keys = new ArrayList<String>();

    int mYear;
    int mMonth;
    int mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addexpenses);

        firebaseAuth = FirebaseAuth.getInstance();
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(AddExpenses.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        key= getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key","-1");
        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/vehicles/");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/expenses/"+key);

        storage = FirebaseStorage.getInstance();
        storageReference=storage.getReference("expenses/"+user.getUid());


        setTitle("Add Expenses");
        expenseSpinner=(Spinner)findViewById(R.id.expenseTitleSp);
        titleEt=(EditText) findViewById(R.id.expenseTitleEt);
        dateEt=(EditText) findViewById(R.id.expenseDateEt);
        timeEt=(EditText) findViewById(R.id.expenseTimeEt);
        meterReadingEt=(EditText) findViewById(R.id.meterEt);
        costEt=(EditText) findViewById(R.id.costEt);
        ltrEt=(EditText) findViewById(R.id.ltrEt);
        saveBtn=(Button) findViewById(R.id.saveExpenseBtn);
        addImgCamera=(ImageView) findViewById(R.id.addImgCamera);
        addImgGallery=(ImageView) findViewById(R.id.addImgGallery);
        imagesContainer= (LinearLayout) findViewById(R.id.imagesContainer);
        fuelContainer= (TextInputLayout) findViewById(R.id.fuelContainer);
        expenseSpinner.setOnItemSelectedListener(this);

//      setting empty value by default
        titleEt.setText("");
        dateEt.setText("");
        timeEt.setText("");
        meterReadingEt.setText("0");
        costEt.setText("0");
        ltrEt.setText("0");


//      add / select images
        addImgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(AddExpenses.this, Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED){
                    ActivityCompat.requestPermissions(AddExpenses.this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERMISSION);
                }else{
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });

        addImgGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ActivityCompat.checkSelfPermission(AddExpenses.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    Intent i=new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select an Picture"), PICK_IMAGE);
                } else {
                    ActivityCompat.requestPermissions((Activity) AddExpenses.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION);
                }
            }
        });

//      seting date and time picker
        dateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

//                setting date picker
                datePickerDialog = new DatePickerDialog(AddExpenses.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        dateEt.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                        calendar.set(year, month, dayOfMonth);
                        date = calendar.getTimeInMillis();
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        timeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mHour=calendar.get(Calendar.HOUR_OF_DAY);
                int mMinute=calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(AddExpenses.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timeEt.setText(hourOfDay+" : "+minute);
                        time=calendar.getTimeInMillis();
                    }
                },mHour, mMinute, true);
                timePickerDialog.show();
            }
        });


        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,expenses);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        expenseSpinner.setAdapter(adapter);

        databaseReference1.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    keys.add(ds.getKey());
                }
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("SaveBtn: ","Button Clicked");

                if(selectedExpenses.equals("Add Odometer")){
                    getValuesFromEt(true, false);
                }else{
                    if(selectedExpenses.equals("Fuel")){
                        Log.d("SaveBtn: ","Fuel addition function called 1");
                        getValuesFromEt(false, true);
                    }else{
                        Log.d("SaveBtn: ","Fuel addition function called 2");
                        getValuesFromEt(false, false);
                    }
                }

                if(key.equals("-1")) {
                    Toast.makeText(AddExpenses.this, "Please Select a Car Before Adding Expense!", Toast.LENGTH_LONG).show();
                }else if (selectedExpenses.isEmpty()){
                    Toast.makeText(AddExpenses.this,"Select Expense Category!",Toast.LENGTH_SHORT).show();
                }else if(date==null){
                    Toast.makeText(AddExpenses.this,"Enter Date!",Toast.LENGTH_SHORT).show();
                }else if(time==null){
                    Toast.makeText(AddExpenses.this,"Enter Time!",Toast.LENGTH_SHORT).show();
                }else if(meter==null){
                    Toast.makeText(AddExpenses.this,"Enter Meter Reading!",Toast.LENGTH_SHORT).show();
                }else if(selectedExpenses.equals("Add Odometer")) {
//                  if adding oddometer
                    databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            Integer i=0;
                            if (dataSnapshot.child("Oddometer").exists()) {
                                for (DataSnapshot ds : dataSnapshot.child("Oddometer").getChildren()) {
                                    i = Integer.valueOf(ds.getKey());
                                    Log.d("oddomter index: ", ds.getKey());
                                }
                                i++;
                                addOdometer(i);
                            }else{
                                addOdometer(0);
                            }
                        }
                    });

                }else if(!selectedExpenses.equals("Add Odometer")){
//                  checking if it isn't add odometer
                    if(title==null){
                        Toast.makeText(AddExpenses.this,"Enter Title!",Toast.LENGTH_SHORT).show();
                    }else if(cost==null){
                        Toast.makeText(AddExpenses.this,"Enter Cost!",Toast.LENGTH_SHORT).show();
                    }else if(selectedExpenses.equals("Fuel")){
                        if (ltr==null){
                            Toast.makeText(AddExpenses.this,"Enter Litters!",Toast.LENGTH_SHORT).show();
                        }else{
                            getDBIndex(true);
                            Log.d("SaveBtn: ", "Check for Null");
                        }
                    }else{
                        int position=keys.indexOf(key);
                        if(position == -1){
                            Toast.makeText(AddExpenses.this, "Please Select an Existing Car!", Toast.LENGTH_LONG).show();
                        }else {
//                            call function
                            getDBIndex(false);
                        }
                }

            }

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            filePath=data.getData();
            imagesPathList.add(filePath);

            Bitmap photo=(Bitmap) data.getExtras().get("data");
            ImageView imageView=new ImageView(AddExpenses.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(150,250));
            imageView.setMaxHeight(250);
            imageView.setMaxWidth(150);
            imageView.setPadding(10,0,10,0);
            imageView.setImageBitmap(photo);
            imagesContainer.addView(imageView);
        }else if(requestCode == PICK_IMAGE && resultCode == RESULT_OK
                && data != null && data.getData() != null){
            filePath=data.getData();
            imagesPathList.add(filePath);

            try {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ImageView imageView=new ImageView(AddExpenses.this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(150,250));
                imageView.setMaxHeight(250);
                imageView.setMaxWidth(150);
                imageView.setPadding(10,0,10,0);
                imageView.setImageBitmap(bitmap);
                imagesContainer.addView(imageView);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selectedExpenses=expenses[position];
        if(selectedExpenses.equals("Add Odometer")){
//          adding odometer
            linearLayout=(LinearLayout)findViewById(R.id.ex1);
            linearLayout.setVisibility(View.GONE);
            linearLayout=(LinearLayout)findViewById(R.id.ex6);
            linearLayout.setVisibility(View.GONE);
            linearLayout=(LinearLayout)findViewById(R.id.ex7);
            linearLayout.setVisibility(View.GONE);
        }else if(! selectedExpenses.equals("Add Odometer") ){
            linearLayout=(LinearLayout)findViewById(R.id.ex1);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout=(LinearLayout)findViewById(R.id.ex6);
            linearLayout.setVisibility(View.VISIBLE);
            linearLayout=(LinearLayout)findViewById(R.id.ex7);
            linearLayout.setVisibility(View.VISIBLE);
//            if adding fuel expense
            if( selectedExpenses.equals("Fuel")){
                fuelContainer.setVisibility(View.VISIBLE);
            }else{
                fuelContainer.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getDBIndex(final Boolean type){
        databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(selectedExpenses).exists()){
                    for (DataSnapshot ds : dataSnapshot.child(selectedExpenses).getChildren()) {
                        expensesIndex = Integer.parseInt(ds.getKey());
                        Log.d("expensesIndex1", String.valueOf(expensesIndex));
                    }
                    expensesIndex++;
//                    Log.d("expensesIndex2", String.valueOf(expensesIndex));
                    if(type){
                        addIntoDb(expensesIndex, true);
                    }else{
                        addIntoDb(expensesIndex, false);
                    }
//                  expensesIndex = Integer.parseInt(dataSnapshot.child(selectedExpenses).getKey());
                }else{
//                    Log.d("expensesIndex3", String.valueOf(expensesIndex));
                    expensesIndex=0;
                    if(type){
                        addIntoDb(expensesIndex, true);
                    }else{
                        addIntoDb(expensesIndex, false);
                    }
                }
            }
        });
    }

    public void addOdometer(int in){
        final int i=in;

        final ExpensesDB expensesDB=new ExpensesDB(date, time, meter);
        databaseReference2.child("Oddometer").child(String.valueOf(i))
                .setValue(expensesDB).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                clearEtValue();
                Toast.makeText(AddExpenses.this, "Oddometer Reading added!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddExpenses.this, "Failed to add, "+e.toString()+"!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addIntoDb(int in, Boolean type){
        final int index=in;
        ExpensesDB expensesDB;
        if (type){
            expensesDB = new ExpensesDB(title, selectedExpenses, date, time, meter, cost, ltr);
        }else{
            expensesDB = new ExpensesDB(title, selectedExpenses, date, time, meter, cost);
        }
        if (imagesPathList.isEmpty()){
            databaseReference2.child(selectedExpenses).child(String.valueOf(index)).setValue(expensesDB)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            clearEtValue();
                            Toast.makeText(AddExpenses.this, "Expense Added!", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AddExpenses.this, "Failed to add: "+e.toString(), Toast.LENGTH_LONG).show();
                }
            });
        }else{
//            ExpensesDB expensesDB;
//            if (selectedExpenses.equals("Fuel")){
//                expensesDB = new ExpensesDB(title, selectedExpenses, date, time, meter, cost, ltr);
//            }else{
//                expensesDB = new ExpensesDB(title, selectedExpenses, date, time, meter, cost);
//            }
            databaseReference2.child(selectedExpenses).child(String.valueOf(index)).setValue(expensesDB)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    for (int i=0; i < imagesPathList.size(); i++){
                        final int j=i;
                        storageReference
                                .child(selectedExpenses)
                                .child(key)
                                .child(String.valueOf(index))
                                .child(String.valueOf(i))
                                .putFile(imagesPathList.get(i))
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                storageReference
                                        .child(selectedExpenses)
                                        .child(key)
                                        .child(String.valueOf(index))
                                        .child(String.valueOf(j))
                                        .getDownloadUrl()
                                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        imagesDbPathList.add(String.valueOf(uri));
                                        if ( j == imagesPathList.size()-1 ){
                                            addImageIntoFirebase(index);
                                         }
                                    }
                                });
                            }
                        });
                    }

                }
            });
        }
    }

    public void addImageIntoFirebase(int index){
        for(int i=0; i<imagesDbPathList.size(); i++) {
            databaseReference2.child(selectedExpenses).child(String.valueOf(index))
                    .child("images")
                    .child(String.valueOf(i))
                    .setValue(imagesDbPathList.get(i))
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(AddExpenses.this, "Expense Added!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(AddExpenses.this, "Failed"+e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        imagesContainer.removeAllViews();
        clearEtValue();
    }

    public void clearEtValue(){
        titleEt.setText("");
        dateEt.setText("");
        timeEt.setText("");
        meterReadingEt.setText("");
        costEt.setText("");
        ltrEt.setText("");
    }

    public void getValuesFromEt(Boolean type, Boolean category){
        if(type){
            meter= Double.valueOf(meterReadingEt.getText().toString());
        }else{
            if(category){
                Log.d("SaveBtn: ","getValuesFromEt() called");
                meter= Double.valueOf(meterReadingEt.getText().toString());
                title= titleEt.getText().toString().trim();
                cost= Double.valueOf(costEt.getText().toString().trim());
                ltr= Double.valueOf(ltrEt.getText().toString().trim());
            }else{
                meter= Double.valueOf(meterReadingEt.getText().toString());
                title= titleEt.getText().toString().trim();
                cost= Double.valueOf(costEt.getText().toString().trim());
            }
        }
    }
}
