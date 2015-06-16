package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dao.IngridientsEntity;
import com.elrain.whattocook.dao.SelectedIngridientsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class CurrentSelectedHelper {
    private static final String TABLE = "currentSelect";
    private static final String ID = "_id";
    private static final String QUANTITY = "quantity";
    private static final String ID_INGRIDIENT = "idIngridient";
    private static final String ID_AMOUNT_TYPE = "idAmountType";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, "
            + QUANTITY + " INTEGER NOT NULL, " + ID_INGRIDIENT + " INTEGER REFERENCES " + IngridientsHelper.TABLE + " (" + IngridientsHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_AMOUNT_TYPE + " INTEGER REFERENCES " + AmountTypeHelper.TABLE + " (" + AmountTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void addIngridient(SQLiteDatabase db, SelectedIngridientsEntity ingridient) {
        ContentValues cv = new ContentValues();
        cv.put(QUANTITY, ingridient.getQuantity());
        cv.put(ID_INGRIDIENT, ingridient.getIngridientsEntity().getId());
        cv.put(ID_AMOUNT_TYPE, ingridient.getIdAmountType());
        db.insert(TABLE, null, cv);
    }

    public static List<SelectedIngridientsEntity> getSelectedIngridients(SQLiteDatabase db) {
        Cursor cursor = null;
        List<SelectedIngridientsEntity> result = new ArrayList<>();
        try {
            cursor = db.rawQuery("SELECT cs." + ID + ", cs." + ID_AMOUNT_TYPE + ", cs." + QUANTITY +
                    ", cs." + ID_INGRIDIENT + ", i." + IngridientsHelper.NAME + ", i." + IngridientsHelper.ID_GROUP +
                    "  FROM " + TABLE + " as cs LEFT JOIN " + IngridientsHelper.TABLE +
                    " as i ON cs." + ID + " = i." + IngridientsHelper.ID, null);
            while (cursor.moveToNext()) {
                IngridientsEntity ingridientsEntity = new IngridientsEntity(cursor.getLong(cursor.getColumnIndex(ID_INGRIDIENT)),
                        cursor.getString(cursor.getColumnIndex(IngridientsHelper.NAME)),
                        cursor.getLong(cursor.getColumnIndex(IngridientsHelper.ID_GROUP)));
                SelectedIngridientsEntity selectedIngridientsEntity = new SelectedIngridientsEntity(
                        cursor.getLong(cursor.getColumnIndex(ID)), cursor.getLong(cursor.getColumnIndex(ID_AMOUNT_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(QUANTITY)), ingridientsEntity);
                result.add(selectedIngridientsEntity);
            }
        } finally {
            if (null != cursor) cursor.close();
        }
        return result;
    }

    public static void clearTable(SQLiteDatabase db) {
        db.execSQL("DELETE FROM " + TABLE);
    }
}
