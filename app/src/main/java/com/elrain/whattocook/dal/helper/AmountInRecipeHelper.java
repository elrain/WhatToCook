package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.ManyToManyEntity;
import com.elrain.whattocook.dao.NamedEntity;

import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class AmountInRecipeHelper extends DbHelper {
    public static final String TABLE = "amountInRecipe";
    private static final String ID = "_id";
    public static final String ID_AMOUNT = "idAmount";
    public static final String ID_RECIPE = "idRecipe";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, "
            + ID_AMOUNT + " INTEGER REFERENCES " + AmountHelper.TABLE + " (" + AmountHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_RECIPE + " INTEGER REFERENCES " + RecipeHelper.TABLE + " (" + RecipeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public AmountInRecipeHelper(Context context) {
        super(context);
    }

    public void add(List<ManyToManyEntity> rules) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            for (ManyToManyEntity no : rules) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, no.getId());
                contentValues.put(ID_AMOUNT, no.getIdFirst());
                contentValues.put(ID_RECIPE, no.getIdSecond());
                db.insert(TABLE, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }
}
