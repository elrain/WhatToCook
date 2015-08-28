package com.elrain.whattocook.webutil.rest;


import com.elrain.whattocook.webutil.rest.api.Constants;

import retrofit.RestAdapter;

/**
 * Created by elrain on 05.06.15.
 */
public class RestHelper {
    public static RestAdapter getRestAdapter() {
//        return new RestAdapter.Builder().setEndpoint(Constants.SERVER_URL).setLogLevel(RestAdapter.LogLevel.FULL).build();
        return new RestAdapter.Builder().setEndpoint(Constants.SERVER_URL).setLogLevel(RestAdapter.LogLevel.FULL).build();
    }
}
