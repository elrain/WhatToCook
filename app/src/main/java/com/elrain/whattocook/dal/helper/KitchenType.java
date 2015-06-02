package com.elrain.whattocook.dal.helper;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class KitchenType {

    public static final String TABLE = "kitchenType";
    public static final String ID = "_id";
    private static final String NAME = "name";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " + NAME + " VARCHAR (50) NOT NULL);";
}
