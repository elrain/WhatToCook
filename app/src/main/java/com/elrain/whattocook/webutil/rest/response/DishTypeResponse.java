package com.elrain.whattocook.webutil.rest.response;

/**
 * Created by elrain on 21.07.15.
 */
public class DishTypeResponse implements java.io.Serializable {
    private long idDishType;
    private String name;

    public long getIdDishType() {
        return idDishType;
    }

    public void setIdDishType(long idDishType) {
        this.idDishType = idDishType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
