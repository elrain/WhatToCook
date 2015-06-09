package com.elrain.whattocook.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class Preferences {

    private static final String PREFERENCES_NAME = "whatToCookpref";
    private static Preferences mInstance;
    private final SharedPreferences mPreferences;

    private Preferences(Context context) {
        mPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static Preferences getInstance(Context context) {
        if (null == mInstance) mInstance = new Preferences(context);
        return mInstance;
    }
}
