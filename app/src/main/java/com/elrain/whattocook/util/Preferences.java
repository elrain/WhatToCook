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
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_USER_TYPE = "userType";
    private static final String KEY_USER_ID = "userId";
    private static final String ID_WASN_T_SET = "Id wasn't set";
    private static final String USER_NAME_WASN_T_SET = "User name wasn't set";
    private static Preferences mInstance;
    private final SharedPreferences mPreferences;

    private Preferences(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(Context context) {
        if (null == mInstance) mInstance = new Preferences(context);
        return mInstance;
    }

    public long getKitchenTypeId() {
        return mPreferences.getLong(KEY_KITCHEN, 0);
    }

    public void setKitchenTypeId(long kitchenTypeId) {
        mPreferences.edit().putLong(KEY_KITCHEN, kitchenTypeId).apply();
    }

    public long getDishTypeId() {
        return mPreferences.getLong(KEY_DISH, 0);
    }

    public void setDishTypeId(long dishTypeId) {
        mPreferences.edit().putLong(KEY_DISH, dishTypeId).apply();
    }

    public String getUserName() {
        String name = mPreferences.getString(KEY_USER_NAME, null);
        if (null != name) return mPreferences.getString(KEY_USER_NAME, null);
        else throw new IllegalArgumentException(USER_NAME_WASN_T_SET);
    }

    public void setUserName(String name) {
        mPreferences.edit().putString(KEY_USER_NAME, name).apply();
    }

    public long getUserType() {
        return mPreferences.getLong(KEY_USER_TYPE, 2);
    }

    public void setUserType(long type) {
        mPreferences.edit().putLong(KEY_USER_TYPE, type).apply();
    }

    public long getUserId() {
        long id = mPreferences.getLong(KEY_USER_ID, 0);
        if (id != 0) return id;
        else throw new IllegalArgumentException(ID_WASN_T_SET);
    }

    public void setUserId(long userId) {
        mPreferences.edit().putLong(KEY_USER_ID, userId).apply();
    }
}
