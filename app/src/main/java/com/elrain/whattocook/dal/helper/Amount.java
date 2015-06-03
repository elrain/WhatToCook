package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class Amount extends DbHelper{

    private static final String TABLE = "amount";
    private static final String ID = "_id";
    private static final String COUNT = "count";
    private static final String ID_INGRIDIENTS = "idIngridients";
    private static final String ID_AMOUNT_TYPE = "idAmountType";
    private static final String ID_RECIPE = "idRecipe";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, " + COUNT + " INTEGER, "
            + ID_INGRIDIENTS + " INTEGER REFERENCES " + Ingridients.TABLE + " (" + Ingridients.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_AMOUNT_TYPE + " INTEGER REFERENCES " + AmountType.TABLE + " (" + AmountType.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_RECIPE + " INTEGER NOT NULL REFERENCES " + Recipe.TABLE + " (" + Recipe.ID + "));";

    public Amount(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(COUNT, 700);
        cv.put(ID_INGRIDIENTS, 1);
        cv.put(ID_AMOUNT_TYPE, 2);
        cv.put(ID_RECIPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(COUNT, 100);
        cv.put(ID_INGRIDIENTS, 2);
        cv.put(ID_AMOUNT_TYPE, 2);
        cv.put(ID_RECIPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(COUNT, 3);
        cv.put(ID_INGRIDIENTS, 3);
        cv.put(ID_AMOUNT_TYPE, 6);
        cv.put(ID_RECIPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(COUNT, 80);
        cv.put(ID_INGRIDIENTS, 4);
        cv.put(ID_AMOUNT_TYPE, 2);
        cv.put(ID_RECIPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(COUNT, 250);
        cv.put(ID_INGRIDIENTS, 5);
        cv.put(ID_AMOUNT_TYPE, 1);
        cv.put(ID_RECIPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(COUNT, 0);
        cv.put(ID_INGRIDIENTS, 6);
        cv.put(ID_AMOUNT_TYPE, 7);
        cv.put(ID_RECIPE, 1);
        db.insert(TABLE, null, cv);
    }
}
