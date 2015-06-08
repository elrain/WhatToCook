package com.elrain.whattocook.webutil.rest;

import com.elrain.whattocook.webutil.rest.response.InitDataResponse;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by elrain on 05.06.15.
 */
public interface RestApi {
    @GET("/init")
    void initData(Callback<InitDataResponse> callback);
}
