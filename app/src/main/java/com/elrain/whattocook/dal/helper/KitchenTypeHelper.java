package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.R;
import com.elrain.whattocook.dal.CommonHelper;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.webutil.rest.response.KitchenTypeResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class KitchenTypeHelper {
    public static final String TABLE = "kitchenType";
    public static final String ID = "_id";
    public static final String NAME = "name";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";


    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db, KitchenTypeResponse kitchen) {
        if (!CommonHelper.isItemExist(db, TABLE, kitchen.getIdKitchenType())) {
            ContentValues cv = new ContentValues();
            cv.put(ID, kitchen.getIdKitchenType());
            cv.put(NAME, kitchen.getName());
            db.insert(TABLE, null, cv);
        }
    }

    public static List<NamedEntity> getAll(SQLiteDatabase db, Context context) {
        Cursor cursor = null;
        List<NamedEntity> result = new ArrayList<>();
        NamedEntity ne = new NamedEntity();
        ne.setId(0);
        ne.setName(context.getString(R.string.text_all));
        result.add(ne);
        try {
            cursor = db.query(TABLE, new String[]{ID, NAME}, null, null, null, null, null);
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
