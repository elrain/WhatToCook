package com.elrain.whattocook.dao;

/**
 * Created by elrain on 05.06.15.
 */
public class AmountEntity {

    private long id;
    private int count;
    private long idIngridient;
    private long idAmountType;

    public AmountEntity(long id, int count, long idIngridient, long idAmountType) {
        this.id = id;
        this.count = count;
        this.idIngridient = idIngridient;
        this.idAmountType = idAmountType;
    }

    public long getId() {
        return id;
    }

    public int getCount() {
        return count;
    }

    public long getIdIngridient() {
        return idIngridient;
    }

    public long getIdAmountType() {
        return idAmountType;
    }
}
