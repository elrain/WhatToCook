package com.elrain.whattocook.webutil.rest.api;

import android.content.Context;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.AmountHelper;
import com.elrain.whattocook.dal.helper.AmountInRecipeHelper;
import com.elrain.whattocook.dal.helper.AmountTypeHelper;
import com.elrain.whattocook.dal.helper.AvialAmountTypeHelper;
import com.elrain.whattocook.dal.helper.DishTypeHelper;
import com.elrain.whattocook.dal.helper.GroupHelper;
import com.elrain.whattocook.dal.helper.IngridientsHelper;
import com.elrain.whattocook.dal.helper.KitchenTypeHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.message.CommentsMessage;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.webutil.rest.RestApi;
import com.elrain.whattocook.webutil.rest.RestHelper;
import com.elrain.whattocook.webutil.rest.body.CommentBody;
import com.elrain.whattocook.webutil.rest.body.UserBody;
import com.elrain.whattocook.webutil.rest.response.CommentsResponse;
import com.elrain.whattocook.webutil.rest.response.InitDataResponse;
import com.elrain.whattocook.webutil.rest.response.UserInfoResponse;

import java.util.List;

import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by elrain on 05.06.15.
 */
public class ApiWorker {
    private static ApiWorker mInstance;
    private static RestApi mApi;
    private static Context mContext;
    private static DbHelper mDbHelper;

    private ApiWorker(Context context) {
        mApi = RestHelper.getRestAdapter().create(RestApi.class);
        mContext = context;
        mDbHelper = DbHelper.getInstance(context);
    }

    public static ApiWorker getInstance(Context context) {
        if (null == mInstance) {
            mInstance = new ApiWorker(context);
        }
        return mInstance;
    }


    private Errors getError(RetrofitError error) {
        if (null != error.getResponse())
            switch (error.getResponse().getStatus()) {
                case Constants.HTTP_UNAUTHORIZED:
                    return Errors.HTTP_401;
            }
        else {
            if (error.getMessage().contains(Constants.FAILED_TO_CONNECT_TO))
                return Errors.FAILED_TO_CONNECT_TO;
            else if (error.getMessage().contains(Constants.ETIMEDOUT))
                return Errors.ETIMEDOUT;
            else if (error.getMessage().contains(Constants.ECONNREFUSED))
                return Errors.ECONNREFUSED;
            else if (error.getMessage().contains(Constants.EHOSTUNREACH))
                return Errors.EHOSTUNREACH;
            else if (error.getMessage().contains(Constants.ENETUNREACH))
                return Errors.ENETUNREACH;
            else if (error.getMessage().contains(Constants.NO_AUTHENTICATION_CHALLENGES_FOUND))
                return Errors.NO_AUTHENTICATION_CHALLENGES_FOUND;
        }
        return null;
    }

    public void initData() {
        mApi.initData(new Callback<InitDataResponse>() {
            @Override
            public void success(InitDataResponse initDataResponse, Response response) {
                if (null != initDataResponse)
                    startInit(initDataResponse);
                else
                    EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.DATA_LOAD_FAIL));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void startInit(InitDataResponse initDataResponse) {
        KitchenTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getKitchenTypes());
        DishTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getDishTypes());
        GroupHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getGroups());
        AmountTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmountTypes());
        IngridientsHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getIngridients());
        AvialAmountTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmountTypesRules());
        AmountHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmounts());
        RecipeHelper.add(mDbHelper.getWritableDatabase(), mContext, initDataResponse.getRecipes());
        AmountInRecipeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmountsInRecipes());

        EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.DATA_LOAD_FINISHED));
    }

    public void login(UserBody user) {
        mApi.login(user, new Callback<UserInfoResponse>() {
            @Override
            public void success(UserInfoResponse userInfoResponse, Response response) {
                EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.LOGIN_ACCEPTED, userInfoResponse));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.API_ERROR, getError(error)));
            }
        });
    }

    public void registerUser(UserBody user) {
        mApi.registerUser(user, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (response.getStatus() == 200)
                    EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.USER_REGISTERED));
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getMessage().contains("400")) {
                    EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.ERROR_USER_EXISTS));
                }
            }
        });
    }

    public void getComments(long recipeId) {
        mApi.getCommentsForRecipe(recipeId, new Callback<List<CommentsResponse>>() {
            @Override
            public void success(List<CommentsResponse> commentsResponses, Response response) {
                EventBus.getDefault().post(new CommentsMessage(commentsResponses));
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    public void sendComment(CommentBody comment) {
        mApi.addComment(comment, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (response.getStatus() == 200)
                    EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.COMMENT_SENT));
            }

            @Override
            public void failure(RetrofitError error) {
                EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.ERROR_COMMENT_SNET, error));
            }
        });
    }
}
