package com.fieldforce.db.tables;import android.net.Uri;import com.fieldforce.db.ContentDescriptor;public class ConversionJobCardTable {    public static final String TABLE_NAME = "ConversionJobCardTable";    public static final String PATH = "CONVERSION_JOB_CARD_TABLE";    public static final int PATH_TOKEN = 7;    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();    /**     * This class contains Constants to describe name of Columns of Consumer Table     *     * @author Bynry01     */    public static class Cols {        public static final String ID = "_id";        public static final String USER_LOGIN_ID = "user_login_id";        public static final String CONSUMER_NAME = "consumer_name";        public static final String MOBILE_NO = "mobile_no";        public static final String JOB_AREA = "job_area";        public static final String CARD_STATUS = "status";        public static final String ACCEPT_STATUS = "accept_status";        public static final String CONVERSION_ID = "conversion_id";        public static final String JOB_ID = "job_id";        public static final String ASSIGNED_DATE = "assigned_date";        public static final String DUE_DATE = "due_date";        public static final String REGULATOR_NO = "regulator_no";        public static final String METER_NO = "meter_no";        public static final String INSTALLED_ON = "installed_on";        public static final String RFC_VERIFIED_ON = "rfc_verified_on";        public static final String REJECTION_REMARK = "rejection_remark";        public static final String SCREEN = "screen";        public static final String COMPLETED_ON  = "completed_on";        public static final String AREA_NAME   = "area_name";    }}