package com.example.ceyda.friendlypaws;

import android.net.Uri;
import android.provider.BaseColumns;

public class Contract {
    public static final String AUTHORITY = "com.example.rumeysa.friendlypaws.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    // The URI for the "lostanimals" table
    //public static final Uri LOST_ANIMALS_URI = BASE_CONTENT_URI.buildUpon().appendPath("lostanimals").build();

    public static class MyEntry implements BaseColumns {
        public static final String TABLE_NAME = "lostanimals";
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String COLUMN_ID = "_id";

        public static final String COLUMN_PICTURE = "picture";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_INFO = "information";

        public static final String COLUMN_PHONE = "phone";

        public static final String COLUMN_USER_ID = "userid";
    }
}

