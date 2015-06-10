package com.elrain.whattocook.dao;

/**
 * Created by elrain on 10.06.15.
 */
public class RecipeIngridientsEntity {
    private String amountTypeName;
    private int quantity;
    private String name;

    public RecipeIngridientsEntity(int quantity, String name, String amountTypeName) {
        this.amountTypeName = amountTypeName;
        this.quantity = quantity;
        this.name = name;
    }

    public String getAmountTypeName() {
        return amountTypeName;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }
}
