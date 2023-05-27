package com.godthablab.wtracker.RestoredDeletedMessages;

/*NOTIFICATION LISTENER SERVICE WHICH LISTENS THE NOTIFICATION ONLY FOR WHATSAPP*/

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;



public class  NotificationListener extends NotificationListenerService {

    private final String TAG =    NotificationListener.class.getSimpleName();
    private static final String WA_PACKAGE = "com.whatsapp";
    String user_id;
  //  private static final String SK_PACKAGE = "com.skype.raider";

    /*sbn = status bar notification*/



    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {

        if (!sbn.getPackageName().equals(WA_PACKAGE)) return; // getting only whatsapp notifications
      //  if (!sbn.getPackageName().equals(SK_PACKAGE)) return; //getting only Skype messages

        Log.i(TAG, "ID:" + sbn.getId());
        Log.i(TAG, "Posted by:" + sbn.getPackageName());





        try {
            NotificationHandler notificationHandler = new NotificationHandler(this);
            notificationHandler.handlePosted(sbn);
            notificationHandler.addNotification();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
//        Log.v(TAG, "ID:" + sbn.getId());
//        Log.v(TAG, "Removed ," + "Posted by:" + sbn.getPackageName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);

    }

    @Override
    public void onListenerConnected() {
        Log.i(TAG, "Connected");
    }

    @Override
    public void onListenerDisconnected (){
        Log.v(TAG,"Disconnected");
    }





}
