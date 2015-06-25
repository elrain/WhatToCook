package com.elrain.whattocook.message;

import com.elrain.whattocook.webutil.rest.response.CommentsResponse;

import java.util.List;

/**
 * Created by elrain on 24.06.15.
 */
public class CommentsMessage {
    public final List<CommentsResponse> comments;

    public CommentsMessage(List<CommentsResponse> comments) {
        this.comments = comments;
    }
}
