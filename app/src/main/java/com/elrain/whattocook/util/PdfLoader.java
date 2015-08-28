package com.elrain.whattocook.util;

import android.content.Context;

import com.elrain.whattocook.webutil.rest.api.ApiWorker;

/**
 * Created by elrain on 26.08.15.
 */
public final class PdfLoader {
    public static void downloadPdf(Context context, long recipeId, String fileName) {
        ApiWorker api = ApiWorker.getInstance(context);
        api.downloadPdf(recipeId, fileName);
    }
}
