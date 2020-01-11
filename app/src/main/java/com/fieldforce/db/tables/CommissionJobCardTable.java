package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;


/**
 * This class describes all necessary info
 * about the CommissionJobCardTable of device database
 *
 * @author Bynry01
 */
public class CommissionJobCardTable {
    public static final String TABLE_NAME = "CommissionJobCardTable";
    public static final String PATH = "COMMISSION_JOB_CARD_TABLE";
    public static final int PATH_TOKEN = 20;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */
    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String COMMISSION_ID = "commission_id";
        public static final String JOB_ID = "job_id";
        public static final String JOB_NAME = "job_name";
        public static final String JOB_AREA = "job_area";
        public static final String JOB_LOCATION = "job_location";
        public static final String JOB_CATEGORY = "job_category";
        public static final String JOB_SUBCATEGORY = "job_subcategory";
        public static final String CARD_STATUS = "card_status";
        public static final String MAKE_NUMBER = "make_no";
        public static final String ASSIGNED_DATE = "assigned_date";
        public static final String ACCEPT_STATUS = "accept_status";
        public static final String JOB_TYPE = "job_type";
        public static final String MATERIAL_RECEIVED = "material_received";
    }
}