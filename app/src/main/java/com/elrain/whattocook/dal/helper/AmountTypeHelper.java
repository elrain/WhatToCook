package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.NamedObject;

import java.util.List;

/**
 * Enum table for amount types like weight, amount of items etc.
 * Created by Denys.Husher on 02.06.2015.
 */
public class AmountTypeHelper extends DbHelper{

    public static final String TABLE = "amountType";
    public static final String ID = "_id";
    public static final String NAME = "name";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(ID, 1);
        cv.put(NAME, "мл");
        db.insert(TABLE, null, cv);

        cv.put(ID, 2);
        cv.put(NAME, "г");
        db.insert(TABLE, null, cv);

        cv.put(ID, 3);
        cv.put(NAME, "ч.ложка(и)");
        db.insert(TABLE, null, cv);

        cv.put(ID, 4);
        cv.put(NAME, "ст.ложка(и)");
        db.insert(TABLE, null, cv);

        cv.put(ID, 5);
        cv.put(NAME, "шт");
        db.insert(TABLE, null, cv);

        cv.put(ID, 6);
        cv.put(NAME, "стебля");
        db.insert(TABLE, null, cv);

        cv.put(ID, 7);
        cv.put(NAME, "по вкусу");
        db.insert(TABLE, null, cv);
    }

    public AmountTypeHelper(Context context) {
        super(context);
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
