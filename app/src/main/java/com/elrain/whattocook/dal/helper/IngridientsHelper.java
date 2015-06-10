package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.IngridientsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class IngridientsHelper extends DbHelper {
    public static final String TABLE = "ingridients";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String ID_GROUP = "idGroup";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (50) NOT NULL, " + ID_GROUP + " INTEGER REFERENCES " + GroupHelper.TABLE + " (" + GroupHelper.ID + ") ON DELETE CASCADE NOT NULL);";

    public IngridientsHelper(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void add(List<IngridientsEntity> ingridients) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (IngridientsEntity no : ingridients) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, no.getId());
                contentValues.put(NAME, no.getName());
                contentValues.put(ID_GROUP, no.getIdGroup());
                db.insert(TABLE, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public List<IngridientsEntity> getAllIngridients() {
        Cursor cursor = null;
        List<IngridientsEntity> result = new ArrayList<>();
        try {
            cursor = this.getReadableDatabase().query(TABLE, new String[]{ID, NAME, ID_GROUP}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                IngridientsEntity no = new IngridientsEntity(cursor.getLong(cursor.getColumnIndex(ID)),
                        cursor.getString(cursor.getColumnIndex(NAME)), cursor.getLong(cursor.getColumnIndex(ID_GROUP)));
                result.add(no);
            }
        } finally {
            if (null != cursor)
                cursor.close();
        }
        return result;
    }

    public String getName(long ingridientId) {
        Cursor cursor = null;
        try {
            cursor = this.getReadableDatabase().query(TABLE, new String[]{NAME}, ID + "=?", new String[]{String.valueOf(ingridientId)}, null, null, null);
            return cursor.moveToNext() ? cursor.getString(cursor.getColumnIndex(NAME)) : null;
        } finally {
            if (null != cursor) cursor.close();
        }
    }

    public List<IngridientsEntity> getIngridientsByName(String likeName) {
        likeName = likeName.replaceAll("'", "''");
        likeName = likeName.replace(likeName.charAt(0), likeName.toUpperCase().charAt(0));
        Cursor cursor = null;
        List<IngridientsEntity> ingridients = new ArrayList<>();
        try {
            cursor = this.getReadableDatabase().rawQuery("SELECT " + ID + ", " + NAME + ", " + ID_GROUP +
                    " FROM " + TABLE + " WHERE " + NAME + " LIKE '" + likeName + "%%'", null);
            while (cursor.moveToNext()) {
                IngridientsEntity entity = new IngridientsEntity(cursor.getLong(cursor.getColumnIndex(ID)),
                        cursor.getString(cursor.getColumnIndex(NAME)), cursor.getLong(cursor.getColumnIndex(ID_GROUP)));
                ingridients.add(entity);
            }
        } finally {
            if (null != cursor) cursor.close();
        }
        return ingridients;
    }

}
