package com.elrain.whattocook.webutil.rest.response;

/**
 * Created by elrain on 21.07.15.
 */
public class IngridientsResponse implements java.io.Serializable {
    private int idIngridient;
    private String name;
    private GroupsResponse group;

    public int getIdIngridient() {
        return idIngridient;
    }

    public void setIdIngridient(int idIngridients) {
        this.idIngridient = idIngridients;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GroupsResponse getGroup() {
        return group;
    }

    public void setGroup(GroupsResponse group) {
        this.group = group;
    }
}
