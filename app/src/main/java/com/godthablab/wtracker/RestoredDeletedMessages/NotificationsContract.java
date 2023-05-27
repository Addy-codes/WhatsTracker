package com.godthablab.wtracker.RestoredDeletedMessages;

/* CONTENT PROVIDER For Notification Data  STRING */

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public final class NotificationsContract {

    public static final String CONTENT_AUTHORITY = "com.godthablab.wtracker";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_NOTIFS = "notifications";

    private NotificationsContract(){}

    public static final class NotifEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_NOTIFS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFS;



        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_NOTIFS;

        public final static String TABLE_NAME = "notifications";

        public final static String TABLE_NAME_APP_PREF = "apppreference";

        public final static String _ID = BaseColumns._ID;

        public final static String COLUMN_NOTIF_APP_NAME ="name";

        public final static String COLUMN_NOTIF_APP_DATA_TEXT = "text";

        public final static String COLUMN_NOTIF_APP_DATA_TITLE = "title";

        public final static String COLUMN_NOTIF_APP_DATA_TIME = "time";

        public final static String COLUMN_NOTIF_APP_DATA_DATE = "date";

        public final static String COLUMN_NOTIF_APP_DATA_PACKAGE_NAME = "packagename";

        public final static String COLUMN_NOTIF_APP_TIME_IN_MILLI = "timeinmilli";
    }

}
