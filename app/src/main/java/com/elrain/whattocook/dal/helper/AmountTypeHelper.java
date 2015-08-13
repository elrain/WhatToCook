package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.CommonHelper;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.webutil.rest.response.AmountTypeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum table for amount types like weight, amount of items etc.
 * Created by Denys.Husher on 02.06.2015.
 */
public class AmountTypeHelper {
    public static final String TABLE = "amountType";
    public static final String ID = "_id";
    public static final String NAME = "name";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db, AmountTypeResponse amountType) {
        if (!CommonHelper.isItemExist(db, TABLE, amountType.getIdAmountType())) {
            ContentValues cv = new ContentValues();
            cv.put(NAME, amountType.getName());
            cv.put(ID, amountType.getIdAmountType());
            db.insert(TABLE, null, cv);
        }
    }

    public static void add(SQLiteDatabase db, List<NamedEntity> ingridients) {
        db.beginTransaction();
        try {
            for (NamedEntity no : ingridients) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, no.getId());
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

    public static List<NamedEntity> getAllTypes(SQLiteDatabase db) {
        Cursor cursor = null;
        List<NamedEntity> result = new ArrayList<>();
        try {
            cursor = db.query(TABLE, new String[]{ID, NAME}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                NamedEntity no = new NamedEntity();
                no.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                no.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                result.add(no);
            }
        } finally {
            if (null != cursor)
                cursor.close();
        }
        return result;
    }

    public static List<NamedEntity> getTypesForGroup(SQLiteDatabase db, long ingridientId) {
        Cursor cursor = null;
        List<NamedEntity> result = new ArrayList<>();
        try {
            cursor = db.rawQuery("SELECT at." + ID + ", at." + NAME + " " +
                    "FROM " + TABLE + " as at LEFT JOIN " + AvailAmountTypeHelper.TABLE + " as aat on at." + ID + " = aat." + AvailAmountTypeHelper.ID_AMOUNT_TYPE + " " +
                    "WHERE aat." + AvailAmountTypeHelper.ID_GROUP + " = " +
                    "(SELECT " + IngridientsHelper.ID_GROUP + " FROM " + IngridientsHelper.TABLE + " WHERE " + IngridientsHelper.ID + " = ?)", new String[]{String.valueOf(ingridientId)});
            while (cursor.moveToNext()) {
                NamedEntity no = new NamedEntity();
                no.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                no.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                result.add(no);
            }
        } finally {
            if (null != cursor) cursor.close();
        }

        return result;
    }

    public static String getTypeName(SQLiteDatabase db, long typeId) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE, new String[]{NAME}, ID + "= ?", new String[]{String.valueOf(typeId)}, null, null, null);
            return cursor.moveToNext() ? cursor.getString(cursor.getColumnIndex(NAME)) : null;
        } finally {
            if (null != cursor) cursor.close();
        }
    }
}
