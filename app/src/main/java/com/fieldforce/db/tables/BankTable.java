package com.fieldforce.db.tables;

import android.net.Uri;

import com.fieldforce.db.ContentDescriptor;

public class BankTable {
    public static final String TABLE_NAME = "BankTable";
    public static final String PATH = "BANK_TABLE";
    public static final int PATH_TOKEN = 3;
    public static final Uri CONTENT_URI = ContentDescriptor.BASE_URI.buildUpon().appendPath(PATH).build();

    /**
     * This class contains Constants to describe name of Columns of All Job Card Table
     *
     * @author Bynry01
     */

    public static class Cols {
        public static final String ID = "_id";
        public static final String BANK_NAME = "bank_name";
    }
}
