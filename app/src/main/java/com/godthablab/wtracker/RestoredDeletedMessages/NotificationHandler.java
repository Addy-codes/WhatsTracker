package com.godthablab.wtracker.RestoredDeletedMessages;

/*NOTIFICATION OBJECT = PUT THE NOTIFICATION DATA VALUES IN SQLITE DATABASE*/

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.godthablab.wtracker.R;

import java.util.HashMap;


public class NotificationHandler extends AppCompatActivity  {

    public static final String LOCK = "lock";

    private LayoutInflater layoutInflater;
    private Context context;



   public NotificationHandler(Context context) {
        this.context = context.getApplicationContext();
        layoutInflater = (LayoutInflater) context.getSystemService(AppCompatActivity.LAYOUT_INFLATER_SERVICE);
    }





    void handlePosted(StatusBarNotification sbn) {
//        if(sbn.isOngoing()){
//            return;
//        }
        NotificationObject notificationObject = new NotificationObject(sbn, context);
        if (notificationObject.getText().length() == 0)
            return;


        String appName = notificationObject.getAppName();
        String title = notificationObject.getTitle().replaceAll("\\(.*?\\)", "");
        String text = notificationObject.getText();
        String time = utils.getTime(notificationObject.getSystemTime());
        String date = utils.getDate(notificationObject.getSystemTime());
        String packageName = notificationObject.getPackageName();
        long timeInMillis = notificationObject.getSystemTime();
        String[] selectionArgs = new String[]{appName, text, title, time, date, packageName};
        String selection = "name = ? AND text = ? AND title = ? AND time = ? AND date = ? AND packagename = ?";


        String[] projection = {
                NotificationsContract.NotifEntry._ID,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_NAME,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TIME,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_DATE,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TITLE,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TEXT,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_PACKAGE_NAME};

        Cursor cursor = context.getContentResolver().query(NotificationsContract.NotifEntry.CONTENT_URI, projection, selection, selectionArgs, null);


        if (cursor != null && cursor.getCount() > 0)
            return;
        synchronized (LOCK) {
            ContentValues values = new ContentValues();
            values.put(NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_NAME, appName);
            values.put(NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TITLE, title);
            values.put(NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TEXT, text);
            values.put(NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TIME, time);
            values.put(NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_DATE, date);
            values.put(NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_PACKAGE_NAME, packageName);
            values.put(NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_TIME_IN_MILLI, timeInMillis);
            context.getContentResolver().insert(NotificationsContract.NotifEntry.CONTENT_URI, values);
        }





        HashMap<String, String> object = new HashMap<>();
        object.put("Message", text);
            if (text.equalsIgnoreCase("this message was deleted")) {

                addNotification();
            Log.i("Deleted", "Deleted Message String Detected");


        } else {
            Log.i("Deleted", "No Deleted Message String Detected");
            addNotification();
        }
    }


    public void addNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notificationsound_icon) //set icon for notification
                        .setContentTitle("Message Deleted") //set title of notification
                        .setContentText("Tap to view Deleted Message in WhatsTracker")//this is notification message
                        .setAutoCancel(true) // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification

        Intent notificationIntent = new Intent(this, NotificationHandler.class);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //notification message will get at NotificationView
        notificationIntent.putExtra("message", "This is a notification message");

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);

        // Add as notification
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

    public void broadcastNotification(Context context) {

        Log.d("BroadCast", "Reached");
        Intent intent = new Intent("NOTIFICATION");
        intent.putExtra("MESSAGE", "your_message");
        context.sendBroadcast(intent);
    }





}

