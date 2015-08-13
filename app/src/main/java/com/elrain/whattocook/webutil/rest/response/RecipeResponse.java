package com.elrain.whattocook.webutil.rest.response;

import java.util.List;

/**
 * Created by elrain on 24.07.15.
 */
public class RecipeResponse {

    private int idRecipe;
    private String name;
    private String description;
    private int cookTime;
    private KitchenTypeResponse kitchen;
    private DishTypeResponse dishType;
    private String image;
    private List<AmountResponse> amounts;

    public RecipeResponse() {
    }

    public int getIdRecipe() {
        return idRecipe;
    }

    public void setIdRecipe(int idRecipe) {
        this.idRecipe = idRecipe;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public KitchenTypeResponse getKitchen() {
        return kitchen;
    }

    public void setKitchen(KitchenTypeResponse kitchen) {
        this.kitchen = kitchen;
    }

    public DishTypeResponse getDishType() {
        return dishType;
    }

    public void setDishType(DishTypeResponse dishType) {
        this.dishType = dishType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<AmountResponse> getAmounts() {
        return amounts;
    }

    public void setAmounts(List<AmountResponse> amounts) {
        this.amounts = amounts;
    }
}
