package com.elrain.whattocook.dao;

import java.util.List;

/**
 * Created by elrain on 05.06.15.
 */
public class RecipeEntity {
    private long id;
    private String name;
    private String description;
    private int cookTime;
    private long idKitchenType;
    private long idDishType;
    private List<Ingridient> ingridients;

    public RecipeEntity(long id, String name, String description, int cookTime, long idKitchenType, long idDishType) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cookTime = cookTime;
        this.idKitchenType = idKitchenType;
        this.idDishType = idDishType;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCookTime() {
        return cookTime;
    }

    public long getIdKitchenType() {
        return idKitchenType;
    }

    public long getIdDishType() {
        return idDishType;
    }

    public List<Ingridient> getIngridients() {
        return ingridients;
    }

    public void setIngridients(List<Ingridient> ingridients) {
        this.ingridients = ingridients;
    }
}
