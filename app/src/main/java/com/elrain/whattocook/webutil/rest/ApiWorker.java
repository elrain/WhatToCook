package com.elrain.whattocook.webutil.rest;

import android.content.Context;

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

    private ApiWorker(Context context) {
        mApi = RestHelper.getRestAdapter().create(RestApi.class);
        mContext = context;
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
        AmountInRecipeHelper amountInRecipeHelper = new AmountInRecipeHelper(mContext);
        amountInRecipeHelper.add(initDataResponse.getAmountsInRecipes());
    }

    private void insertRecipes(InitDataResponse initDataResponse) {
        RecipeHelper recipeHelper = new RecipeHelper(mContext);
        recipeHelper.add(initDataResponse.getRecipes());
    }

    private void insertAmounts(InitDataResponse initDataResponse) {
        AmountHelper amountHelper = new AmountHelper(mContext);
        amountHelper.add(initDataResponse.getAmounts());
    }

    private void insertAmountTypeRules(InitDataResponse initDataResponse) {
        AvialAmountTypeHelper avialAmountTypeHelper = new AvialAmountTypeHelper(mContext);
        avialAmountTypeHelper.add(initDataResponse.getAmountTypesRules());
    }

    private void insertIngridients(InitDataResponse initDataResponse) {
        IngridientsHelper ingridientsHelper = new IngridientsHelper(mContext);
        ingridientsHelper.add(initDataResponse.getIngridients());
    }

    private void insertAmountTypes(InitDataResponse initDataResponse) {
        AmountTypeHelper amountTypeHelper = new AmountTypeHelper(mContext);
        amountTypeHelper.add(initDataResponse.getAmountTypes());
    }

    private void insertGroups(InitDataResponse initDataResponse) {
        GroupHelper groupHelper = new GroupHelper(mContext);
        groupHelper.add(initDataResponse.getGroups());
    }

    private void insertDishTypes(InitDataResponse initDataResponse) {
        DishTypeHelper dishTypeHelper = new DishTypeHelper(mContext);
        dishTypeHelper.add(initDataResponse.getDishTypes());
    }

    private void insertKitchens(InitDataResponse initDataResponse) {
        KitchenTypeHelper kitchenTypeHelper = new KitchenTypeHelper(mContext);
        kitchenTypeHelper.add(initDataResponse.getKitchenTypes());
    }

}
