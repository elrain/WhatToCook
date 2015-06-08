package com.elrain.whattocook.webutil.rest.response;

import com.elrain.whattocook.dao.AmountEntity;
import com.elrain.whattocook.dao.IngridientsEntity;
import com.elrain.whattocook.dao.ManyToManyEntity;
import com.elrain.whattocook.dao.NamedEntity;
import com.elrain.whattocook.dao.RecipeEntity;

import java.util.List;

/**
 * Created by elrain on 05.06.15.
 */
public class InitDataResponse {

    private List<NamedEntity> groups;
    private List<NamedEntity> kitchenTypes;
    private List<NamedEntity> dishTypes;
    private List<NamedEntity> amountTypes;
    private List<IngridientsEntity> ingridients;
    private List<ManyToManyEntity> amountTypesRules;
    private List<ManyToManyEntity> amountsInRecipes;
    private List<AmountEntity> amounts;
    private List<RecipeEntity> recipes;

    public List<NamedEntity> getGroups() {
        return groups;
    }

    public List<NamedEntity> getKitchenTypes() {
        return kitchenTypes;
    }

    public List<NamedEntity> getDishTypes() {
        return dishTypes;
    }

    public List<NamedEntity> getAmountTypes() {
        return amountTypes;
    }

    public List<IngridientsEntity> getIngridients() {
        return ingridients;
    }

    public List<ManyToManyEntity> getAmountTypesRules() {
        return amountTypesRules;
    }

    public List<ManyToManyEntity> getAmountsInRecipes() {
        return amountsInRecipes;
    }

    public List<AmountEntity> getAmounts() {
        return amounts;
    }

    public List<RecipeEntity> getRecipes() {
        return recipes;
    }
}
