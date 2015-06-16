package com.elrain.whattocook.webutil.rest;

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
import com.elrain.whattocook.message.CommonMessage;
import com.elrain.whattocook.webutil.rest.response.InitDataResponse;

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

    public void initData() {
        mApi.initData(new Callback<InitDataResponse>() {
            @Override
            public void success(InitDataResponse initDataResponse, Response response) {
                startInit(initDataResponse);
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });

    }

    private void startInit(InitDataResponse initDataResponse) {
        insertKitchens(initDataResponse);
        insertDishTypes(initDataResponse);
        insertGroups(initDataResponse);
        insertAmountTypes(initDataResponse);
        insertIngridients(initDataResponse);
        insertAmountTypeRules(initDataResponse);
        insertAmounts(initDataResponse);
        insertRecipes(initDataResponse);
        insertAmountsInRecipe(initDataResponse);

        EventBus.getDefault().post(new CommonMessage(CommonMessage.MessageEvent.DATA_LOAD_FINISHED));
    }

    private void insertAmountsInRecipe(InitDataResponse initDataResponse) {
        AmountInRecipeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmountsInRecipes());
    }

    private void insertRecipes(InitDataResponse initDataResponse) {
        RecipeHelper.add(mDbHelper.getWritableDatabase(), mContext, initDataResponse.getRecipes());
    }

    private void insertAmounts(InitDataResponse initDataResponse) {
        AmountHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmounts());
    }

    private void insertAmountTypeRules(InitDataResponse initDataResponse) {
        AvialAmountTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmountTypesRules());
    }

    private void insertIngridients(InitDataResponse initDataResponse) {
        IngridientsHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getIngridients());
    }

    private void insertAmountTypes(InitDataResponse initDataResponse) {
        AmountTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getAmountTypes());
    }

    private void insertGroups(InitDataResponse initDataResponse) {
        GroupHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getGroups());
    }

    private void insertDishTypes(InitDataResponse initDataResponse) {
        DishTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getDishTypes());
    }

    private void insertKitchens(InitDataResponse initDataResponse) {
        KitchenTypeHelper.add(mDbHelper.getWritableDatabase(), initDataResponse.getKitchenTypes());
    }

}
