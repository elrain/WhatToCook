package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.AmountEntity;

import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class AmountHelper {
    public static final String TABLE = "amount";
    public static final String ID = "_id";
    public static final String COUNT = "count";
    public static final String ID_INGRIDIENTS = "idIngridients";
    public static final String ID_AMOUNT_TYPE = "idAmountType";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, " + COUNT + " INTEGER, "
            + ID_INGRIDIENTS + " INTEGER REFERENCES " + IngridientsHelper.TABLE + " (" + IngridientsHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_AMOUNT_TYPE + " INTEGER REFERENCES " + AmountTypeHelper.TABLE + " (" + AmountTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db, List<AmountEntity> amounts) {
        db.beginTransaction();
        try {
            for (AmountEntity no : amounts) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, no.getId());
                contentValues.put(COUNT, no.getCount());
                contentValues.put(ID_INGRIDIENTS, no.getIdIngridient());
                contentValues.put(ID_AMOUNT_TYPE, no.getIdAmountType());
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
