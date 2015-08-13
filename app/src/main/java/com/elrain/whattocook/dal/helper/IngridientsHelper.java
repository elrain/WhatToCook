package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.CommonHelper;
import com.elrain.whattocook.dao.IngridientsEntity;
import com.elrain.whattocook.webutil.rest.response.IngridientsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class IngridientsHelper {
    public static final String TABLE = "ingridients";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String ID_GROUP = "idGroup";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (50) NOT NULL, " + ID_GROUP + " INTEGER REFERENCES " + GroupHelper.TABLE + " (" + GroupHelper.ID + ") ON DELETE CASCADE NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db, IngridientsResponse ingridient) {
        if (!CommonHelper.isItemExist(db, TABLE, ingridient.getIdIngridient())) {
            ContentValues cv = new ContentValues();
            cv.put(ID, ingridient.getIdIngridient());
            cv.put(NAME, ingridient.getName());
            cv.put(ID_GROUP, ingridient.getGroup().getIdGroup());
            db.insert(TABLE, null, cv);
        }
    }

    public static List<IngridientsEntity> getAllIngridients(SQLiteDatabase db) {
        Cursor cursor = null;
        List<IngridientsEntity> result = new ArrayList<>();
        try {
            cursor = db.query(TABLE, new String[]{ID, NAME, ID_GROUP}, null, null, null, null, null);
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

    public static String getName(SQLiteDatabase db, long ingridientId) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE, new String[]{NAME}, ID + "=?", new String[]{String.valueOf(ingridientId)}, null, null, null);
            return cursor.moveToNext() ? cursor.getString(cursor.getColumnIndex(NAME)) : null;
        } finally {
            if (null != cursor) cursor.close();
        }
    }

    public static IngridientsEntity getIngridientById(SQLiteDatabase db, long ingridientId) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE, new String[]{ID, NAME, ID_GROUP}, ID + " =? ", new String[]{String.valueOf(ingridientId)}, null, null, null);
            if (cursor.moveToNext()) {
                return new IngridientsEntity(cursor.getLong(cursor.getColumnIndex(ID)),
                        cursor.getString(cursor.getColumnIndex(NAME)), cursor.getLong(cursor.getColumnIndex(ID_GROUP)));
            }
        } finally {
            if (null != cursor) cursor.close();
        }

        return null;
    }

    public static List<IngridientsEntity> getIngridientsByName(SQLiteDatabase db, String likeName) {
        likeName = likeName.replaceAll("'", "''");
        String likeNameUpper = likeName.replace(likeName.charAt(0), likeName.toUpperCase().charAt(0));
        Cursor cursor = null;
        List<IngridientsEntity> ingridients = new ArrayList<>();
        try {
            cursor = db.rawQuery("SELECT " + ID + ", " + NAME + ", " + ID_GROUP +
                    " FROM " + TABLE + " WHERE " + NAME + " LIKE '" + likeName + "%%' OR " + NAME + " LIKE '" + likeNameUpper + "%%'", null);
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
