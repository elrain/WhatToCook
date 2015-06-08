package com.elrain.whattocook.dao;

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
    private String image;

    public RecipeEntity(long id, String name, String description, int cookTime, long idKitchenType, long idDishType, String image) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cookTime = cookTime;
        this.idKitchenType = idKitchenType;
        this.idDishType = idDishType;
        this.image = image;
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

    public String getImage() {
        return image;
    }
}
