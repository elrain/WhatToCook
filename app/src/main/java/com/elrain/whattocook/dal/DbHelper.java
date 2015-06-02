package com.elrain.whattocook.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "whattocook.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
