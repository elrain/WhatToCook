package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;

/**
 * Created by Denys.Husher on 04.06.2015.
 */
public class GroupHelper extends DbHelper{

    public static final String TABLE = "groups";
    public static final String ID = "_id";
    public static final String NAME = "name";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(ID,1);
        cv.put(NAME, "Твердые");
        db.insert(TABLE, null, cv);

        cv.put(ID,2);
        cv.put(NAME, "Жидкие");
        db.insert(TABLE, null, cv);

        cv.put(ID,3);
        cv.put(NAME, "Специи");
        db.insert(TABLE, null, cv);
    }

    public GroupHelper(Context context) {
        super(context);
    }
}
