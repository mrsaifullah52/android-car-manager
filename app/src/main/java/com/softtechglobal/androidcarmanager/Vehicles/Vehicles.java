package com.softtechglobal.androidcarmanager.Vehicles;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.softtechglobal.androidcarmanager.Database.VehicleDB;
import com.softtechglobal.androidcarmanager.MainActivity;
import com.softtechglobal.androidcarmanager.ModelForAdapter;
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.CustomBaseAdapter;
import com.softtechglobal.androidcarmanager.UserManagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Vehicles extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;
    CustomBaseAdapter adapter;

    ArrayList<String> key=new ArrayList<String>();
    ArrayList<String> title=new ArrayList<String>();
    ArrayList<String> odometerUnit=new ArrayList<String>();
    ArrayList<String> manufacturer=new ArrayList<String>();
    ArrayList<String> purchaseDate=new ArrayList<String>();
    ArrayList<String> model=new ArrayList<String>();
    ArrayList<String> milage=new ArrayList<String>();
    ArrayList<String> fuelLimit=new ArrayList<String>();
    ArrayList<String> plateNum=new ArrayList<String>();

    ArrayList<ModelForAdapter> listModel= new ArrayList<ModelForAdapter>();

    AlertDialog.Builder builder;
    AlertDialog dialog;

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;

    boolean status;
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


//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                int index = (int) snapshot.getChildrenCount();
//                if(snapshot.exists()) {
//                    for( int i=0; i<=index; i++) {
//                        VehicleDB vehicleDB = snapshot
//                                .child(String.valueOf(i))
//                                .getValue(VehicleDB.class);
////                      insert data into Arraylist<String>
//                        if (!snapshot.child(String.valueOf(i)).exists()) {
////                            if(!snapshot.child(String.valueOf(i)).equals("0")){
////                                index++;
////                                Toast.makeText(Vehicles.this,"index found",Toast.LENGTH_SHORT).show();
////                            }else{
//                                Toast.makeText(Vehicles.this,"no index found",Toast.LENGTH_SHORT).show();
////                            }
//                        }else{
//                            title.add(vehicleDB.getVehicleName());
//                            //getting date from long
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTimeInMillis(vehicleDB.getPurchaseDate());
//                            String dateObj = calendar.DAY_OF_MONTH + "/" + calendar.MONTH + "/" + calendar.YEAR;
//                            purchaseDate.add(dateObj);
//                            unitType.add(vehicleDB.getOdometerType());
//                            manufacturer.add(vehicleDB.getManufacturer());
//                            model.add(vehicleDB.getVehicleModel());
//                            milage.add(String.valueOf(vehicleDB.getMileageRange()));
//                            fuelLimit.add(String.valueOf(vehicleDB.getFuelLimit()));
//                            plateNum.add(vehicleDB.getPlateNumber());
//                        }
//                    }
////                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
//                        setListAdapter();
////                    }else{
////                        Toast.makeText(Vehicles.this, "Failed in Retreiving Data",Toast.LENGTH_SHORT).show();
////                    }
//                }else{
//                    Toast.makeText(Vehicles.this,"Please add a Vehicle",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

//          second method
//        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
//            @Override
//            public void onSuccess(DataSnapshot dataSnapshot) {
//                if(dataSnapshot.exists()) {
//                    for( DataSnapshot ds:dataSnapshot.getChildren()) {
//                        VehicleDB vehicleDB = ds.getValue(VehicleDB.class);
//                            key.add(ds.getKey());
//                            title.add(vehicleDB.getVehicleName());
//                            //getting date from long
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTimeInMillis(vehicleDB.getPurchaseDate());
//                            String dateObj = calendar.DAY_OF_MONTH + "/" + calendar.MONTH + "/" + calendar.YEAR;
//                            purchaseDate.add(dateObj);
//                            odometerUnit.add(vehicleDB.getModometerReading());
//                            manufacturer.add(vehicleDB.getManufacturer());
//                            model.add(vehicleDB.getVehicleModel());
//                            milage.add(String.valueOf(vehicleDB.getMileageRange()));
//                            fuelLimit.add(String.valueOf(vehicleDB.getFuelLimit()));
//                            plateNum.add(vehicleDB.getPlateNumber());
//                    }
//                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
//                        setListAdapter();
//                    }else{
//                        Toast.makeText(Vehicles.this, "Failed in Retrieving Data",Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(Vehicles.this,"Please add a Vehicle",Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//      delete or update item from database
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    int index= Integer.parseInt(snapshot.getKey());
                    Map<String,Object> myVal=(HashMap<String,Object>) snapshot.getValue();
                    key.add(String.valueOf(index));
                    title.add(String.valueOf(myVal.get("vehicleName")));
                    //getting date from long
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis((Long) myVal.get("purchaseDate"));
                    String dateObj = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
                    purchaseDate.add(dateObj);
                    odometerUnit.add(String.valueOf(myVal.get("modometerReading")));
                    manufacturer.add(String.valueOf(myVal.get("manufacturer")));
                    model.add(String.valueOf(myVal.get("vehicleModel")));
                    milage.add(String.valueOf(myVal.get("mileageRange")));
                    fuelLimit.add(String.valueOf(myVal.get("fuelLimit")));
                    plateNum.add(String.valueOf(myVal.get("plateNumber")));

                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
                        for(int i=0;i<title.size();i++){
                            ModelForAdapter modelAdapter=new ModelForAdapter(title.get(i), purchaseDate.get(i));
                            //bind all strings in an array
                            listModel.add(modelAdapter);
                        }
                        setListAdapter();
                    }else{
                        Toast.makeText(Vehicles.this, "Failed in Retrieving Data",Toast.LENGTH_SHORT).show();
                    }
                        Log.d("dateobj", purchaseDate.get(0));
                }else{
                    Log.d("snapshot:", "snapshot does not exist");
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    int arrayIndex=key.indexOf(snapshot.getKey());
                    if(arrayIndex != -1 ){
                            VehicleDB vehicleDB=snapshot.getValue(VehicleDB.class);
                            title.set(arrayIndex, vehicleDB.getVehicleName());
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(vehicleDB.getPurchaseDate());
                            String dateObj = calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR);
                            purchaseDate.set(arrayIndex,dateObj);
                            odometerUnit.set(arrayIndex,vehicleDB.getModometerReading());
                            manufacturer.set(arrayIndex,vehicleDB.getManufacturer());
                            model.set(arrayIndex,vehicleDB.getVehicleModel());
                            milage.set(arrayIndex,String.valueOf(vehicleDB.getMileageRange()));
                            fuelLimit.set(arrayIndex,String.valueOf(vehicleDB.getFuelLimit()));
                            plateNum.set(arrayIndex,vehicleDB.getPlateNumber());
//                          notify the adapter
                            adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    int arrayIndex=key.indexOf(snapshot.getKey());
                    if(arrayIndex != -1 ) {
                        int index = Integer.parseInt(snapshot.getKey());
                        Log.d("snapshotindex", String.valueOf(index));
                        key.remove(arrayIndex);
                        title.remove(arrayIndex);
                        purchaseDate.remove(arrayIndex);
                        odometerUnit.remove(arrayIndex);
                        manufacturer.remove(arrayIndex);
                        model.remove(arrayIndex);
                        milage.remove(arrayIndex);

                        fuelLimit.remove(arrayIndex);
                        plateNum.remove(arrayIndex);
//                      notify the adapter
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    Toast.makeText(Vehicles.this,"Failed to Delete!! Please Try again.",Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
//      set childEventListener
        databaseReference.addChildEventListener(childEventListener);

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
                                    new AlertDialog.Builder(Vehicles.this)
                                            .setTitle(title.get(position))
//                                            display message
                                            .setMessage("Date: "+purchaseDate.get(position)+"\n"
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
                                i.putExtra("vehicle",title.get(position));
//                              it should be unique from firebase
                                i.putExtra("vehicleId","124");
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
                                        removeItem(Integer.parseInt(index.get(position)));
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
        adapter = new CustomBaseAdapter(Vehicles.this, listModel);
        listView.setAdapter(adapter);
    }

    public boolean removeItem(int position){
        Log.d("position", String.valueOf(position));
        boolean status;
        if(position<0){
            status=false;
        }else{
            status=true;
            databaseReference.child(String.valueOf(position)).removeValue();
            getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .edit()
                    .putString("vehicle", "Nothing Selected")
                    .putString("key", "")
                    .commit();
        }
        return status;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(childEventListener);
    }
}