package com.example.test.cashcontrol.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;


public class DB {
    final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("mm/dd/yyyy");
    private static final String DB_NAME = "spending_db";
    private static final int DB_VERSION = 1;
    private static final String DB_TABLE = "Spending";
    private final Context _context;
    private DBHelper _DBHelper;
    private SQLiteDatabase _DB;

    public DB(Context context){
        _context = context;
    }

    public void open(){
        _DBHelper = new DBHelper(_context, DB_NAME, null, DB_VERSION);
        _DB = _DBHelper.getWritableDatabase();
    }

    public void close(){
        if (_DBHelper != null)
            _DBHelper.close();
    }

    public Cursor getAllData(){
        return _DB.query(DB_TABLE, null, null, null, null, null, null);
    }

    public void addRecord(String name, int price, String date, String category){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("date", date);
        cv.put("category", category);
        _DB.insert(DB_TABLE, null, cv);
    }

    public void update (int id, String name, int price, String date, String category){
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("price", price);
        cv.put("date", date);
        cv.put("category", category);
        _DB.update(DB_TABLE, cv, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteRecord(int id){
        _DB.delete(DB_TABLE, "id = " + id, null);
    }

    public void deleteAllRecords(){
        _DB.delete(DB_TABLE, null, null);
    }
}
