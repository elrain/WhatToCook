package com.elrain.whattocook.dao;

/**
 * Created by Denys.Husher on 03.06.2015.
 */
public class Ingridient {
    private int amount;
    private String ingridientName;
    private String amountTypeName;

    public Ingridient(int amount, String ingridientName, String amountTypeName) {
        this.amount = amount;
        this.ingridientName = ingridientName;
        this.amountTypeName = amountTypeName;
    }

    public int getAmount() {
        return amount;
    }

    public String getIngridientName() {
        return ingridientName;
    }

    public String getAmountTypeName() {
        return amountTypeName;
    }
}
