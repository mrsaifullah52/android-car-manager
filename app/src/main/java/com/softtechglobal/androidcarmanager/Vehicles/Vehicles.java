package com.softtechglobal.androidcarmanager.Vehicles;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.Views.BaseAdapterForList;
import com.softtechglobal.androidcarmanager.Database.VehicleDB;
import com.softtechglobal.androidcarmanager.MainActivity;
import com.softtechglobal.androidcarmanager.Views.ModelForList;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;

public class Vehicles extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;
    BaseAdapterForList adapter;

    ArrayList<String> key=new ArrayList<String>();
    ArrayList<String> title=new ArrayList<String>();
    ArrayList<String> odometerUnit=new ArrayList<String>();
    ArrayList<String> manufacturer=new ArrayList<String>();
    ArrayList<Long> purchaseDate=new ArrayList<Long>();
    ArrayList<String> model=new ArrayList<String>();
    ArrayList<String> milage=new ArrayList<String>();
    ArrayList<String> fuelLimit=new ArrayList<String>();
    ArrayList<String> plateNum=new ArrayList<String>();

    ArrayList<ModelForList> listModel= new ArrayList<ModelForList>();

    AlertDialog.Builder builder;
    AlertDialog dialog;

    private DatabaseReference databaseReference, databaseReference2;
    private FirebaseAuth firebaseAuth;

    boolean status;
    ProgressDialog progressDialog1, progressDialog;
    ChildEventListener childEventListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicles);
        getSupportActionBar().hide();
        imageButton = (ImageButton)findViewById(R.id.addVehicle);
        listView = (ListView)findViewById(R.id.vehicleslist);

//      check user is loggedin or not
        firebaseAuth=FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(Vehicles.this, Signin.class));
        }


        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/vehicles");
        databaseReference2 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid());
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

//      add new vehicle details
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Vehicles.this, AddVehicle.class);
                i.putExtra("type", "add");
                startActivity(i);
            }
        });

        progressDialog1 = ProgressDialog.show(Vehicles.this, "","Please Wait, Loading...");

        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot ds:dataSnapshot.getChildren()){
//                      Map<String,Object> myVal=(HashMap<String,Object>) dataSnapshot.getValue();
                        VehicleDB vehicleDB=ds.getValue(VehicleDB.class);
                        key.add(ds.getKey());
                        title.add(String.valueOf(vehicleDB.getVehicleName()));
                        purchaseDate.add((Long) vehicleDB.getPurchaseDate());
                        odometerUnit.add(String.valueOf(vehicleDB.getModometerReading()));
                        manufacturer.add(String.valueOf(vehicleDB.getManufacturer()));
                        model.add(String.valueOf(vehicleDB.getVehicleModel()));
                        milage.add(String.valueOf(vehicleDB.getMileageRange()));
                        fuelLimit.add(String.valueOf(vehicleDB.getFuelLimit()));
                        plateNum.add(String.valueOf(vehicleDB.getPlateNumber()));
                    }

                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
                        for(int i=0;i<title.size();i++){
                            ModelForList modelAdapter=new ModelForList(title.get(i), purchaseDate.get(i));
                            //bind all strings in an array
                            listModel.add(modelAdapter);
                        }
                        setListAdapter();
                        progressDialog1.dismiss();
                    }else{
                        progressDialog1.dismiss();
                        Toast.makeText(Vehicles.this, "Failed in Retrieving Data",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog1.dismiss();
                Toast.makeText(Vehicles.this,"Failed to Fetch Data try again",Toast.LENGTH_SHORT);
                Log.d("snapshot:", "snapshot does not exist");
            }
        });

//      add or delete or update item from firebase database
//        childEventListener=new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
////                Log.d("prevChild",previousChildName);
////                progressDialog= ProgressDialog.show(Vehicles.this, "","Please Wait, Loading...");
////                if(snapshot.exists()){
////                    int index= Integer.parseInt(snapshot.getKey());
////                    Map<String,Object> myVal=(HashMap<String,Object>) snapshot.getValue();
////                    key.add(String.valueOf(index));
////                    title.add(String.valueOf(myVal.get("vehicleName")));
////                    purchaseDate.add((Long) myVal.get("purchaseDate"));
////                    odometerUnit.add(String.valueOf(myVal.get("odometerReading")));
////                    manufacturer.add(String.valueOf(myVal.get("manufacturer")));
////                    model.add(String.valueOf(myVal.get("vehicleModel")));
////                    milage.add(String.valueOf(myVal.get("mileageRange")));
////                    fuelLimit.add(String.valueOf(myVal.get("fuelLimit")));
////                    plateNum.add(String.valueOf(myVal.get("plateNumber")));
////
////                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
////                        for(int i=0;i<title.size();i++){
////                            ModelForAdapter modelAdapter=new ModelForAdapter(title.get(i), purchaseDate.get(i));
////                            //bind all strings in an array
////                            listModel.add(modelAdapter);
////                        }
////                        progressDialog.dismiss();
////                        setListAdapter();
////                    }else{
////                        progressDialog.dismiss();
////                        Toast.makeText(Vehicles.this, "Failed in Retrieving Data",Toast.LENGTH_SHORT).show();
////                    }
////                }else{
////                    progressDialog.dismiss();
////                    Toast.makeText(Vehicles.this,"Failed to Fetch Data try again",Toast.LENGTH_SHORT);
////                    Log.d("snapshot:", "snapshot does not exist");
////                }
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                progressDialog= ProgressDialog.show(Vehicles.this, "","Please Wait, Loading...");
//                if(snapshot.exists()){
//                    int arrayIndex=key.indexOf(snapshot.getKey());
//                    if(arrayIndex != -1 ){
//                            VehicleDB vehicleDB=snapshot.getValue(VehicleDB.class);
//                            title.set(arrayIndex, vehicleDB.getVehicleName());
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTimeInMillis(vehicleDB.getPurchaseDate());
//                            String dateObj = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
//                            purchaseDate.set(arrayIndex, vehicleDB.getPurchaseDate());
//                            odometerUnit.set(arrayIndex,vehicleDB.getModometerReading());
//                            manufacturer.set(arrayIndex,vehicleDB.getManufacturer());
//                            model.set(arrayIndex,vehicleDB.getVehicleModel());
//                            milage.set(arrayIndex,String.valueOf(vehicleDB.getMileageRange()));
//                            fuelLimit.set(arrayIndex,String.valueOf(vehicleDB.getFuelLimit()));
//                            plateNum.set(arrayIndex,vehicleDB.getPlateNumber());
////                          notify the adapter
//                            adapter.notifyDataSetChanged();
//                    }
//                    progressDialog.dismiss();
//                }else{
//                    progressDialog.dismiss();
//                    Toast.makeText(Vehicles.this,"Failed to Update Refresh it.",Toast.LENGTH_SHORT);
//                }
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//                progressDialog= ProgressDialog.show(Vehicles.this, "","Please Wait, Loading...");
//                if(snapshot.exists()){
//                    int arrayIndex=key.indexOf(snapshot.getKey());
//                    if(arrayIndex != -1 ) {
//                        int index = Integer.parseInt(snapshot.getKey());
//                        Log.d("snapshotindex", String.valueOf(index));
//                        key.remove(arrayIndex);
//                        title.remove(arrayIndex);
//                        purchaseDate.remove(arrayIndex);
//                        odometerUnit.remove(arrayIndex);
//                        manufacturer.remove(arrayIndex);
//                        model.remove(arrayIndex);
//                        milage.remove(arrayIndex);
//                        fuelLimit.remove(arrayIndex);
//                        plateNum.remove(arrayIndex);
////                      notify the adapter
//                        adapter.notifyDataSetChanged();
//                    }
//                    progressDialog.dismiss();
//                }else{
//                    progressDialog.dismiss();
//                    Toast.makeText(Vehicles.this,"Failed to Delete!! Please Try again.",Toast.LENGTH_LONG);
//                }
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
////                progressDialog.dismiss();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(Vehicles.this,error.toString(),Toast.LENGTH_LONG);
////                progressDialog.dismiss();
//            }
//        };
//      set childEventListener
//        databaseReference.addChildEventListener(childEventListener);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                builder = new AlertDialog.Builder(Vehicles.this);
                builder.setTitle("Choose an option");
                String[] options={"View","Edit", "Select it","Delete"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:{
                                try {
                                    Calendar c = Calendar.getInstance();
                                    c.setTimeInMillis(purchaseDate.get(position));
                                    c.add(Calendar.MONTH,1);
                                    String dateString= c.get(Calendar.DAY_OF_MONTH)+"/"+c.get(Calendar.MONTH)+"/"+c.get(Calendar.YEAR);
                                    new AlertDialog.Builder(Vehicles.this)
                                            .setTitle(title.get(position))
//                                            display message
                                            .setMessage("Date: "+dateString+"\n"
                                                    +"----------------------------------\n\n"
                                                    +"Model: "+model.get(position)+"\n\n"
                                                    +"Mileage: "+milage.get(position)+"km \n\n"
                                                    +"FuelLimit: "+ fuelLimit.get(position)+" Ltr\n\n"
                                                    +"PlateNumber: "+ plateNum.get(position))
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
                                Intent i = new Intent(Vehicles.this, AddVehicle.class);
                                i.putExtra("type", "edit");
                                i.putExtra("key",key.get(position));
                                i.putExtra("title",title.get(position));
                                i.putExtra("meterReading",odometerUnit.get(position));
                                i.putExtra("manufacturer",manufacturer.get(position));
                                i.putExtra("purchaseDate",purchaseDate.get(position));
                                i.putExtra("model",model.get(position));
                                i.putExtra("milage",milage.get(position));
                                i.putExtra("fuelLimit",fuelLimit.get(position));
                                i.putExtra("plateNum",plateNum.get(position));
                                startActivity(i);
                            }break;
                            case 2:{
//                              set vehicle name
                                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                        .edit()
                                        .putString("vehicle", title.get(position))
                                        .putString("key", key.get(position))
                                        .commit();
//                              start dashboard activity
                                Intent i=new Intent(Vehicles.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }break;
                            case 3:{
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
                                        removeItem(index.get(position), position);
                                    }
                                });
                            }
                        }
                    }
                });
                dialog = builder.create();
                dialog.show();
            }
        });
    }

    public void setListAdapter(){
        adapter = new BaseAdapterForList(Vehicles.this, listModel);
        listView.setAdapter(adapter);
    }

    public void removeItem(String dbPosition, int listPosition){
        final String delKey = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getString("key",null);


        final String pos=dbPosition;
        final int listPos=listPosition;

        databaseReference.child(dbPosition)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        listModel.remove(listPos);
                        key.remove(listPos);
                        title.remove(listPos);
                        purchaseDate.remove(listPos);
                        odometerUnit.remove(listPos);
                        manufacturer.remove(listPos);
                        model.remove(listPos);
                        milage.remove(listPos);
                        fuelLimit.remove(listPos);
                        plateNum.remove(listPos);
                        adapter.notifyDataSetChanged();

                        if (delKey.equals(pos)){
                            databaseReference2.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {

                                    if(dataSnapshot.child("expenses").exists()){
                                        databaseReference2.child("expenses").child(pos).removeValue();
                                    }
                                    if(dataSnapshot.child("notes").exists()){
                                        databaseReference2.child("notes").child(pos).removeValue();
                                    }
                                    getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                            .edit()
                                            .putString("vehicle", "Nothing Selected")
                                            .putString("key", "")
                                            .commit();
                                }
                            });
                        }else{
                            Toast.makeText(Vehicles.this, "Failed to remove other Details",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}