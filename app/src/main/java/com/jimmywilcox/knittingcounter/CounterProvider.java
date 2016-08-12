package com.jimmywilcox.knittingcounter;


import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

public class CounterProvider extends ContentProvider{

    private static final String AUTHORITY = "com.jimmywilcox.knittingcounter.counterprovider";
    private static final String BASE_PATH = "counter";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // Constant to identify the requested operation
    private static final int COUNTER = 1;
    private static final int COUNTER_ID = 2;

    private static final UriMatcher uriMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);

    public static final String CONTENT_ITEM_TYPE = "Row";

    static {
        uriMatcher.addURI(AUTHORITY, BASE_PATH, COUNTER);
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", COUNTER_ID);
    }

    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        DBOpenHelper helper = new DBOpenHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        if (uriMatcher.match(uri) == COUNTER_ID) {
            selection = DBOpenHelper.COUNTER_ID + "=" + uri.getLastPathSegment();
        }


        return database.query(DBOpenHelper.TABLE_COUNTER, DBOpenHelper.ALL_COLUMNS,
                selection, null, null, null,
                DBOpenHelper.COUNTER_CREATED + " DESC");
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id = database.insert(DBOpenHelper.TABLE_COUNTER,
                null, values);
        return uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
       return database.delete(DBOpenHelper.TABLE_COUNTER, selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return database.update(DBOpenHelper.TABLE_COUNTER,
                values, selection, selectionArgs);
    }
}
