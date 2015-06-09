package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.NamedEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Enum table for amount types like weight, amount of items etc.
 * Created by Denys.Husher on 02.06.2015.
 */
public class AmountTypeHelper extends DbHelper {
    public static final String TABLE = "amountType";
    public static final String ID = "_id";
    public static final String NAME = "name";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";

    public AmountTypeHelper(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void add(NamedEntity ingridient) {
        ContentValues cv = new ContentValues();
        cv.put(NAME, ingridient.getName());
        this.getWritableDatabase().insert(TABLE, null, cv);
    }

    public void add(List<NamedEntity> ingridients) {
        SQLiteDatabase db = this.getWritableDatabase();
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

    public List<NamedEntity> getAllTypes() {
        Cursor cursor = null;
        List<NamedEntity> result = new ArrayList<>();
        try {
            cursor = this.getReadableDatabase().query(TABLE, new String[]{ID, NAME}, null, null, null, null, null);
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

    public List<NamedEntity> getTypesForGroup(long ingridientId) {
        Cursor cursor = null;
        List<NamedEntity> result = new ArrayList<>();
        try {
            cursor = this.getReadableDatabase().rawQuery("SELECT at." + ID + ", at." + NAME + " " +
                    "FROM " + TABLE + " as at LEFT JOIN " + AvialAmountTypeHelper.TABLE + " as aat on at." + ID + " = aat." + AvialAmountTypeHelper.ID_AMOUNT_TYPE + " " +
                    "WHERE aat." + AvialAmountTypeHelper.ID_GROUP + " = " +
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

    public String getTypeName(long typeId) {
        Cursor cursor = null;
        try {
            cursor = this.getReadableDatabase().query(TABLE, new String[]{NAME}, ID + "= ?", new String[]{String.valueOf(typeId)}, null, null, null);
            return cursor.moveToNext() ? cursor.getString(cursor.getColumnIndex(NAME)) : null;
        } finally {
            if (null != cursor) cursor.close();
        }
    }
}
