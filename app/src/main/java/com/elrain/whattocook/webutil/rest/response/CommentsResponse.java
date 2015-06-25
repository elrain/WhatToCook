package com.elrain.whattocook.webutil.rest.response;

/**
 * Created by elrain on 24.06.15.
 */
public class CommentsResponse {

    private long idComment;
    private String text;
    private String time;
    private String userName;

    public CommentsResponse(long idComment, String text, String time, String userName) {
        this.idComment = idComment;
        this.text = text;
        this.time = time;
        this.userName = userName;
    }

    public long getIdComment() {
        return idComment;
    }

    public void setIdComment(long idComment) {
        this.idComment = idComment;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
