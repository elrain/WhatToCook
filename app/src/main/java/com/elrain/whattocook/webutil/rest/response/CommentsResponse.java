package com.elrain.whattocook.webutil.rest.response;

/**
 * Created by elrain on 24.06.15.
 */
public class CommentsResponse {

    private long commentId;
    private String text;
    private long time;
    private String userName;

    public CommentsResponse(long commentId, String text, long time, String userName) {
        this.commentId = commentId;
        this.text = text;
        this.time = time;
        this.userName = userName;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
