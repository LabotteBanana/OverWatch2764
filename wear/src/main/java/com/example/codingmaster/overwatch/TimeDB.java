package com.example.codingmaster.overwatch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by YangYang on 2017-01-10.
 */

public class TimeDB extends SQLiteOpenHelper {

    private static final String TAG = "TimeDB";
    private static String DATABASE_NAME = "BlackBoxDB";
    private static int DATABASEVERSION = 1;

    public TimeDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASEVERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, " Create Table ");
        try {
            String DROP_SQL = "drop table if exists " + "TimeDB";
            db.execSQL(DROP_SQL);
        }
        catch (Exception e)
        {
            Log.d(TAG, "Exception in Drop_SQl");
        }

        String CREATE_SQL = "create table" + "TimeDB" + "("
                            + "_id integer PRIMARY KEY autoincrement, "
                            + " YMD integer, "
                            + " HMS integer, "
                            + " X text, "
                            + " Y text, "
                            + " Z text) ";

        try {
            db.execSQL(CREATE_SQL);
        } catch(Exception ex) {
            Log.d(TAG, "Exception in Create_SQl");
        }
        Log.d(TAG, "Inserting Record");


    }

    public void onOpen(SQLiteDatabase db)
    {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    /*
    private int insertRecordParam() {
        int count = 1;

        ContentValues recordValues = new ContentValues();

        recordValues.put("YMD", "20170110");
        recordValues.put("HMS", "142030");

        int rowPosition = (int) db.insert("TimeDB", null, recordValues);
    }
    */
}
