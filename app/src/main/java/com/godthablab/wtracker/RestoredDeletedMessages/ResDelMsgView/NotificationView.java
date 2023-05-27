package com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.godthablab.wtracker.R;
import com.godthablab.wtracker.RestoredDeletedMessages.NotificationHandler;

import java.util.HashMap;

public class NotificationView extends AppCompatActivity {
    TextView textView;

    boolean notificationalert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        if (notificationalert == true){

            addNotification();
        }
        else

            {
                /*empty*/

            }


    }


    public void addNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.notificationsound_icon) //set icon for notification
                        .setContentTitle("Message Deleted") //set title of notification
                        .setContentText("Tap to view Deleted Message in WhatsTracker")//this is notification message
                        .setAutoCancel(true)
                        // makes auto cancel of notification
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT); //set priority of notification
        Log.i("Alert","addNotification method called from UserChatResDel");

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


  /*  public void  checkDeletedMessages(){

        HashMap<String, String> object = new HashMap<>();
        object.put("Message", text);
        if (text.equalsIgnoreCase("this message was deleted"))
        {


            Log.i("Alert","addNotification method called from NotificationHandler");
            Log.i("Deleted","Deleted Message String Detected");


            startActivity(new Intent(NotificationHandler.this, NotificationView.class));
        }
        else
        {
            Log.i("Deleted","No Deleted Message String Detected");
        }

    }
    */


}