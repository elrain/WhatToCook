package com.elrain.whattocook.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by elrain on 24.06.15.
 */
public class TimeUtil {
    public static final String FULL_DATE_TIME = "yyyy-MM-dd HH:mm:ss";

    public static String getFullTime(long date) {
        SimpleDateFormat format = new SimpleDateFormat(FULL_DATE_TIME, Locale.US);
        Date localDate = new Date(date);
        return format.format(localDate);
    }
}
