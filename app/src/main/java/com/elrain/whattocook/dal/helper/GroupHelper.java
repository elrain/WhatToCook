package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.CommonHelper;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.webutil.rest.response.GroupsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 04.06.2015.
 */
public class GroupHelper {
    public static final String TABLE = "groups";
    public static final String ID = "_id";
    public static final String NAME = "name";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db, GroupsResponse group) {
        if (!CommonHelper.isItemExist(db, TABLE, group.getIdGroup())) {
            ContentValues cv = new ContentValues();
            cv.put(ID, group.getIdGroup());
            cv.put(NAME, group.getName());
            db.insert(TABLE, null, cv);
        }
    }

    public static List<NamedEntity> getAll(SQLiteDatabase db) {
        Cursor cursor = null;
        List<NamedEntity> result = new ArrayList<>();
        try {
            cursor = db.query(TABLE, new String[]{ID, NAME}, null, null, null, null, null);
            while (cursor.moveToNext()) {
                NamedEntity ne = new NamedEntity();
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
