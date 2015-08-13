package com.elrain.whattocook.webutil.rest.response;

/**
 * Created by elrain on 23.07.15.
 */
public class AmountResponse {
    private int idAmount;
    private int count;
    private IngridientsResponse ingridient;
    private AmountTypeResponse amountType;

    public int getIdAmount() {
        return idAmount;
    }

    public void setIdAmount(int idAmount) {
        this.idAmount = idAmount;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public IngridientsResponse getIngridient() {
        return ingridient;
    }

    public void setIngridient(IngridientsResponse ingridient) {
        this.ingridient = ingridient;
    }

    public AmountTypeResponse getAmountType() {
        return amountType;
    }

    public void setAmountType(AmountTypeResponse amountType) {
        this.amountType = amountType;
    }
}
