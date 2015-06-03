package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class AmountInRecipeHelper extends DbHelper{

    public static final String TABLE = "amountInRecipe";
    private static final String ID = "_id";
    public static final String ID_AMOUNT = "idAmount";
    public static final String ID_RECIPE = "idRecipe";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, "
            + ID_AMOUNT + " INTEGER REFERENCES " + AmountHelper.TABLE + " (" + AmountHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_RECIPE + " INTEGER REFERENCES " + RecipeHelper.TABLE + " (" + RecipeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";


    public static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(ID, 1);
        cv.put(ID_AMOUNT, 1);
        cv.put(ID_RECIPE, 1);
        db.insert(TABLE, null, cv);

        cv.put(ID, 2);
        cv.put(ID_AMOUNT, 2);
        db.insert(TABLE, null, cv);

        cv.put(ID, 3);
        cv.put(ID_AMOUNT, 3);
        db.insert(TABLE, null, cv);

        cv.put(ID, 4);
        cv.put(ID_AMOUNT, 4);
        db.insert(TABLE, null, cv);

        cv.put(ID, 5);
        cv.put(ID_AMOUNT, 5);
        db.insert(TABLE, null, cv);

        cv.put(ID, 6);
        cv.put(ID_AMOUNT, 6);
        db.insert(TABLE, null, cv);
    }

    public AmountInRecipeHelper(Context context) {
        super(context);
    }
}
