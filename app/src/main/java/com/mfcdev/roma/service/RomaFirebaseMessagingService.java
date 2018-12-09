package com.mfcdev.roma.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.mfcdev.roma.LoginActivity;
import com.mfcdev.roma.R;

/**
 * Created by tim on 12/9/18.
 */

public class RomaFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "RomaMessagingService";

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // // If you want to send messages to this application instance or
        // // manage this apps subscriptions on the server side, send the
        // // Instance ID token to your app server.
        // sendRegistrationToServer(token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // notif mgr
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // check api version for notif. channels
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("default","ROMA", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Roma social messaging");
            mNotificationManager.createNotificationChannel(channel);
        }

        // notif. builder
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "default")
                .setSmallIcon(R.mipmap.ic_launcher) // notification icon
                .setContentTitle(remoteMessage.getNotification().getTitle()) // title for notification
                .setContentText(remoteMessage.getNotification().getBody())// message for notification
//                .setSound(alarmSound) // set alarm sound for notification
                .setAutoCancel(true); // clear notification after click
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(pi);
        mNotificationManager.notify(0, mBuilder.build());
    }
}
