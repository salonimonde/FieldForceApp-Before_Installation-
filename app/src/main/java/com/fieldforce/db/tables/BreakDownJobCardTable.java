package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;


public class BreakDownJobCardTable
{
    public static final String TABLE_NAME = "BreakDownJobCardTable";
    public static final String PATH = "BREAKDOWN_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 35;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();
    /**
     * This class contains Constants to describe name of Columns of BreakDownJobCardTable Table
     *
     * @author Bynry01
     */
    public static class Cols
    {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String BREAKDOWN_ID = "breakdown_id";
        public static final String JOB_ID = "breakdown_job_id";
        public static final String JOB_NAME = "breakdown_job_name";
        public static final String JOB_AREA = "breakdown_job_area";
        public static final String JOB_LOCATION = "breakdown_job_location";
        public static final String JOB_CATEGORY = "breakdown_job_category";
        public static final String JOB_SUBCATEGORY = "breakdown_job_subcategory";
        public static final String CARD_STATUS = "breakdown_card_status";
        public static final String MAKE_NUMBER = "make_no";
        public static final String DATE = "breakdown_card_date";
        public static final String ACCEPT_STATUS = "accept_status";
    }
}
