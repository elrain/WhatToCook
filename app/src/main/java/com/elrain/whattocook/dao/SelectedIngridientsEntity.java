package com.elrain.whattocook.dao;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class SelectedIngridientsEntity {

    private long id;
    private long idIngridient;
    private long idAmountType;
    private int quantity;

    private SelectedIngridientsEntity(long id, long idIngridient, long idAmountType, int quantity) {
        this.id = id;
        this.idIngridient = idIngridient;
        this.idAmountType = idAmountType;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public long getIdIngridient() {
        return idIngridient;
    }

    public long getIdAmountType() {
        return idAmountType;
    }

    public int getQuantity() {
        return quantity;
    }
}
