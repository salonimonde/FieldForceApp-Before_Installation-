package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

public class ConsumerEnquiryTable {


    public static final String TABLE_NAME = "ConsumerEnquiryTable";
    public static final String PATH = "CONSUMER_ENQUIRY_TABLE";
    public static final int PATH_TOKEN = 30;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of Consumer Table
     *
     * @author Bynry01
     */

    public static class Cols {
        public static final String ID = "_id";
        public static final String USER_LOGIN_ID = "user_login_id";
        public static final String CONSUMER_NAME = "consumer_name";
        public static final String MOBILE_NO = "mobile_no";
        public static final String JOB_AREA = "job_area";
        public static final String CARD_STATUS = "status";
        public static final String ENQUIRY_ID = "enquiry_id";
        public static final String ENQUIRY_NO = "enquiry_no";
        public static final String ASSIGN_DATE = "assign_date";
        public static final String DUE_DATE = "due_date";

        public static final String SCREEN = "screen";
        public static final String STATE_ID = "state_id";
        public static final String CITY_ID = "city_id";
        public static final String STATE = "state";
        public static final String CITY = "city";
        public static final String NSC_ID  = "nsc_id";
        public static final String COMPLETED_ON  = "completed_on";
        public static final String REGISTRATION_ID  = "registration_id";
    }
}
