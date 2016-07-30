package com.example.megha.todoapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Megha on 13-03-2016.
 */
public class SQLHelper extends SQLiteOpenHelper {

    final static String DATABASE_NAME = "ToDoDb";
    final static String TABLE_NAME = "ToDoTable";
    final static String _ID = "_ID";
    final static String TITLE = "Title";
    final static String DATE = "Date";
    final static String CONTENT = "Content";
    final static String COLOUR = "Colour";

    public SQLHelper(Context context, int version){
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE "+TABLE_NAME+" ( "+_ID+" INTEGER , "+TITLE+" TEXT, "+ DATE+" TEXT, "+
                CONTENT+" TEXT, "+COLOUR+" INTEGER , "+" );";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
