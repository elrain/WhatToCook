package com.elrain.whattocook.dao;

import java.util.Date;
import java.util.List;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class Recipe {
    private long id;
    private String name;
    private String description;
    private Date cookTime;
    private String dishTypeName;
    private String kitchenTypeName;
    private List<Ingridient> ingridients;

    public Recipe(long id, String name, String description, Date cookTime, String dishTypeName, String kitchenTypeName, List<Ingridient> ingridients) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.cookTime = cookTime;
        this.dishTypeName = dishTypeName;
        this.kitchenTypeName = kitchenTypeName;
        this.ingridients = ingridients;
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

    public Date getCookTime() {
        return cookTime;
    }

    public String getDishTypeName() {
        return dishTypeName;
    }

    public String getKitchenTypeName() {
        return kitchenTypeName;
    }

    public List<Ingridient> getIngridients() {
        return ingridients;
    }
}
