package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.Ingridient;
import com.elrain.whattocook.dao.Recipe;
import com.elrain.whattocook.dao.RecipeEntity;
import com.elrain.whattocook.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class RecipeHelper extends DbHelper {
    public static final String TABLE = "recipe";
    public static final String ID = "_id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String COOK_TIME = "cookTime";
    private static final String ID_DISH_TYPE = "idDishType";
    private static final String ID_KITCHEN_TYPE = "idKitchenType";
    private static final String IMAGE = "image";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (120) NOT NULL, " + DESCRIPTION + " TEXT NOT NULL, " + COOK_TIME + " INTEGER DEFAULT (0), " + IMAGE + " STRING, "
            + ID_DISH_TYPE + " INTEGER REFERENCES " + DishTypeHelper.TABLE + " (" + DishTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_KITCHEN_TYPE + " INTEGER REFERENCES " + KitchenTypeHelper.TABLE + " (" + KitchenTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public RecipeHelper(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public int getRecipeCount() {
        Cursor cursor = null;
        try {
            cursor = this.getReadableDatabase().rawQuery("SELECT COUNT(*) as " + NAME + " FROM " + TABLE, null);
            if (cursor.moveToNext())
                return cursor.getInt(cursor.getColumnIndex(NAME));
        } finally {
            if (null != cursor)
                cursor.close();
        }
        return 0;
    }

    public void add(List<RecipeEntity> recipes) {
        SQLiteDatabase db = this.getWritableDatabase();
        String filePath = getContext().getExternalFilesDir(null) != null ? getContext().getExternalFilesDir(null).getAbsolutePath() : getContext().getFilesDir().getAbsolutePath();
        db.beginTransaction();
        try {
            for (RecipeEntity no : recipes) {
                ContentValues contentValues = new ContentValues();
                contentValues.put(ID, no.getId());
                contentValues.put(NAME, no.getName());
                contentValues.put(DESCRIPTION, no.getDescription());
                contentValues.put(COOK_TIME, no.getCookTime());
                contentValues.put(ID_KITCHEN_TYPE, no.getIdKitchenType());
                contentValues.put(ID_DISH_TYPE, no.getIdDishType());
                contentValues.put(IMAGE, ImageUtil.saveImage(no.getId(), no.getImage(), filePath));
                db.insert(TABLE, null, contentValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }

    public Recipe getRecipe(long recipeId) {
        Cursor recipeCursor = null;
        Recipe result = null;
        try {
            recipeCursor = this.getReadableDatabase().rawQuery("SELECT re." + ID + ", re." + IMAGE + ", re." + NAME + ", re." + DESCRIPTION + ", re." + COOK_TIME + ", kt." + KitchenTypeHelper.NAME + " as kt" + NAME +
                    ", dt." + DishTypeHelper.NAME + " as dt" + NAME + " FROM " + TABLE + " as re LEFT JOIN " + KitchenTypeHelper.TABLE + " as kt on re." + ID + " = kt." + KitchenTypeHelper.ID +
                    "                          LEFT JOIN " + DishTypeHelper.TABLE + " as dt on re." + ID + " = dt." + DishTypeHelper.ID +
                    " WHERE re." + ID + " = ?;", new String[]{String.valueOf(recipeId)});
            if (recipeCursor.moveToNext()) {
                Cursor ingridientsCursor = null;
                try {
                    List<Ingridient> ingridients = new ArrayList<>();
                    ingridientsCursor = this.getReadableDatabase().rawQuery("SELECT a." + AmountHelper.COUNT + ", i." + IngridientsHelper.NAME + " as i" + NAME +
                            ", at." + AmountTypeHelper.NAME + " as at" + NAME + " FROM " + AmountInRecipeHelper.TABLE + " as air " +
                            "                               LEFT JOIN " + AmountHelper.TABLE + " as a on a." + AmountHelper.ID + " = air." + AmountInRecipeHelper.ID_AMOUNT +
                            "                               LEFT JOIN " + IngridientsHelper.TABLE + " as i on i." + IngridientsHelper.ID + " = a." + AmountHelper.ID_INGRIDIENTS +
                            "                               LEFT JOIN " + AmountTypeHelper.TABLE + " as at on at." + AmountTypeHelper.ID + " = a." + AmountHelper.ID_AMOUNT_TYPE +
                            "    WHERE air." + AmountInRecipeHelper.ID_RECIPE + " = ?;", new String[]{String.valueOf(recipeId)});
                    while (ingridientsCursor.moveToNext()) {
                        Ingridient ingridient = new Ingridient(ingridientsCursor.getInt(ingridientsCursor.getColumnIndex(AmountHelper.COUNT)),
                                ingridientsCursor.getString(ingridientsCursor.getColumnIndex("i" + NAME)),
                                ingridientsCursor.getString(ingridientsCursor.getColumnIndex("at" + NAME)));
                        ingridients.add(ingridient);
                    }
                    result = new Recipe(recipeCursor.getLong(recipeCursor.getColumnIndex(ID)), recipeCursor.getString(recipeCursor.getColumnIndex(NAME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex(DESCRIPTION)), recipeCursor.getInt(recipeCursor.getColumnIndex(COOK_TIME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex("kt" + NAME)), recipeCursor.getString(recipeCursor.getColumnIndex("dt" + NAME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex(IMAGE)), ingridients);
                } finally {
                    if (null != ingridientsCursor)
                        ingridientsCursor.close();
                }
            }
        } finally {
            if (null != recipeCursor)
                recipeCursor.close();
        }

        return result;
    }
}
