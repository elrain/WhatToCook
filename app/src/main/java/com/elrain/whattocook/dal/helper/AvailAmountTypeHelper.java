package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Denys.Husher on 04.06.2015.
 */
public class AvailAmountTypeHelper {
    public static final String TABLE = "AvailAmountType";
    public static final String ID = "_id";
    public static final String ID_GROUP = "idGroup";
    public static final String ID_AMOUNT_TYPE = "idAmountType";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + ID_GROUP + " INTEGER REFERENCES " + GroupHelper.TABLE + " (" + GroupHelper.ID + ") ON DELETE CASCADE NOT NULL, "
            + ID_AMOUNT_TYPE + " INTEGER REFERENCES " + AmountTypeHelper.TABLE + " (" + AmountTypeHelper.ID + ") ON DELETE CASCADE NOT NULL, "
            + "UNIQUE("+ID_GROUP+", "+ID_AMOUNT_TYPE+") ON CONFLICT IGNORE);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db, long idGroup, long idAmountType) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_GROUP, idGroup);
        contentValues.put(ID_AMOUNT_TYPE, idAmountType);
        db.insert(TABLE, null, contentValues);
    }
}
