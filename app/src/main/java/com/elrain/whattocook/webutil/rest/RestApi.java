package com.elrain.whattocook.webutil.rest;

import com.elrain.whattocook.webutil.rest.body.CommentBody;
import com.elrain.whattocook.webutil.rest.body.UserBody;
import com.elrain.whattocook.webutil.rest.response.CommentsResponse;
import com.elrain.whattocook.webutil.rest.response.InitDataResponse;
import com.elrain.whattocook.webutil.rest.response.UserInfoResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

/**
 * Created by elrain on 05.06.15.
 */
public interface RestApi {
    @GET("/init")
    void initData(Callback<InitDataResponse> callback);

    @POST("/user/login")
    void login(@Body UserBody user, Callback<UserInfoResponse> callback);

    @PUT("/user/register")
    void registerUser(@Body UserBody user, Callback<Response> callback);

    @GET("/comments/{id}")
    void getCommentsForRecipe(@Path("id") long id, Callback<List<CommentsResponse>> callback);

    @PUT("/comments/new")
    void addComment(@Body CommentBody comment, Callback<Response> callback);
}
