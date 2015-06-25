package com.elrain.whattocook.webutil.rest.body;

/**
 * Created by elrain on 24.06.15.
 */
public class CommentBody {
    private String text;
    private long userId;
    private long recipeId;

    public CommentBody(String text, long userId, long recipeId) {
        this.text = text;
        this.userId = userId;
        this.recipeId = recipeId;
    }

    public CommentBody() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
