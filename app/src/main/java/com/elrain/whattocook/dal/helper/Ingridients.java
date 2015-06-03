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
public class Ingridients extends DbHelper {
    public static final String TABLE = "ingridients";
    public static final String ID = "_id";
    private static final String NAME = "name";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (50) NOT NULL);";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(ID, 1);
        cv.put(NAME, "Вырезка телячья");
        db.insert(TABLE, null, cv);

        cv.put(ID, 2);
        cv.put(NAME, "Прошутто");
        db.insert(TABLE, null, cv);

        cv.put(ID, 3);
        cv.put(NAME, "Шалфей");
        db.insert(TABLE, null, cv);

        cv.put(ID, 4);
        cv.put(NAME, "Масло сливочное");
        db.insert(TABLE, null, cv);

        cv.put(ID, 5);
        cv.put(NAME, "Белое сухое вино");
        db.insert(TABLE, null, cv);

        cv.put(ID, 6);
        cv.put(NAME, "Перец черный молотый");
        db.insert(TABLE, null, cv);
    }

    public Ingridients(Context context) {
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
