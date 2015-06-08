package com.elrain.whattocook.webutil.rest;

import retrofit.RestAdapter;

/**
 * Created by elrain on 05.06.15.
 */
public class RestHelper {
    public static RestAdapter getRestAdapter() {
        return new RestAdapter.Builder().setEndpoint("http://172.20.29.73:8080/WhatToCook/app").setLogLevel(RestAdapter.LogLevel.FULL).build();
    }
}
