package com.jimmywilcox.knittingcounter;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

    //Constants for db name and version
    private static final String DATABASE_NAME = "counter.db";
    private static final int DATABASE_VERSION = 1;

    //Constants for identifying table and columns
    public static final String TABLE_COUNTER = "counter";
    public static final String COUNTER_ID = "_id";
    public static final String COUNTER_NAME = "counterName";
    public static final String COUNTER_ROW = "counterRow";
    public static final String COUNTER_CREATED = "counterCreated";

    public static final String[] ALL_COLUMNS =
            {COUNTER_ID, COUNTER_NAME, COUNTER_ROW, COUNTER_CREATED};

    //SQL to create table
    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_COUNTER + " (" +
                    COUNTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COUNTER_NAME + " TEXT, " +
                    COUNTER_ROW + " INTEGER, " +
                    COUNTER_CREATED + " TEXT default CURRENT_TIMESTAMP" +
                    ")";



    public DBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COUNTER);
        onCreate(db);
    }
}
