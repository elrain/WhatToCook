package com.elrain.whattocook.dal;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by elrain on 07.08.15.
 */
public final class CommonHelper {

    private static final String ID = "_id";

    public static boolean isItemExist(SQLiteDatabase db, String tableName, long id) {
        Cursor cursor = null;
        try {
            cursor = db.query(tableName, new String[]{ID}, ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
            return cursor.moveToNext();
        } finally {
            if (null != cursor)
                cursor.close();
        }
    }
}
