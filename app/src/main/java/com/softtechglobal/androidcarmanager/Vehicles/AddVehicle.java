package com.softtechglobal.androidcarmanager.Vehicles;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.softtechglobal.androidcarmanager.Database.VehicleDB;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.usermanagement.Signin;

import java.util.Calendar;

public class AddVehicle extends AppCompatActivity implements AdapterView.OnItemSelectedListener  {

    String vehicleName, manufacturer, vehicleModel, plateNum, odometerUnit;
    Long purchaseDate;
    Double milageRange, fuelLimit;
//  spinners
    String[] odometerUnitTypeSpin={"Kilometer","Mile","Hour"};

    Spinner odometerUnitSpin;
    EditText vehicleNameEt, manufacturerEt, vehicleModelEt, plateNumEt, purchaseDateEt, milageRangeEt, fuelLimitEt;
    Button saveVehicleBtn;
    DatePickerDialog datePickerDialog;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    int vehicleId;
    boolean status=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(AddVehicle.this, Signin.class));
        }
        final FirebaseUser user=firebaseAuth.getCurrentUser();
        databaseReference= FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/vehicles");

        odometerUnitSpin=(Spinner)findViewById(R.id.odometerUnit);
        odometerUnitSpin.setOnItemSelectedListener(this);

        vehicleNameEt=(EditText)findViewById(R.id.vehicleName);
        manufacturerEt=(EditText)findViewById(R.id.manufacturer);
        vehicleModelEt=(EditText)findViewById(R.id.vehicleModel);
        plateNumEt=(EditText)findViewById(R.id.licensePlateNum);
        purchaseDateEt=(EditText)findViewById(R.id.purchaseDate);
        milageRangeEt=(EditText)findViewById(R.id.mileageRange);
        fuelLimitEt=(EditText)findViewById(R.id.fuelLimit);

        saveVehicleBtn=(Button)findViewById(R.id.addVehicleBtn);


        ArrayAdapter adapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item, odometerUnitTypeSpin);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        odometerUnitSpin.setAdapter(adapter);

        purchaseDateEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar=Calendar.getInstance();
                int mYear=calendar.get(Calendar.YEAR);
                int mMonth=calendar.get(Calendar.MONTH);
                int mDay=calendar.get(Calendar.DAY_OF_MONTH);

//                setting date picker
                datePickerDialog = new DatePickerDialog(AddVehicle.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        purchaseDateEt.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        purchaseDate=calendar.getTimeInMillis();
                    }
                }, mYear,mMonth,mDay);
                datePickerDialog.show();
            }
        });

        saveVehicleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setValues();

                if(TextUtils.isEmpty(vehicleName)){
                    Toast.makeText(AddVehicle.this,"Enter Vehicle Name!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(manufacturer)){
                    Toast.makeText(AddVehicle.this,"Enter Manufacturer Name!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(vehicleModel)){
                    Toast.makeText(AddVehicle.this,"Enter Vehicle Model!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(plateNum)){
                    Toast.makeText(AddVehicle.this,"Enter License Plate Number!",Toast.LENGTH_SHORT).show();
                }else if(milageRange==null){
                    Toast.makeText(AddVehicle.this,"Enter Mileage Range!",Toast.LENGTH_SHORT).show();
                }else if(fuelLimit==null){
                    Toast.makeText(AddVehicle.this,"Enter Fuel Limit Name!",Toast.LENGTH_SHORT).show();
                }else if(TextUtils.isEmpty(odometerUnit)){
                    Toast.makeText(AddVehicle.this,"Enter Odometer Unit Type!",Toast.LENGTH_SHORT).show();
                }else if(purchaseDate==null){
                    Toast.makeText(AddVehicle.this,"Enter Odometer Unit Type!",Toast.LENGTH_SHORT).show();
                }else{
                    VehicleDB vehicleDB =new VehicleDB(vehicleName, odometerUnit, manufacturer, vehicleModel,

                    milageRange, fuelLimit, plateNum, purchaseDate);

//                    first method

                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.child("0").exists()){
                                vehicleId = Integer.parseInt(String.valueOf(snapshot.getChildrenCount()));
                                status=true;
                            }else{
                                vehicleId=0;
                                status=true;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(AddVehicle.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

//                    second method

//                    databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DataSnapshot> task) {
//                            if(task.isComplete()){
////                                vehicleId = Integer.parseInt(task.getResult().getKey())+1;
////                                Log.d("gekey", String.valueOf(task.getResult().getChildren()));
//
////                                DataSnapshot snapshot= (DataSnapshot) task.getResult().getChildren();
//
//                                Log.d("gekey", String.valueOf( task.getResult().or));
//                                if(task.getResult().child("1").exists()){
//
//                                    vehicleId = Integer.parseInt(task.getResult().getKey())+1;
//
//                                    status=true;
//                                }else{
//                                    vehicleId=0;
//                                    status=true;
//                                }
//                            }
//                        }
//                    });


//                    third method
//                    Query query=databaseReference.chi

                    if(status){
                        databaseReference.child(String.valueOf(vehicleId)).setValue(vehicleDB);
                        Toast.makeText(getApplicationContext(),"Vehicle information updated", Toast.LENGTH_LONG).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Please try Again", Toast.LENGTH_LONG).show();
                    }
                }


            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        odometerUnit=odometerUnitTypeSpin[position];
        Log.d("odometerUnitTypeSpin",odometerUnitTypeSpin[position]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        odometerUnit="";
    }

    public void setValues(){
        vehicleName=vehicleNameEt.getText().toString().trim();
        manufacturer=manufacturerEt.getText().toString().trim();
        vehicleModel=vehicleModelEt.getText().toString().trim();
        plateNum=plateNumEt.getText().toString().trim();
//        purchaseDate=Long.parseLong(purchaseDateEt.getText().toString());
        milageRange=Double.parseDouble(milageRangeEt.getText().toString());
        fuelLimit=Double.parseDouble(fuelLimitEt.getText().toString());
    }
}