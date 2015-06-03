package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class AmountHelper extends DbHelper{

    public static final String TABLE = "amount";
    public static final String ID = "_id";
    public static final String COUNT = "count";
    public static final String ID_INGRIDIENTS = "idIngridients";
    public static final String ID_AMOUNT_TYPE = "idAmountType";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, " + COUNT + " INTEGER, "
            + ID_INGRIDIENTS + " INTEGER REFERENCES " + IngridientsHelper.TABLE + " (" + IngridientsHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_AMOUNT_TYPE + " INTEGER REFERENCES " + AmountTypeHelper.TABLE + " (" + AmountTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public AmountHelper(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(ID, 1);
        cv.put(COUNT, 700);
        cv.put(ID_INGRIDIENTS, 1);
        cv.put(ID_AMOUNT_TYPE, 2);
        db.insert(TABLE, null, cv);

        cv.put(ID, 2);
        cv.put(COUNT, 100);
        cv.put(ID_INGRIDIENTS, 2);
        cv.put(ID_AMOUNT_TYPE, 2);
        db.insert(TABLE, null, cv);

        cv.put(ID, 3);
        cv.put(COUNT, 3);
        cv.put(ID_INGRIDIENTS, 3);
        cv.put(ID_AMOUNT_TYPE, 6);
        db.insert(TABLE, null, cv);

        cv.put(ID, 4);
        cv.put(COUNT, 80);
        cv.put(ID_INGRIDIENTS, 4);
        cv.put(ID_AMOUNT_TYPE, 2);
        db.insert(TABLE, null, cv);

        cv.put(ID, 5);
        cv.put(COUNT, 250);
        cv.put(ID_INGRIDIENTS, 5);
        cv.put(ID_AMOUNT_TYPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(ID, 6);
        cv.put(COUNT, 0);
        cv.put(ID_INGRIDIENTS, 6);
        cv.put(ID_AMOUNT_TYPE, 7);
        db.insert(TABLE, null, cv);
    }
}
