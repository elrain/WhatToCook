package com.elrain.whattocook.dao;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class NamedEntity {
    private long id;
    private String name;

    public NamedEntity(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public NamedEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
