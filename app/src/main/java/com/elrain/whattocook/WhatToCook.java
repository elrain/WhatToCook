package com.elrain.whattocook;

import android.app.Application;

import java.io.File;

/**
 * Created by elrain on 08.06.15.
 */
public class WhatToCook extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        String filePath = getExternalFilesDir(null) != null ? getExternalFilesDir(null).getAbsolutePath() : getFilesDir().getAbsolutePath();
        File file = new File(filePath);
        if(!file.exists())
            file.mkdirs();

    }
}
