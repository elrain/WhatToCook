package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.webutil.rest.response.AmountResponse;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class AmountInRecipeHelper {
    public static final String TABLE = "amountInRecipe";
    private static final String ID = "_id";
    public static final String ID_AMOUNT = "idAmount";
    public static final String ID_RECIPE = "idRecipe";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, "
            + ID_AMOUNT + " INTEGER REFERENCES " + AmountHelper.TABLE + " (" + AmountHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_RECIPE + " INTEGER REFERENCES " + RecipeHelper.TABLE + " (" + RecipeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, " +
            "UNIQUE("+ID_AMOUNT+", "+ID_RECIPE+") ON CONFLICT IGNORE);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static void add(SQLiteDatabase db, int amountId, int recipeId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(ID_RECIPE, recipeId);
        contentValues.put(ID_AMOUNT, amountId);
        db.insert(TABLE, null, contentValues);
    }
}
