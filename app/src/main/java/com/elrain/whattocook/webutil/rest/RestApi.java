package com.elrain.whattocook.webutil.rest;

import com.elrain.whattocook.webutil.rest.body.CommentBody;
import com.elrain.whattocook.webutil.rest.body.UserBody;
import com.elrain.whattocook.webutil.rest.response.CommentsResponse;
import com.elrain.whattocook.webutil.rest.response.RecipeResponse;
import com.elrain.whattocook.webutil.rest.response.UserInfoResponse;

import java.util.List;
import java.util.Queue;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by elrain on 05.06.15.
 */
public interface RestApi {
    @GET("/init")
    void initData(Callback<List<RecipeResponse>> callback);

    @POST("/user/login")
    void login(@Body UserBody user, Callback<UserInfoResponse> callback);

    @PUT("/user/register")
    void registerUser(@Body UserBody user, Callback<Response> callback);

    @GET("/comments/{recipeId}")
    void getCommentsForRecipe(@Path("recipeId") long id, Callback<List<CommentsResponse>> callback);

    @PUT("/list/new")
    void addComment(@Body CommentBody comment, Callback<Response> callback);

    @GET("/recipe/search")
    void getRecipesByIngridients(@Query("ingridient")String[] names, Callback<List<RecipeResponse>> callback);

    @GET("/file/pdf/{recipeId}")
    void downloadPdf(@Path("recipeId") long recipeId, Callback<Response> callback);
}
