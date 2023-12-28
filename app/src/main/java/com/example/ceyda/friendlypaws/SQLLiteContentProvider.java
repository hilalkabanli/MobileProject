package com.example.ceyda.friendlypaws;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class SQLLiteContentProvider extends ContentProvider {
    private static final int LOSTANIMALS_TABLE = 100;
    public static final int LOSTANIMALS_TABLE_ITEM = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.MyEntry.TABLE_NAME, LOSTANIMALS_TABLE);
        uriMatcher.addURI(Contract.AUTHORITY, Contract.MyEntry.TABLE_NAME + "/#", LOSTANIMALS_TABLE_ITEM);
        return uriMatcher;
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor;

        switch (sUriMatcher.match(uri)) {
            case LOSTANIMALS_TABLE:
                cursor = db.query(
                        Contract.MyEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case LOSTANIMALS_TABLE_ITEM:
                // Extract the ID from the URI
                String itemId = uri.getLastPathSegment();

                // Add the ID to the selection
                String[] selectionArgsWithId = new String[]{itemId};
                String selectionWithId = Contract.MyEntry.COLUMN_ID + "=?";

                // Query for a specific item by ID
                cursor = db.query(
                        Contract.MyEntry.TABLE_NAME,
                        projection,
                        selectionWithId,
                        selectionArgsWithId,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int uriType = sUriMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHelper.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case LOSTANIMALS_TABLE:
                id = sqlDB.insert(Contract.MyEntry.TABLE_NAME, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        if (id > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, id);
            getContext().getContentResolver().notifyChange(uri, null);
            return newUri;
        } else {
            throw new SQLException("Failed to insert row into " + uri);
        }
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsDeleted;
        //Uri deleteUri;

        switch (sUriMatcher.match(uri)) {
            case LOSTANIMALS_TABLE:
                rowsDeleted = db.delete(Contract.MyEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case LOSTANIMALS_TABLE_ITEM:
                // Extract the ID from the URI
                String idToDelete = uri.getLastPathSegment();

                // Delete data with the specified ID
                rowsDeleted = db.delete(Contract.MyEntry.TABLE_NAME, Contract.MyEntry._ID + "=?", new String[]{idToDelete});
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);  //deleteUri
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int rowsUpdated;

        switch (sUriMatcher.match(uri)) {
            case LOSTANIMALS_TABLE:
                rowsUpdated = db.update(Contract.MyEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case LOSTANIMALS_TABLE:
                return "vnd.android.cursor.dir/" + Contract.MyEntry.TABLE_NAME;
            case LOSTANIMALS_TABLE_ITEM:
                return "vnd.android.cursor.item/" + Contract.MyEntry.TABLE_NAME;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }
}
