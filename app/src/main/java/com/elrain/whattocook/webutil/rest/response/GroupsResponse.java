package com.elrain.whattocook.webutil.rest.response;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by elrain on 21.07.15.
 */
public class GroupsResponse implements java.io.Serializable {
    private int idGroup;
    private String name;
    private List<AmountTypeResponse> amountTypes;

    public int getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(int idGroup) {
        this.idGroup = idGroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<AmountTypeResponse> getAmountTypes() {
        return amountTypes;
    }

    public void setAmountTypes(List<AmountTypeResponse> amountTypes) {
        this.amountTypes = amountTypes;
    }
}
