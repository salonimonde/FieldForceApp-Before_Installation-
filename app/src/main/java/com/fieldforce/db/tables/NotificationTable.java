package com.fieldforce.db.tables;import android.net.Uri;import com.fieldforce.db.ContentDescriptor;public class NotificationTable {    public static final String TABLE_NAME = "NotificationTable";    public static final String PATH = "NOTIFICATION_TABLE";    public static final int PATH_TOKEN = 1;    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();    public static class Cols {        public static final String ID = "_id";        public static final String USER_LOGIN_ID = "user_login_id";        public static final String MESSAGE = "message";        public static final String TITLE = "title";        public static final String DATE = "date";        public static final String IS_READ = "is_read";    }}