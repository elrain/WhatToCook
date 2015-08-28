package com.elrain.whattocook.webutil.rest.api;

import android.content.Context;
import android.os.Environment;

import com.elrain.whattocook.dal.DbHelper;
import com.elrain.whattocook.dal.helper.AmountHelper;
import com.elrain.whattocook.dal.helper.AmountInRecipeHelper;
import com.elrain.whattocook.dal.helper.AmountTypeHelper;
import com.elrain.whattocook.dal.helper.AvailAmountTypeHelper;
import com.elrain.whattocook.dal.helper.DishTypeHelper;
import com.elrain.whattocook.dal.helper.GroupHelper;
import com.elrain.whattocook.dal.helper.IngridientsHelper;
import com.elrain.whattocook.dal.helper.KitchenTypeHelper;
import com.elrain.whattocook.dal.helper.RecipeHelper;
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.message.ListMessage;
import com.elrain.whattocook.webutil.rest.RestApi;
import com.elrain.whattocook.webutil.rest.RestHelper;
import com.elrain.whattocook.webutil.rest.body.CommentBody;
import com.elrain.whattocook.webutil.rest.body.UserBody;
import com.elrain.whattocook.webutil.rest.response.AmountResponse;
import com.elrain.whattocook.webutil.rest.response.AmountTypeResponse;
import com.elrain.whattocook.webutil.rest.response.CommentsResponse;
import com.elrain.whattocook.webutil.rest.response.GroupsResponse;
import com.elrain.whattocook.webutil.rest.response.IngridientsResponse;
import com.elrain.whattocook.webutil.rest.response.RecipeResponse;
import com.elrain.whattocook.webutil.rest.response.UserInfoResponse;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        mApi.initData(new Callback<List<RecipeResponse>>() {
            @Override
            public void success(List<RecipeResponse> recipesResponse, Response response) {
                if (null != recipesResponse)
                    startInit(recipesResponse);
                else
                    EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.DATA_LOAD_FAIL));
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });

    }

    private void startInit(List<RecipeResponse> recipesResponse) {
        for (RecipeResponse rr : recipesResponse) {
            DishTypeHelper.add(mDbHelper.getWritableDatabase(), rr.getDishType());
            KitchenTypeHelper.add(mDbHelper.getWritableDatabase(), rr.getKitchen());
            RecipeHelper.add(mDbHelper.getWritableDatabase(), mContext, rr);
            for (AmountResponse ar : rr.getAmounts()) {
                IngridientsResponse ir = ar.getIngridient();
                GroupsResponse gr = ir.getGroup();
                GroupHelper.add(mDbHelper.getWritableDatabase(), gr);
                IngridientsHelper.add(mDbHelper.getWritableDatabase(), ir);
                AmountTypeResponse atr = ar.getAmountType();
                AmountTypeHelper.add(mDbHelper.getWritableDatabase(), atr);
                AvailAmountTypeHelper.add(mDbHelper.getWritableDatabase(), gr.getIdGroup(), atr.getIdAmountType());
                AmountHelper.add(mDbHelper.getWritableDatabase(), ar);
                AmountInRecipeHelper.add(mDbHelper.getWritableDatabase(), ar.getIdAmount(), rr.getIdRecipe());
            }
        }
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
                EventBus.getDefault().post(new ListMessage(commentsResponses));
            }

            @Override
            public void failure(RetrofitError error) {
                int i = 0;
                ++i;
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

    public void getRecipesByIngridients(String[] names) {
        mApi.getRecipesByIngridients(names, new Callback<List<RecipeResponse>>() {
            @Override
            public void success(List<RecipeResponse> recipeResponse, Response response) {
                if (null != recipeResponse)
                    startInit(recipeResponse);
                EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.NEW_RESIPES_ADDED));
            }

            @Override
            public void failure(RetrofitError error) {
                error.toString();
            }
        });
    }

    public void downloadPdf(final long recipeId, final String fileName) {
        mApi.downloadPdf(recipeId, new Callback<Response>() {
            @Override
            public void success(Response response, Response response2) {
                if (response.getStatus() == 200) {
                    InputStream is = null;
                    OutputStream os = null;
                    try {
                        is = response.getBody().in();
                        os = new FileOutputStream(new File(String.format(Constants.PDF_LOCATION, recipeId)));
                        int read;
                        byte[] bytes = new byte[1024];

                        while ((read = is.read(bytes)) != -1)
                            os.write(bytes, 0, read);
                        EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.PDF_DOWNLOADED, fileName));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        if (null != is)
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        if (null != os)
                            try {
                                os.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                error.toString();
            }
        });
    }
}
