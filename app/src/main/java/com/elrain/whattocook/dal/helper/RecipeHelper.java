package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dao.Recipe;
import com.elrain.whattocook.dao.RecipeIngridientsEntity;
import com.elrain.whattocook.util.ImageUtil;
import com.elrain.whattocook.util.Preferences;
import com.elrain.whattocook.webutil.rest.response.RecipeResponse;

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
    private static final String IS_SAVED = "isSaved";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (120) NOT NULL, " + DESCRIPTION + " TEXT NOT NULL, " + IS_SAVED + " BOOLEAN, " + COOK_TIME + " INTEGER DEFAULT (0), " + IMAGE + " TEXT, "
            + ID_DISH_TYPE + " INTEGER REFERENCES " + DishTypeHelper.TABLE + " (" + DishTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_KITCHEN_TYPE + " INTEGER REFERENCES " + KitchenTypeHelper.TABLE + " (" + KitchenTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";
    private static final String AMOUNT_INGRIDIENT_SUM_VIEW = "v1";
    private static final String RECIPE_INGRIDIENT_SUM_VIEW = "v2";

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

    public static void add(SQLiteDatabase db, Context context, RecipeResponse recipe) {
        if (!isRecipeExist(db, recipe.getIdRecipe())) {
            String filePath = context.getExternalFilesDir(null) != null ? context.getExternalFilesDir(null).getAbsolutePath() : context.getFilesDir().getAbsolutePath();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, recipe.getIdRecipe());
            contentValues.put(NAME, recipe.getName());
            contentValues.put(DESCRIPTION, recipe.getDescription());
            contentValues.put(COOK_TIME, recipe.getCookTime());
            contentValues.put(ID_KITCHEN_TYPE, recipe.getKitchen().getIdKitchenType());
            contentValues.put(ID_DISH_TYPE, recipe.getDishType().getIdDishType());
            contentValues.put(IMAGE, ImageUtil.saveImage(recipe.getIdRecipe(), recipe.getImage(), filePath));
            db.insert(TABLE, null, contentValues);
        }
    }

    private static boolean isRecipeExist(SQLiteDatabase db, long id) {
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE, new String[]{ID}, ID + " = ? ", new String[]{String.valueOf(id)}, null, null, null);
            return cursor.moveToNext();
        } finally {
            if (null != cursor)
                cursor.close();
        }
    }

    public static List<Recipe> getAllRecipes(SQLiteDatabase db, Preferences preferences) {
        List<Recipe> result = new ArrayList<>();
        Cursor cursor = null;
        String where = generateWhere(preferences);
        try {
            cursor = db.rawQuery("SELECT re." + ID + ", re." + IS_SAVED + ", re." + IMAGE + ", re." + NAME + ", re."
                    + DESCRIPTION + ", re." + COOK_TIME + ", kt." + KitchenTypeHelper.NAME + " as kt" + NAME +
                    ", dt." + DishTypeHelper.NAME + " as dt" + NAME +
                    " FROM " + TABLE + " as re LEFT JOIN " + KitchenTypeHelper.TABLE + " as kt on re." + ID_KITCHEN_TYPE + " = kt." + KitchenTypeHelper.ID +
                    " LEFT JOIN " + DishTypeHelper.TABLE + " as dt on re." + ID_DISH_TYPE + " = dt." + DishTypeHelper.ID + " " + where + ";", null);
            while (cursor.moveToNext()) {
                Cursor ingridientsCursor = null;
                try {
                    long id = cursor.getLong(cursor.getColumnIndex(ID));
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
                    Recipe r = new Recipe(cursor.getLong(cursor.getColumnIndex(ID)), cursor.getString(cursor.getColumnIndex(NAME)),
                            cursor.getString(cursor.getColumnIndex(DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(COOK_TIME)),
                            cursor.getString(cursor.getColumnIndex("dt" + NAME)), cursor.getString(cursor.getColumnIndex("kt" + NAME)),
                            cursor.getString(cursor.getColumnIndex(IMAGE)), cursor.getInt(cursor.getColumnIndex(IS_SAVED)) == 1, ingridients);
                    result.add(r);

                } finally {
                    if (null != ingridientsCursor)
                        ingridientsCursor.close();
                }
            }
        } finally {
            if (null != cursor)
                cursor.close();
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
        Cursor cursor = null;
        Recipe result = null;
        try {
            cursor = db.rawQuery("SELECT re." + ID + ", re." + IS_SAVED + ", re." + IMAGE + ", re." + NAME + ", re." + DESCRIPTION + ", re." + COOK_TIME + ", kt." + KitchenTypeHelper.NAME + " as kt" + NAME +
                    ", dt." + DishTypeHelper.NAME + " as dt" + NAME + " FROM " + TABLE + " as re LEFT JOIN " + KitchenTypeHelper.TABLE + " as kt on re." + ID_KITCHEN_TYPE + " = kt." + KitchenTypeHelper.ID +
                    "                          LEFT JOIN " + DishTypeHelper.TABLE + " as dt on re." + ID_DISH_TYPE + " = dt." + DishTypeHelper.ID +
                    " WHERE re." + ID + " = ?;", new String[]{String.valueOf(recipeId)});
            if (cursor.moveToNext()) {
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
                    result = new Recipe(cursor.getLong(cursor.getColumnIndex(ID)), cursor.getString(cursor.getColumnIndex(NAME)),
                            cursor.getString(cursor.getColumnIndex(DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(COOK_TIME)),
                            cursor.getString(cursor.getColumnIndex("dt" + NAME)), cursor.getString(cursor.getColumnIndex("kt" + NAME)),
                            cursor.getString(cursor.getColumnIndex(IMAGE)), cursor.getInt(cursor.getColumnIndex(IS_SAVED)) == 1, ingridients);
                } finally {
                    if (null != ingridientsCursor)
                        ingridientsCursor.close();
                }
            }
        } finally {
            if (null != cursor)
                cursor.close();
        }

        return result;
    }

    public static List<Recipe> getPossibleRecipes(SQLiteDatabase db, long[] ids) {
        List<Recipe> recipes = new ArrayList<>();
        createView1(db, ids);
        createView2(db);

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT " + AMOUNT_INGRIDIENT_SUM_VIEW + ".id AS id, " +
                    " (" + AMOUNT_INGRIDIENT_SUM_VIEW + ".row = " + RECIPE_INGRIDIENT_SUM_VIEW + ".row) as result " +
                    " FROM " + AMOUNT_INGRIDIENT_SUM_VIEW + " LEFT JOIN " + RECIPE_INGRIDIENT_SUM_VIEW + " " +
                    " ON " + AMOUNT_INGRIDIENT_SUM_VIEW + ".id = " + RECIPE_INGRIDIENT_SUM_VIEW + ".id " +
                    " GROUP BY " + AMOUNT_INGRIDIENT_SUM_VIEW + ".id HAVING result = 1", null);
            while (cursor.moveToNext()) {
                Recipe r = getRecipe(db, cursor.getLong(cursor.getColumnIndex("id")));
                recipes.add(r);
            }
        } finally {
            if (null != cursor)
                cursor.close();
        }

        return recipes;
    }

    private static void createView2(SQLiteDatabase db) {
        db.execSQL("DROP VIEW IF EXISTS " + RECIPE_INGRIDIENT_SUM_VIEW + ";");
        db.execSQL("CREATE VIEW " + RECIPE_INGRIDIENT_SUM_VIEW + " AS SELECT " +
                " air." + AmountInRecipeHelper.ID_RECIPE + " as id, COUNT(air." + AmountInRecipeHelper.ID_RECIPE + ") " +
                " as 'row' FROM " + AmountInRecipeHelper.TABLE + " AS air " +
                " LEFT JOIN " + RecipeHelper.TABLE + " AS r ON air." + AmountInRecipeHelper.ID_RECIPE +
                " = r." + RecipeHelper.ID + " GROUP BY air." + AmountInRecipeHelper.ID_RECIPE);
    }

    private static void createView1(SQLiteDatabase db, long[] ids) {
        String idsString = " ";
        for (Long id : ids) {
            idsString += "'" + String.valueOf(id) + "', ";
        }
        idsString = idsString.substring(0, idsString.lastIndexOf(','));
        db.execSQL("DROP VIEW IF EXISTS " + AMOUNT_INGRIDIENT_SUM_VIEW + ";");
        db.execSQL("CREATE VIEW " + AMOUNT_INGRIDIENT_SUM_VIEW + " AS SELECT " +
                " air." + AmountInRecipeHelper.ID_RECIPE + " AS id, COUNT(air." + AmountInRecipeHelper.ID_RECIPE + ") AS 'row' " +
                " FROM " + AmountInRecipeHelper.TABLE + " AS air LEFT JOIN " + AmountHelper.TABLE +
                " AS a ON air." + AmountInRecipeHelper.ID_AMOUNT + " = a." + AmountHelper.ID + " WHERE " +
                " a." + AmountHelper.ID_INGRIDIENTS + " IN (" + idsString + ") " +
                " GROUP BY air." + AmountInRecipeHelper.ID_RECIPE + "");
    }

    public static void saveUnsaveRecipe(SQLiteDatabase db, long recipeId, boolean save) {
        ContentValues cv = new ContentValues();
        cv.put(IS_SAVED, save ? 1 : 0);
        db.update(TABLE, cv, ID + " =? ", new String[]{String.valueOf(recipeId)});
    }

    public static List<Recipe> getSavedRecipes(SQLiteDatabase db) {
        Cursor cursor = null;
        List<Recipe> result = new ArrayList<>();
        try {
            cursor = db.rawQuery("SELECT re." + ID + ", re." + IS_SAVED + ", re." + IMAGE + ", re." + NAME + ", re." + DESCRIPTION + ", re." + COOK_TIME + ", kt." + KitchenTypeHelper.NAME + " as kt" + NAME +
                    ", dt." + DishTypeHelper.NAME + " as dt" + NAME + " FROM " + TABLE + " as re LEFT JOIN " + KitchenTypeHelper.TABLE + " as kt on re." + ID_KITCHEN_TYPE + " = kt." + KitchenTypeHelper.ID +
                    "                          LEFT JOIN " + DishTypeHelper.TABLE + " as dt on re." + ID_DISH_TYPE + " = dt." + DishTypeHelper.ID +
                    " WHERE re." + IS_SAVED + " = ?;", new String[]{String.valueOf(1)});
            while (cursor.moveToNext()) {
                Recipe r = new Recipe(cursor.getLong(cursor.getColumnIndex(ID)), cursor.getString(cursor.getColumnIndex(NAME)),
                        cursor.getString(cursor.getColumnIndex(DESCRIPTION)), cursor.getInt(cursor.getColumnIndex(COOK_TIME)),
                        cursor.getString(cursor.getColumnIndex("dt" + NAME)), cursor.getString(cursor.getColumnIndex("kt" + NAME)),
                        cursor.getString(cursor.getColumnIndex(IMAGE)), cursor.getInt(cursor.getColumnIndex(IS_SAVED)) == 1, null);
                result.add(r);
            }
        } finally {
            if (null != cursor)
                cursor.close();
        }
        return result;
    }


}
