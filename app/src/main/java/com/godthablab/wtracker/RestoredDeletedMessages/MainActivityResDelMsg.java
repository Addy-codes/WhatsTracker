package com.godthablab.wtracker.RestoredDeletedMessages;

/*Gets data and Represents IT into List view*/

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;
import com.godthablab.wtracker.R;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.DataBaseDetails.DatabaseRestoreMessage;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.DataBaseDetails.GetterSetterUserDetails;
import com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.GetUserDetailsAdapter;

import static com.godthablab.wtracker.RestoredDeletedMessages.NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TITLE;

import java.util.ArrayList;

/*
If you still can't find the error this way,
you can set breakpoints at the left of the lines of code where you want the application stops during debug,
and then run the app in the debug mode to see exactly what happens.
*/

public class MainActivityResDelMsg extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, SharedPreferences.OnSharedPreferenceChangeListener {

    private  NotifDbHelper notifDbHelper;
    private NotifCursorAdaptor notifCursorAdaptor;
    private DatabaseRestoreMessage databaseRestoreMessage;
    private static final int NOTIF_LOADER = 1;
    private ListView listView;
    private boolean deleteNotifAfter30days;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_resdelmsg);

       checkNotifPermission();

        doNotKillService();

        listView = (ListView) findViewById(R.id.list_view_notif);
        notifCursorAdaptor = new NotifCursorAdaptor(this, null);
        notifDbHelper = new  NotifDbHelper(this);

        View emptyView = findViewById(R.id.empty_subtitle_text);
        listView.setEmptyView(emptyView);

       ComponentName componentName = new ComponentName(getApplicationContext(), NotificationListener.class);

        listView.setAdapter(notifCursorAdaptor);
        registerForContextMenu(listView);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String click = "clicked :" + id;

                Toast.makeText(MainActivityResDelMsg.this,"Item Clicked Now 123456789",Toast.LENGTH_LONG).show();

//                Log.v("MainActivity","itemClicked" + id);
                TextView text = (TextView) findViewById(R.id.app_text);
                if (text.getMaxLines() == 3)
                    text.setMaxLines(50);
                else
                    text.setMaxLines(3);
            }
        });

        getLoaderManager().initLoader(NOTIF_LOADER, null, this);
    }
    /*Services for listening Notification messages asking packagemanager to not kill */
    private void doNotKillService() {

    try {
        PackageManager pm = getPackageManager();
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
        pm.setComponentEnabledSetting(new ComponentName(this, NotificationListener.class),
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
    }
    catch (Exception ex)
    {
        System.out.println(ex);
    }
    }

    /*checks if notification read permission is open or not*/
    private void checkNotifPermission() {
        boolean isNotificationServiceRunning = isNotificationServiceRunning();
        if (!isNotificationServiceRunning) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Enable Notfication Access").setTitle("Enable permissions");
            builder.setMessage("Enable Notfication Access or the notifications won't be Logged")


                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            startActivity(new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS));
                        }
                    })

                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            onBackPressed();

                        }
                    });





            AlertDialog alert = builder.create();
            alert.setTitle("Enable Notification Access");
            alert.show();
        }
    }

   /* Content Resolver for listening notification*/
    private boolean isNotificationServiceRunning() {
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();
        return enabledNotificationListeners != null && enabledNotificationListeners.contains(packageName);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                NotificationsContract.NotifEntry._ID,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_NAME,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TIME,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_DATE,
                COLUMN_NOTIF_APP_DATA_TITLE,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TEXT,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_PACKAGE_NAME,
                NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_TIME_IN_MILLI};
        return new CursorLoader( this, NotificationsContract.NotifEntry.CONTENT_URI, projection, null, null, NotificationsContract.NotifEntry._ID + " DESC");
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        notifCursorAdaptor.swapCursor(data);
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        notifCursorAdaptor.swapCursor(null);
    }

    private void alertForDeletingAllNotifications() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete all notification currently logged?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int rowsDeleted = getContentResolver().delete(NotificationsContract.NotifEntry.CONTENT_URI, null, null);
                        dialog.cancel();
                        if (rowsDeleted > 0)
                            Toast.makeText(getApplicationContext(), R.string.delted_all_successfully, Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //  Action for 'NO' Button

                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Delete All Notifications");
        alert.show();
    }

    private void deleteCurrentNotif(final long notifId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you really want to delete this notification ?").setTitle("Delete current Notification")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Uri currentNotifUri = ContentUris.withAppendedId(NotificationsContract.NotifEntry.CONTENT_URI, notifId);
                        int rowDeleted = getContentResolver().delete(currentNotifUri, null, null);
                        if (rowDeleted == 0) {
                            Toast.makeText(getApplicationContext(), "Notification Delete failed", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Notification Deleted successfully", Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.setTitle("Delete selected notification");
        alert.show();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.list_view_notif) {
            MenuInflater inflater = getMenuInflater();
            //inflater.inflate(R.menu.list_item_menu, menu);
        }
    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("deletenotifafter")) {
          //  getNotifPreferences();
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }
}


