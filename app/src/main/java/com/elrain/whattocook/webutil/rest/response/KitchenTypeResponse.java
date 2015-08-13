package com.elrain.whattocook.webutil.rest.response;

/**
 * Created by elrain on 21.07.15.
 */
public class KitchenTypeResponse implements java.io.Serializable {
    private Integer idKitchenType;
    private String name;

    public Integer getIdKitchenType() {
        return idKitchenType;
    }

    public void setIdKitchenType(Integer idKitchenType) {
        this.idKitchenType = idKitchenType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
