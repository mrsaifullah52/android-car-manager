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
import com.softtechglobal.androidcarmanager.R;
import com.softtechglobal.androidcarmanager.add.CustomBaseAdapter;
import com.softtechglobal.androidcarmanager.usermanagement.Signin;

import java.util.ArrayList;
import java.util.Calendar;

public class Vehicles extends AppCompatActivity {

    ImageButton imageButton;
    ListView listView;
    CustomBaseAdapter adapter;

    ArrayList<String> title=new ArrayList<String>();
    ArrayList<String> unitType=new ArrayList<String>();
    ArrayList<String> manufacturer=new ArrayList<String>();
    ArrayList<String> purchaseDate=new ArrayList<String>();
    ArrayList<String> model=new ArrayList<String>();
    ArrayList<String> milage=new ArrayList<String>();
    ArrayList<String> fuelLimit=new ArrayList<String>();
    ArrayList<String> plateNum=new ArrayList<String>();

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
//        FirebaseDatabase.getInstance().setPersistenceEnabled(false);
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
                finish();
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
        databaseReference.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                int index = (int) dataSnapshot.getChildrenCount();
                if(dataSnapshot.exists()) {
                    for( int i=0; i<=index; i++) {
                        VehicleDB vehicleDB = dataSnapshot
                                .child(String.valueOf(i))
                                .getValue(VehicleDB.class);
//                      insert data into Arraylist<String>
                        if (dataSnapshot.child(String.valueOf(i)).exists()) {
                            title.add(vehicleDB.getVehicleName());
                            //getting date from long
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(vehicleDB.getPurchaseDate());
                            String dateObj = calendar.DAY_OF_MONTH + "/" + calendar.MONTH + "/" + calendar.YEAR;
                            purchaseDate.add(dateObj);
                            unitType.add(vehicleDB.getOdometerType());
                            manufacturer.add(vehicleDB.getManufacturer());
                            model.add(vehicleDB.getVehicleModel());
                            milage.add(String.valueOf(vehicleDB.getMileageRange()));
                            fuelLimit.add(String.valueOf(vehicleDB.getFuelLimit()));
                            plateNum.add(vehicleDB.getPlateNumber());
                        }
                    }
                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
                        setListAdapter();
                    }else{
                        Toast.makeText(Vehicles.this, "Failed in Retrieving Data",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Vehicles.this,"Please add a Vehicle",Toast.LENGTH_SHORT).show();
                }
            }
        });

//        databaseReference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DataSnapshot> task) {
//                if(task.isComplete()){
////                    int index = (int) task.getResult().getChildrenCount();
////                    for( int i=0; i<=index; i++) {
//                        GenericTypeIndicator<List<VehicleDB>> generic=new GenericTypeIndicator<List<VehicleDB>>() {};
//
//                        List<VehicleDB> taskData=task.getResult().getValue(generic);
//
//                        for(int j=0;j<taskData.size();j++){
////                            Log.d("taskData",taskData.get(j).getVehicleName());
//                            title.add(taskData.get(j).getVehicleName());
//                            //getting date from long
//                            Calendar calendar = Calendar.getInstance();
//                            calendar.setTimeInMillis(taskData.get(j).getPurchaseDate());
//                            String dateObj = calendar.DAY_OF_MONTH + "/" + calendar.MONTH + "/" + calendar.YEAR;
//                            purchaseDate.add(dateObj);
//                            unitType.add(taskData.get(j).getOdometerType());
//                            manufacturer.add(taskData.get(j).getManufacturer());
//                            model.add(taskData.get(j).getVehicleModel());
//                            milage.add(String.valueOf(taskData.get(j).getMileageRange()));
//                            fuelLimit.add(String.valueOf(taskData.get(j).getFuelLimit()));
//                            plateNum.add(taskData.get(j).getPlateNumber());
//                        }
//                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
//                        setListAdapter();
//                    }else{
//                        Toast.makeText(Vehicles.this, "Failed in Retreiving Data",Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(Vehicles.this,"Please add a Vehicle",Toast.LENGTH_SHORT).show();
//                }
//            }
//
//        });


//      delete or update item from database
        childEventListener=new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                int index = (int) snapshot.getChildrenCount();
////                int index= Integer.parseInt(snapshot.getKey());
//                Log.d("previousChildName",previousChildName);
//                if(snapshot.exists()) {
//                    for( int i=0; i<=index; i++) {
//                        Log.d("snapshot--> ",snapshot.getKey());
//                        Log.d("snapshot1--> ", String.valueOf(snapshot.child(String.valueOf(i)).getValue()));
//                        VehicleDB vehicleDB = snapshot
//                                .child(String.valueOf(index))
//                                .getValue(VehicleDB.class);
//                      insert data into Arraylist<String>
//                        if (snapshot.child(String.valueOf(index)).exists()) {
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
//                        }else{
//                            Toast.makeText(Vehicles.this, "Not Existed: "+snapshot.getKey(),Toast.LENGTH_SHORT).show();
//                        }
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
//                    if(!title.isEmpty() && !purchaseDate.isEmpty()){
//                        setListAdapter();
//                    }else{
//                        Toast.makeText(Vehicles.this, "Failed in Retreiving Data",Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(Vehicles.this,"Please add a Vehicle",Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                int index=Integer.parseInt(snapshot.getKey());

                Log.d("snapshotindex", String.valueOf(index));

                title.remove(index);
                purchaseDate.remove(index);
                unitType.remove(index);
                manufacturer.remove(index);
                model.remove(index);
                milage.remove(index);
                fuelLimit.remove(index);
                plateNum.remove(index);
//              notify the adapter
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
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
                                Toast.makeText(Vehicles.this,"Viewed",Toast.LENGTH_SHORT).show();
                                try {
                                    new AlertDialog.Builder(Vehicles.this)
                                            .setTitle(title.get(position))
//                                            display message
                                            .setMessage("Date: "+purchaseDate.get(position)+"\n"
                                                    +"----------------------------------\n\n"
                                                    +"UnitType: "+unitType.get(position)+"\n\n"
                                                    +"Model: "+model.get(position)+"\n\n"
                                                    +"Mileage: "+milage.get(position)+"\n\n"
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
                                i.putExtra("vehicleid", "124");
                                startActivity(i);
                            }break;
                            case 2:{
//                              set vehicle name
                                getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                                        .edit()
                                        .putString("vehicle", title.get(position))
                                        .putInt("vehicleId",124)
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
//                                boolean status =
                                        removeItem(position);
//                                if(status){
//                                    adapter.notifyDataSetChanged();
//                                }else{
//                                    Toast.makeText(Vehicles.this, "Failed to Delete, try again", Toast.LENGTH_SHORT).show();
//                                }
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
        adapter = new CustomBaseAdapter(Vehicles.this, title, purchaseDate);
        listView.setAdapter(adapter);
    }

    public boolean removeItem(int position){
        boolean status;
        if(position<0){
            status=false;
        }else{
            status=true;
            databaseReference.child(String.valueOf(position)).removeValue();
//            title.remove(position);
//            purchaseDate.remove(position);
        }
        return status;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseReference.removeEventListener(childEventListener);
    }
}