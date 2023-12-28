package com.example.ceyda.friendlypaws;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "MobileProject.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_TABLE = "CREATE TABLE " +
                Contract.MyEntry.TABLE_NAME + " (" +
                Contract.MyEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.MyEntry.COLUMN_PICTURE + " BLOB, " +
                Contract.MyEntry.COLUMN_ADDRESS + " TEXT, " +
                Contract.MyEntry.COLUMN_PHONE + " TEXT, " +
                Contract.MyEntry.COLUMN_USER_ID + " TEXT, " +
                Contract.MyEntry.COLUMN_INFO + " TEXT);";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contract.MyEntry.TABLE_NAME);
        onCreate(db);
    }
}
