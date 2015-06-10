package com.elrain.whattocook.dao;

/**
 * Created by Denys.Husher on 09.06.2015.
 */
public class SelectedIngridientsEntity {

    private long id;
    private long idAmountType;
    private int quantity;
    private IngridientsEntity ingridientsEntity;

    public SelectedIngridientsEntity(long id, long idAmountType, int quantity, IngridientsEntity ingridientsEntity) {
        this.id = id;
        this.idAmountType = idAmountType;
        this.quantity = quantity;
        this.ingridientsEntity = ingridientsEntity;
    }

    public long getId() {
        return id;
    }

    public IngridientsEntity getIngridientsEntity() {
        return ingridientsEntity;
    }

    public long getIdAmountType() {
        return idAmountType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
