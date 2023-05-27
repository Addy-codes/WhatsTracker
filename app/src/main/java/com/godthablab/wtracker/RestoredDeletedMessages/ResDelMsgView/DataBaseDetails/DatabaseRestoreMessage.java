package com.godthablab.wtracker.RestoredDeletedMessages.ResDelMsgView.DataBaseDetails;

import static com.godthablab.wtracker.RestoredDeletedMessages.NotificationsContract.NotifEntry.TABLE_NAME;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.godthablab.wtracker.RestoredDeletedMessages.NotificationsContract;


public class DatabaseRestoreMessage extends SQLiteOpenHelper {


    private SQLiteDatabase sqLiteDatabase;


    public DatabaseRestoreMessage(Context context) {
        super(context, "notif.db", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_NOTIF_TABLE = "CREATE TABLE " +
                TABLE_NAME + "(" + NotificationsContract.NotifEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_NAME + " TEXT , "
                + NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TITLE + " TEXT, "
                + NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TEXT + " TEXT , "
                + NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_TIME + " TEXT , "
                + NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_DATE + " TEXT , "
                + NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_DATA_PACKAGE_NAME + " TEXT , "
                + NotificationsContract.NotifEntry.COLUMN_NOTIF_APP_TIME_IN_MILLI + " INTEGER );";

        db.execSQL(SQL_CREATE_NOTIF_TABLE);

    }




    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notifications");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS chat_info");
        onCreate(sqLiteDatabase);
    }


    public void InsertStudentDetails(String username, String userstatus, String useronline, String usertyping, byte[] bmyimage) {
        this.sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uname", username);
        cv.put("ustatus", userstatus);
        cv.put("uonline", useronline);
        cv.put("utyping", usertyping);
        cv.put("uprofile", bmyimage);
        this.sqLiteDatabase.insert("notifications", null, cv);
        Log.d("Database", "Record.....]]]]] Created table....");
        this.sqLiteDatabase.close();
    }


    @SuppressLint({"LongLogTag"})


    /*Userlist*/
    public Cursor ViewUserList() {
        this.sqLiteDatabase = getWritableDatabase();
        Cursor cursor = this.sqLiteDatabase.rawQuery("select * from notifications group by title ORDER BY timeinmilli DESC", null);
        Log.d("Datasend", "Data send to user");
        return cursor;
    }


    public void whatsAppdelete() {
        this.sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.execSQL("delete from notifications where title='WhatsApp'");
        sqLiteDatabase.close();
    }





    public void saveUserChat(String user_id, String sender, String ismsg, String msg, String strDate, String msgstatus, String imagepath) {
        this.sqLiteDatabase = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("uid", user_id);
        cv.put("sender", sender);
        cv.put("ismsg", ismsg);
        cv.put(NotificationCompat.CATEGORY_MESSAGE, msg);
        cv.put("time", strDate);
        cv.put("msgstatus", msgstatus);
        cv.put("imagepath", imagepath);
        this.sqLiteDatabase.insert("chat_info", null, cv);
        Log.d("Database", "Record.....]]]]] Created table....");
        this.sqLiteDatabase.close();
    }


    public void UpdateMessageDetails(String position, String sender, String message, String status) {
        this.sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sender", sender);
        values.put(NotificationCompat.CATEGORY_MESSAGE, message);
        values.put("msgstatus", status);
        this.sqLiteDatabase.update("chat_info", values, "chatid=" + position, null);
    }


    public Cursor getUserChatHistory(String s) {
        Log.e("Got DATA","Reached getUserChatHistory method");
        this.sqLiteDatabase = getWritableDatabase();
        return this.sqLiteDatabase.rawQuery("select * from notifications where title ='" + s + "'group by text ORDER BY timeinmilli ASC",  null);
    }


    public Cursor getLastMessageHistory(String s) {
        Log.e("Got DATA","Reached getUserChatHistory method");
        this.sqLiteDatabase = getWritableDatabase();
        return this.sqLiteDatabase.rawQuery("select * from notifications where title ='" + s + "'group by text ORDER BY timeinmilli ASC",  null);
    }


    public Cursor getUserHistory(String s) {
        this.sqLiteDatabase = getWritableDatabase();
        return this.sqLiteDatabase.rawQuery("select * from notifications where title ='" + s + "'", null);
    }


    public void getUserDetailsUpdate(int user_id, String uname, String status, String isonline, String istyping, byte[] bmyimage) {
        this.sqLiteDatabase = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("uname", uname);
        values.put("ustatus", status);
        values.put("uonline", isonline);
        values.put("utyping", istyping);
        values.put("uprofile", bmyimage);
        this.sqLiteDatabase.update("notifications", values, "uid=" + user_id, null);
    }


    public void DeleteMessage(String chat_id) {
        this.sqLiteDatabase = getWritableDatabase();
        this.sqLiteDatabase.execSQL("DELETE FROM chat_info WHERE chatid = '" + chat_id + "'");
    }


    public void DeleteUserProfile(int user_id) {
        this.sqLiteDatabase = getWritableDatabase();
        this.sqLiteDatabase.execSQL("DELETE FROM notifications WHERE uid = '" + user_id + "'");
        this.sqLiteDatabase.execSQL("DELETE FROM chat_info WHERE uid = '" + user_id + "'");
    }


}

