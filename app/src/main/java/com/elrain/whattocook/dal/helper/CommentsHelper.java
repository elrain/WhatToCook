package com.elrain.whattocook.dal.helper;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class CommentsHelper {

    private static final String TABLE = "comments";
    private static final String ID = "_id";
    private static final String TEXT = "text";
    private static final String TIME = "time";
    private static final String ID_RECIPE = "idRecipe";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, "
            + TEXT + " TEXT NOT NULL, " + TIME + " DATETIME NOT NULL, "
            + ID_RECIPE + " INTEGER REFERENCES " + RecipeHelper.TABLE + " (" + RecipeHelper.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL);";

    public static void createTable(SQLiteDatabase db){
        db.execSQL(CREATE_TABLE);
    }
}
