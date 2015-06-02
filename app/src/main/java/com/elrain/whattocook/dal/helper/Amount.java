package com.elrain.whattocook.dal.helper;

/**
 * Created by Denys.Husher on 02.06.2015.
 */
public class Amount {

    private static final String TABLE = "amount";
    private static final String ID = "_id";
    private static final String COUNT = "count";
    private static final String ID_INGRIDIENTS = "idIngridients";
    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" + ID + " INTEGER PRIMARY KEY NOT NULL, "
            + COUNT + " INTEGER NOT NULL, "
            + ID_INGRIDIENTS + " INTEGER REFERENCES " + Ingridients.TABLE + " (" + Ingridients.ID + ") ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, idAmountType INTEGER REFERENCES amountType (_id) ON DELETE CASCADE ON UPDATE NO ACTION NOT NULL, idRecipe INTEGER NOT NULL REFERENCES recipe (_id));";
}
