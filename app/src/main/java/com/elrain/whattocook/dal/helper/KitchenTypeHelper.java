package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.NamedEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class KitchenTypeHelper extends DbHelper {
    public static final String TABLE = "kitchenType";
    public static final String ID = "_id";
    public static final String NAME = "name";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";

    public KitchenTypeHelper(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void add(List<NamedEntity> kitchens) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (NamedEntity no : kitchens) {
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

    public List<NamedEntity> getAll() {
        Cursor cursor = null;
        List<NamedEntity> result = new ArrayList<>();
        NamedEntity ne = new NamedEntity();
        ne.setId(0);
        ne.setName(getContext().getString(R.string.text_all));
        result.add(ne);
        try {
            cursor = this.getReadableDatabase().query(TABLE, new String[]{ID, NAME}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                ne = new NamedEntity();
                ne.setId(cursor.getLong(cursor.getColumnIndex(ID)));
                ne.setName(cursor.getString(cursor.getColumnIndex(NAME)));
                result.add(ne);
            }
        } finally {
            if (null != cursor) cursor.close();
        }
        return result;
    }
}
