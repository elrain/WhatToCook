package com.elrain.whattocook.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by elrain on 08.06.15.
 */
public class ImageUtil {
    public static final String FILE = "mainImage.jpg";
    public static final String PATH_DIVIDER = "/";
    public static final String RECIPE_FOLDER_NAME = "rec";

    public static String saveImage(long recipeId, String image, String internalStorage) {
        Bitmap bitmap = decodeBase64(image);
        File file = new File(internalStorage + PATH_DIVIDER + RECIPE_FOLDER_NAME + recipeId);
        if (!file.exists()) file.mkdirs();
        String fullFilePath = internalStorage + PATH_DIVIDER + RECIPE_FOLDER_NAME + recipeId + PATH_DIVIDER + FILE;

        try {
            FileOutputStream stream = new FileOutputStream(fullFilePath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] bitmapByteArray = baos.toByteArray();
            stream.write(bitmapByteArray);
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullFilePath;
    }

    public static Bitmap decodeBase64(String base64) {
        byte[] decodedArray = Base64.decode(base64, 0);
        return BitmapFactory.decodeByteArray(decodedArray, 0, decodedArray.length);
    }

    public static Drawable getDrawableFromPath(String filePath){
        return Drawable.createFromPath(filePath);
    }
}
