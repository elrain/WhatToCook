package com.elrain.whattocook.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class Preferences {

    private static final String PREFERENCES_NAME = "whatToCookPref";
    private static final String KEY_KITCHEN = "kitchen";
    private static final String KEY_DISH = "dish";
    private static Preferences mInstance;
    private final SharedPreferences mPreferences;

    private Preferences(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(Context context) {
        if (null == mInstance) mInstance = new Preferences(context);
        return mInstance;
    }

    public void setKitchenTypeId(long kitchenTypeId){
        mPreferences.edit().putLong(KEY_KITCHEN, kitchenTypeId).apply();
    }

    public long getKitchenTypeId(){
        return mPreferences.getLong(KEY_KITCHEN, 0);
    }

    public void setDishTypeId(long dishTypeId){
        mPreferences.edit().putLong(KEY_DISH, dishTypeId).apply();
    }

    public long getDishTypeId(){
        return mPreferences.getLong(KEY_DISH, 0);
    }
}
