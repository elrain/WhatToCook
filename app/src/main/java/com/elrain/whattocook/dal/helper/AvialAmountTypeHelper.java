package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;

/**
 * Created by Denys.Husher on 04.06.2015.
 */
public class AvialAmountTypeHelper extends DbHelper {

    public static final String TABLE = "AvailAmountType";
    public static final String ID = "_id";
    public static final String ID_GROUP = "idGroup";
    public static final String ID_AMOUNT_TYPE = "idAmountType";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + ID_GROUP + " INTEGER REFERENCES " + GroupHelper.TABLE + " (" + GroupHelper.ID + ") ON DELETE CASCADE NOT NULL, "
            + ID_AMOUNT_TYPE + " INTEGER REFERENCES " + AmountTypeHelper.TABLE + " (" + AmountTypeHelper.ID + ") ON DELETE CASCADE NOT NULL);";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(ID, 1);
        cv.put(ID_GROUP, 1);
        cv.put(ID_AMOUNT_TYPE, 2);
        db.insert(TABLE, null, cv);

        cv.put(ID, 2);
        cv.put(ID_AMOUNT_TYPE, 5);
        db.insert(TABLE, null, cv);

        cv.put(ID, 3);
        cv.put(ID_GROUP, 2);
        cv.put(ID_AMOUNT_TYPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(ID, 4);
        cv.put(ID_AMOUNT_TYPE, 3);
        db.insert(TABLE, null, cv);

        cv.put(ID, 5);
        cv.put(ID_AMOUNT_TYPE, 4);
        db.insert(TABLE, null, cv);

        cv.put(ID, 6);
        cv.put(ID_GROUP, 3);
        cv.put(ID_AMOUNT_TYPE, 5);
        db.insert(TABLE, null, cv);

        cv.put(ID, 7);
        cv.put(ID_AMOUNT_TYPE, 6);
        db.insert(TABLE, null, cv);

        cv.put(ID, 8);
        cv.put(ID_AMOUNT_TYPE, 2);
        db.insert(TABLE, null, cv);
    }

    public AvialAmountTypeHelper(Context context) {
        super(context);
    }
}
