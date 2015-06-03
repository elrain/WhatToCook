package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.NamedObject;

import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class DishType extends DbHelper {

    public static final String TABLE = "dishType";
    public static final String ID = "_id";
    private static final String NAME = "name";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";

    public DishType(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, "Основные блюда");
        db.insert(TABLE, null, cv);
    }

    public void add(NamedObject ingridient) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, ingridient.getName());
        this.getWritableDatabase().insert(TABLE, null, cv);
    }

    public void add(List<NamedObject> ingridients) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (NamedObject no : ingridients) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(NAME, no.getName());
                db.insert(TABLE, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
