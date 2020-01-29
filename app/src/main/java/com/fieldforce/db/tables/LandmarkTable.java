package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

public class LandmarkTable {
    public static final String TABLE_NAME = "LandmarkTable";
    public static final String PATH = "LANDMARK_TABLE";
    public static final int PATH_TOKEN = 18;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of All Job Card Table
     *
     * @author Bynry01
     */

    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String LANDMARK_ID = "landmark_id";
        public static final String LANDMARK_NAME = "landmark_name";
        public static final String AREA_ID = "area_id";
    }

}
