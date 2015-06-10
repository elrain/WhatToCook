package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.SelectedIngridientsEntity;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class CurrentSelectedHelper extends DbHelper {
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

    public CurrentSelectedHelper(Context context) {
        super(context);
    }

    public void addIngridient(SelectedIngridientsEntity ingridient){
        ContentValues cv = new ContentValues();
        cv.put(QUANTITY, ingridient.getQuantity());
        cv.put(ID_INGRIDIENT, ingridient.getIngridientsEntity().getId());
        cv.put(ID_AMOUNT_TYPE, ingridient.getIdAmountType());
        this.getWritableDatabase().insert(TABLE, null, cv);
    }
}
