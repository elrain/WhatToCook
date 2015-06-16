package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dao.Recipe;
import com.elrain.whattocook.dao.RecipeEntity;
import com.elrain.whattocook.dao.RecipeIngridientsEntity;
import com.elrain.whattocook.util.ImageUtil;
import com.elrain.whattocook.util.Preferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class RecipeHelper {
    public static final String TABLE = "recipe";
    public static final String ID = "_id";
    public static final String WHERE = " WHERE ";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String COOK_TIME = "cookTime";
    private static final String ID_DISH_TYPE = "idDishType";
    private static final String ID_KITCHEN_TYPE = "idKitchenType";
    private static final String IMAGE = "image";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (120) NOT NULL, " + DESCRIPTION + " TEXT NOT NULL, " + COOK_TIME + " INTEGER DEFAULT (0), " + IMAGE + " TEXT, "
            + ID_DISH_TYPE + " INTEGER REFERENCES " + DishTypeHelper.TABLE + " (" + DishTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_KITCHEN_TYPE + " INTEGER REFERENCES " + KitchenTypeHelper.TABLE + " (" + KitchenTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public static int getRecipeCount(SQLiteDatabase db) {
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT COUNT(*) as " + NAME + " FROM " + TABLE, null);
            if (cursor.moveToNext())
                return cursor.getInt(cursor.getColumnIndex(NAME));
        } finally {
            if (null != cursor)
                cursor.close();
        }
        return 0;
    }

    public static void add(SQLiteDatabase db, Context context, List<RecipeEntity> recipes) {
        String filePath = context.getExternalFilesDir(null) != null ? context.getExternalFilesDir(null).getAbsolutePath() : context.getFilesDir().getAbsolutePath();
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

    public static List<Recipe> getAllRecipes(SQLiteDatabase db, Preferences preferences) {
        List<Recipe> result = new ArrayList<>();
        Cursor recipeCursor = null;
        String where = generateWhere(preferences);
        try {
            recipeCursor = db.rawQuery("SELECT re." + ID + ", re." + IMAGE + ", re." + NAME + ", re."
                    + DESCRIPTION + ", re." + COOK_TIME + ", kt." + KitchenTypeHelper.NAME + " as kt" + NAME +
                    ", dt." + DishTypeHelper.NAME + " as dt" + NAME +
                    " FROM " + TABLE + " as re LEFT JOIN " + KitchenTypeHelper.TABLE + " as kt on re." + ID_KITCHEN_TYPE + " = kt." + KitchenTypeHelper.ID +
                    " LEFT JOIN " + DishTypeHelper.TABLE + " as dt on re." + ID_DISH_TYPE + " = dt." + DishTypeHelper.ID + " " + where + ";", null);
            while (recipeCursor.moveToNext()) {
                Cursor ingridientsCursor = null;
                try {
                    long id = recipeCursor.getLong(recipeCursor.getColumnIndex(ID));
                    List<RecipeIngridientsEntity> ingridients = new ArrayList<>();
                    ingridientsCursor = db.rawQuery("SELECT a." + AmountHelper.COUNT + ", i." + IngridientsHelper.NAME + " as i" + NAME +
                            ", at." + AmountTypeHelper.NAME + " as at" + NAME + " FROM " + AmountInRecipeHelper.TABLE + " as air " +
                            " LEFT JOIN " + AmountHelper.TABLE + " as a on a." + AmountHelper.ID + " = air." + AmountInRecipeHelper.ID_AMOUNT +
                            " LEFT JOIN " + IngridientsHelper.TABLE + " as i on i." + IngridientsHelper.ID + " = a." + AmountHelper.ID_INGRIDIENTS +
                            " LEFT JOIN " + AmountTypeHelper.TABLE + " as at on at." + AmountTypeHelper.ID + " = a." + AmountHelper.ID_AMOUNT_TYPE +
                            " WHERE air." + AmountInRecipeHelper.ID_RECIPE + " = ?;", new String[]{String.valueOf(id)});
                    while (ingridientsCursor.moveToNext()) {
                        RecipeIngridientsEntity ingridient = new RecipeIngridientsEntity(ingridientsCursor.getInt(ingridientsCursor.getColumnIndex(AmountHelper.COUNT)),
                                ingridientsCursor.getString(ingridientsCursor.getColumnIndex("i" + NAME)),
                                ingridientsCursor.getString(ingridientsCursor.getColumnIndex("at" + NAME)));
                        ingridients.add(ingridient);
                    }
                    Recipe r = new Recipe(recipeCursor.getLong(recipeCursor.getColumnIndex(ID)), recipeCursor.getString(recipeCursor.getColumnIndex(NAME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex(DESCRIPTION)), recipeCursor.getInt(recipeCursor.getColumnIndex(COOK_TIME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex("dt" + NAME)), recipeCursor.getString(recipeCursor.getColumnIndex("kt" + NAME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex(IMAGE)), ingridients);
                    result.add(r);

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

    private static String generateWhere(Preferences preferences) {
        String result = "";
        if (0 != preferences.getKitchenTypeId())
            result = WHERE + " re." + ID_KITCHEN_TYPE + " = " + String.valueOf(preferences.getKitchenTypeId());
        if (0 != preferences.getDishTypeId()) {
            if ("".equals(result))
                result = WHERE + " re." + ID_DISH_TYPE + " = " + String.valueOf(preferences.getDishTypeId());
            else
                result += " AND re." + ID_DISH_TYPE + " = " + String.valueOf(preferences.getDishTypeId());
        }
        return result;
    }

    public static Recipe getRecipe(SQLiteDatabase db, long recipeId) {
        Cursor recipeCursor = null;
        Recipe result = null;
        try {
            recipeCursor = db.rawQuery("SELECT re." + ID + ", re." + IMAGE + ", re." + NAME + ", re." + DESCRIPTION + ", re." + COOK_TIME + ", kt." + KitchenTypeHelper.NAME + " as kt" + NAME +
                    ", dt." + DishTypeHelper.NAME + " as dt" + NAME + " FROM " + TABLE + " as re LEFT JOIN " + KitchenTypeHelper.TABLE + " as kt on re." + ID_KITCHEN_TYPE + " = kt." + KitchenTypeHelper.ID +
                    "                          LEFT JOIN " + DishTypeHelper.TABLE + " as dt on re." + ID_DISH_TYPE + " = dt." + DishTypeHelper.ID +
                    " WHERE re." + ID + " = ?;", new String[]{String.valueOf(recipeId)});
            if (recipeCursor.moveToNext()) {
                Cursor ingridientsCursor = null;
                try {
                    List<RecipeIngridientsEntity> ingridients = new ArrayList<>();
                    ingridientsCursor = db.rawQuery("SELECT a." + AmountHelper.COUNT + ", i." + IngridientsHelper.NAME + " as i" + NAME +
                            ", at." + AmountTypeHelper.NAME + " as at" + NAME + " FROM " + AmountInRecipeHelper.TABLE + " as air " +
                            "                               LEFT JOIN " + AmountHelper.TABLE + " as a on a." + AmountHelper.ID + " = air." + AmountInRecipeHelper.ID_AMOUNT +
                            "                               LEFT JOIN " + IngridientsHelper.TABLE + " as i on i." + IngridientsHelper.ID + " = a." + AmountHelper.ID_INGRIDIENTS +
                            "                               LEFT JOIN " + AmountTypeHelper.TABLE + " as at on at." + AmountTypeHelper.ID + " = a." + AmountHelper.ID_AMOUNT_TYPE +
                            "    WHERE air." + AmountInRecipeHelper.ID_RECIPE + " = ?;", new String[]{String.valueOf(recipeId)});
                    while (ingridientsCursor.moveToNext()) {
                        RecipeIngridientsEntity ingridient = new RecipeIngridientsEntity(ingridientsCursor.getInt(ingridientsCursor.getColumnIndex(AmountHelper.COUNT)),
                                ingridientsCursor.getString(ingridientsCursor.getColumnIndex("i" + NAME)),
                                ingridientsCursor.getString(ingridientsCursor.getColumnIndex("at" + NAME)));
                        ingridients.add(ingridient);
                    }
                    result = new Recipe(recipeCursor.getLong(recipeCursor.getColumnIndex(ID)), recipeCursor.getString(recipeCursor.getColumnIndex(NAME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex(DESCRIPTION)), recipeCursor.getInt(recipeCursor.getColumnIndex(COOK_TIME)),
                            recipeCursor.getString(recipeCursor.getColumnIndex("dt" + NAME)), recipeCursor.getString(recipeCursor.getColumnIndex("kt" + NAME)),
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
