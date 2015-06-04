package com.elrain.whattocook.dal.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dao.Ingridient;
import com.elrain.whattocook.dao.Recipe;

import java.util.ArrayList;
import java.util.Date;
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

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (120) NOT NULL, " + DESCRIPTION + " TEXT NOT NULL, " + COOK_TIME + " INTEGER DEFAULT (0), "
            + ID_DISH_TYPE + " INTEGER REFERENCES " + DishTypeHelper.TABLE + " (" + DishTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_KITCHEN_TYPE + " INTEGER REFERENCES " + KitchenTypeHelper.TABLE + " (" + KitchenTypeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public RecipeHelper(Context context) {
        super(context);
    }

    public static void createTable(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        insertValues(db);
    }

    private static void insertValues(SQLiteDatabase db) {
        ContentValues cv = new ContentValues();
        cv.put(ID, 1);
        cv.put(NAME, "Сальтимбокка");
        cv.put(DESCRIPTION, "" +
                "1.  Разогреть духовку до 250 градусов. Отбить куски вырезки до толщины примерно в сантиметр. Посолить, поперчить, положить на каждый несколько листиков шалфея. \n" +
                "2.  Обернуть каждую отбивную ломтиками прошутто. Если куски получились довольно большими, можно разрезать их для удобства пополам. \n" +
                "3.  В глубокой сковороде с толстым дном растопить 3 столовые ложки сливочного масла — пока оно не начнет пузыриться. Обжарить мясо, выкладывая его шалфеем вниз, до золотистой корочки. \n" +
                "4.  Аккуратно перевернуть, готовить еще минуту. Отправить мясо в духовку на пять минут. \n" +
                "5.  Тем временем плеснуть в сковороду, где жарилась вырезка, вино. Выпаривать его на сильном огне до тех пор, пока количество вина не уменьшится на треть. \n" +
                "6.  Подержать еще немного, интенсивно помешивая, затянуть соус 1 столовой ложкой сливочного масла. \n" +
                "7.  Достать мясо из духовки, дать ему отдохнуть несколько минут. Подавать, полив соусом.");
        cv.put(COOK_TIME, 15);
        cv.put(ID_DISH_TYPE, 1);
        cv.put(ID_KITCHEN_TYPE, 1);
        db.insert(TABLE, null, cv);
    }

    public Recipe getRecipe(long recipeId) {
        Cursor recipeCursor = null;
        Recipe result = null;
        try {
            recipeCursor = this.getReadableDatabase().rawQuery("SELECT re." + ID + ", re." + NAME + ", re." + DESCRIPTION + ", re." + COOK_TIME + ", kt." + KitchenTypeHelper.NAME + " as kt" + NAME +
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
                            recipeCursor.getString(recipeCursor.getColumnIndex(DESCRIPTION)), new Date(recipeCursor.getLong(recipeCursor.getColumnIndex(COOK_TIME))),
                            recipeCursor.getString(recipeCursor.getColumnIndex("kt" + NAME)), recipeCursor.getString(recipeCursor.getColumnIndex("dt" + NAME)),
                            ingridients);
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
