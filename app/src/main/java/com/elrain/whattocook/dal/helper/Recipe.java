package com.elrain.whattocook.dal.helper;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class Recipe {

    public static final String TABLE = "recipe";
    public static final String ID = "_id";
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private static final String COOK_TIME = "cookTime";
    private static final String ID_DISH_TYPE = "idDishType";
    private static final String ID_KITCHEN_TYPE = "idKitchenType";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
            + NAME + " VARCHAR (120) NOT NULL, " + DESCRIPTION + " TEXT NOT NULL, " + COOK_TIME + " INTEGER DEFAULT (0), "
            + ID_DISH_TYPE + " INTEGER REFERENCES " + DishType.TABLE + " (" + DishType.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, "
            + ID_KITCHEN_TYPE + " INTEGER REFERENCES " + KitchenType.TABLE + " (" + KitchenType.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }
}
