package com.elrain.whattocook.webutil.rest.response;

/**
 * Created by elrain on 21.07.15.
 */
public class AmountTypeResponse implements java.io.Serializable {
    private int idAmountType;
    private String name;

    public int getIdAmountType() {
        return idAmountType;
    }

    public void setIdAmountType(int idAmountType) {
        this.idAmountType = idAmountType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
