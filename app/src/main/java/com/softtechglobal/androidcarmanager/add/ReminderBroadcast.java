package com.softtechglobal.androidcarmanager.add;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.softtechglobal.androidcarmanager.R;

import java.util.ArrayList;

public class ReminderBroadcast extends BroadcastReceiver {

    String key;
    ArrayList<String> keys = new ArrayList<String>();
    private DatabaseReference databaseReference1;
    private FirebaseAuth firebaseAuth;

    @Override
    public void onReceive(final Context context, Intent intent) {
        firebaseAuth = FirebaseAuth.getInstance();
        final FirebaseUser user=firebaseAuth.getCurrentUser();

        databaseReference1 = FirebaseDatabase.getInstance().getReference("users/"+user.getUid()+"/reminders/");

        SharedPreferences preferences = context.getSharedPreferences("PREFERENCE", context.MODE_PRIVATE);
        key = preferences.getString("key","-1");

        Bundle bundle = intent.getExtras();
        String Title = bundle.getString("Title");
        String Description = bundle.getString("Description");
        String index = bundle.getString("index");

        databaseReference1.child(index).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(context,"Reminder has been Completed!!",Toast.LENGTH_LONG).show();
            }
        });

        NotificationCompat.Builder builder=new NotificationCompat.Builder(context, "AndroidCarManager")
                .setSmallIcon(R.drawable.icon)
                .setContentTitle(Title)
                .setContentText(Description)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(0, builder.build());
    }
}
