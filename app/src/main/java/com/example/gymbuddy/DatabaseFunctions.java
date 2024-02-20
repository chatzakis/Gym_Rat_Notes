package com.example.gymbuddy;

import android.annotation.SuppressLint;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.*;
import android.database.*;
import android.util.Log;


public class DatabaseFunctions extends SQLiteOpenHelper {
    private final Context content;
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;

    private static final String ADD_RECORD_RESULT = "Add record result";
    private static final String RESULT_OK = "OK";
    private static final  String RESULT_ERROR = "ERROR";
    private static final String COLUMN_NAME = "Name";

    private static final String COLUMN_ID = "ID";


    public DatabaseFunctions(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.content = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean checkTableEmpty(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean isEmpty;
        Cursor cursor;
        String query = "SELECT * FROM " + tableName;
        cursor = db.rawQuery(query, null);
        isEmpty = cursor != null && cursor.getCount() == 0;
        assert cursor != null;
        cursor.close();
        return isEmpty;
    }

    public boolean checkNameExistsInTable(String dbRecordName, String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean exists;

        String query = "SELECT * FROM " + tableName +
                " WHERE "+ COLUMN_NAME + " = '"+ dbRecordName + "'";
        Cursor cursor = db.rawQuery(query, null);
        exists = (cursor != null && cursor.getCount() > 0);
        assert cursor != null;
        cursor.close();
        return exists;
    }

    public boolean checkIdExistsInTable(long dbRecordId, String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        boolean exists;
        String query = "SELECT * FROM " + tableName + " WHERE " + COLUMN_ID +
                " = "+ dbRecordId;
        cursor = db.rawQuery(query, null);
        exists = (cursor != null && cursor.getCount() > 0);
        assert cursor != null;
        cursor.close();
        return exists;
    }

    public void addRecordToDatabase(ContentValues values, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        long id = db.insert(tableName, null, values);
        if (id == -1) {
            Log.i(ADD_RECORD_RESULT, "Adding in Table " + tableName + " " + RESULT_OK);
        } else {
            Log.i(ADD_RECORD_RESULT, RESULT_ERROR + " in adding in Table " + tableName);
        }
        db.close();
    }

    public void updateRecordInDatabase(ContentValues values, String tableName, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(tableName, values, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    public void deleteRecord(String table, long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(table, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @SuppressLint("Range")
    public String getNameByID(String tableName, long id){
        String query = "SELECT * FROM " + tableName +
                " WHERE "+ COLUMN_ID + " = '"+ id + "'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        String exerciseName = "";
        while (cursor.moveToNext()) {
            if (cursor.getColumnIndex(COLUMN_NAME) >= 0)
                exerciseName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        }
        cursor.close();
        db.close();
        return exerciseName;
    }

    public Cursor getRecordsById(String tableName, long id){
        String query = "SELECT * FROM " + tableName +
                " WHERE "+ COLUMN_ID + " = "+ id +
                " ORDER BY " + COLUMN_ID + " DESC";;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public Cursor getAllRecordsFromTableDescending(String tableName){
        String query = "SELECT * FROM " + tableName +
                " ORDER BY " + COLUMN_ID + " DESC";
        System.out.println(query);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }


}
