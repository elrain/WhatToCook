package com.elrain.whattocook.webutil.rest.body;

/**
 * Created by elrain on 19.06.15.
 */
public class UserBody {
    private String name;
    private String password;

    public UserBody(String name, String password) {
        this.name = name;
        this.password = password;
    }
}
