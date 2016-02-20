package com.andressantibanez.sarapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asantibanez on 2/20/16.
 */
public class DatabaseSQLiteOpenHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "sarapp.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        DatabaseSetup.execute(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
