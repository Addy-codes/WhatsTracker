package com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.legacy.content.WakefulBroadcastReceiver;
import com.godthablab.wtracker.R;



class NotificationIntentService extends IntentService {

    private static final int NOTIFICATION_ID = 1;
    private static final String ACTION_START = "ACTION_START";
    private static final String ACTION_DELETE = "ACTION_DELETE";

    public NotificationIntentService() {
        super(NotificationIntentService.class.getSimpleName());
    }

    public static Intent createIntentStartNotificationService(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_START);
        return intent;
    }

    public static Intent createIntentDeleteNotification(Context context) {
        Intent intent = new Intent(context, NotificationIntentService.class);
        intent.setAction(ACTION_DELETE);
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(getClass().getSimpleName(), "onHandleIntent, started handling a notification event");
        try {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                processStartNotification();
            }
            if (ACTION_DELETE.equals(action)) {
                processDeleteNotification(intent);
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent);
        }
    }

    private void processDeleteNotification(Intent intent) {
    }

    private void processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Scheduled Notification")
                .setAutoCancel(true)
                .setColor(getResources().getColor(R.color.colorAccent))
                .setContentText("This notification has been triggered by Notification Service")
                .setSmallIcon(R.drawable.notificationsound_icon);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                NOTIFICATION_ID,
                new Intent(this, NotificationIntentService.class),
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        //builder.setDeleteIntent(NotificationIntentService.getDeleteIntent(this));

        final NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(NOTIFICATION_ID, builder.build());
    }
}