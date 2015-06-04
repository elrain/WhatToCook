package com.elrain.whattocook.dal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.elrain.whattocook.dal.helper.AmountHelper;
import com.elrain.whattocook.dal.helper.AmountInRecipeHelper;
import com.elrain.whattocook.dal.helper.AmountTypeHelper;
import com.elrain.whattocook.dal.helper.AvialAmountTypeHelper;
import com.elrain.whattocook.dal.helper.CommentsHelper;
import com.elrain.whattocook.dal.helper.CurrentSelectedHelper;
import com.elrain.whattocook.dal.helper.DishTypeHelper;
import com.elrain.whattocook.dal.helper.GroupHelper;
import com.elrain.whattocook.dal.helper.IngridientsHelper;
import com.elrain.whattocook.dal.helper.KitchenTypeHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "whattocook.db";
    private static final int VERSION = 1;

    public DbHelper(Context context) {
        super(context.getApplicationContext(), DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        IngridientsHelper.createTable(db);
        DishTypeHelper.createTable(db);
        KitchenTypeHelper.createTable(db);
        RecipeHelper.createTable(db);
        AmountTypeHelper.createTable(db);
        AmountHelper.createTable(db);
        CommentsHelper.createTable(db);
        AmountInRecipeHelper.createTable(db);
        CurrentSelectedHelper.createTable(db);
        GroupHelper.createTable(db);
        AvialAmountTypeHelper.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
