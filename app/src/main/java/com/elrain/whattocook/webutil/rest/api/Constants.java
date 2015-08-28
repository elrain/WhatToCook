package com.elrain.whattocook.webutil.rest.api;

import android.os.Environment;

/**
 * Created by elrain on 22.06.15.
 */
public class Constants {

    public static final String NO_AUTHENTICATION_CHALLENGES_FOUND = "No authentication challenges found";
    public static final String ECONNREFUSED = "ECONNREFUSED";
    public static final String ETIMEDOUT = "ETIMEDOUT";
    public static final String EHOSTUNREACH = "EHOSTUNREACH";
    public static final String ENETUNREACH = "ENETUNREACH";
    public static final String FAILED_TO_CONNECT_TO = "failed to connect to";

    public static final int RESPONSE_STATUS_NONE = 204;
    public static final int HTTP_UNAUTHORIZED = 401;
    public static final int RESPONSE_UNAUTHORIZED_403 = 403;
    public static final int RESPONSE_NOT_FOUND = 404;
    public static final int RESPONSE_STATUS_ALREADY_SET = 409;
    public static final int RESPONSE_OK = 200;

    public static final String SERVER_URL = "http://172.20.29.73:8080/app";
    //    public static final String SERVER_URL = "http://172.20.29.73:8080/WhatToCook/app";
    public static final String IMAGE_URL = "http://172.20.29.73:8080/app/file/image/";

    public static final String PDF_LOCATION = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + "%s.pdf";
}
