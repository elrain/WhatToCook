package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.ManyToManyEntity;
import com.elrain.whattocook.dao.NamedEntity;

import java.util.List;

/**
 * Created by Denys.Husher on 04.06.2015.
 */
public class AvialAmountTypeHelper {
    public static final String TABLE = "AvailAmountType";
    public static final String ID = "_id";
    public static final String ID_GROUP = "idGroup";
    public static final String ID_AMOUNT_TYPE = "idAmountType";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + ID_GROUP + " INTEGER REFERENCES " + GroupHelper.TABLE + " (" + GroupHelper.ID + ") ON DELETE CASCADE NOT NULL, "
            + ID_AMOUNT_TYPE + " INTEGER REFERENCES " + AmountTypeHelper.TABLE + " (" + AmountTypeHelper.ID + ") ON DELETE CASCADE NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db,List<ManyToManyEntity> rules) {
        db.beginTransaction();
        try {
            for (ManyToManyEntity no : rules) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, no.getId());
                contentValues.put(ID_GROUP, no.getIdFirst());
                contentValues.put(ID_AMOUNT_TYPE, no.getIdSecond());
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
