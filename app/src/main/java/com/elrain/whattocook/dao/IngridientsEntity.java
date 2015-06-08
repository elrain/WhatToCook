package com.elrain.whattocook.dao;

/**
 * Created by elrain on 05.06.15.
 */
public class IngridientsEntity {

    private long id;
    private String name;
    private long idGroup;

    public IngridientsEntity(long id, String name, long idGroup) {
        this.id = id;
        this.name = name;
        this.idGroup = idGroup;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public long getIdGroup() {
        return idGroup;
    }
}
