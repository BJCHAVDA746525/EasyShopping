package com.example.easyshopping.Utils;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.easyshopping.Activities.ShoppingActivity;
import com.example.easyshopping.Data.Room_Helper;
import com.example.easyshopping.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseMessageReceiver extends FirebaseMessagingService {

    private static final String CHANNEL_ID = "default_channel";
    private static final int REQUEST_ID = 100;
    private static final int NOTIFICATION_ID = 0;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    Room_Helper helper;



    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);

        Log.d("Notification Token","Refreshed token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        if (message.getNotification() != null) {
            showNotification(
                    message.getNotification().getTitle(),
                    message.getNotification().getBody()
            );

            StoreIntoFirebase(
                    message.getNotification().getTitle(),
                    message.getNotification().getBody()
            );

        }

    }

    private void StoreIntoFirebase(String title, String body) {

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("/Notification");
        helper = Room_Helper.GetDB(getApplicationContext());

        String Uid = helper.room_dao().getalluserdetails().get(0).getUserID();

        if (Uid.isEmpty()){
            Log.d("Uid", "UserId Missing");
        }else {
            Map<String, String> UserNotification = new HashMap<>();
            UserNotification.put("Notification_Title", title);
            UserNotification.put("Notification_Body", body);

            databaseReference.child(Uid).push().setValue(UserNotification).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("Notification ", "Notification added successfully");
                }
            });
        }

    }


    public void showNotification(String title, String message)
    {

        Intent intent = new Intent(this, ShoppingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, REQUEST_ID, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

        String channelId = CHANNEL_ID;
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since Android Oreo (API 26), a notification channel is required.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    CHANNEL_ID,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }



}
