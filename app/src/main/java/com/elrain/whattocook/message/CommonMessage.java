package com.elrain.whattocook.message;

/**
 * Created by elrain on 08.06.15.
 */
public class CommonMessage {
    public final MessageEvent mMessageEvent;
    public Object messageObject;

    public CommonMessage(MessageEvent mMessageEvent) {
        this.mMessageEvent = mMessageEvent;
    }

    public CommonMessage(MessageEvent mMessageEvent, Object messageObject) {
        this.mMessageEvent = mMessageEvent;
        this.messageObject = messageObject;
    }

    public enum MessageEvent{
        DATA_LOAD_FINISHED,
        DATA_LOAD_FAIL,
        INGRIDIENT_ADDED,
        FILTER_CHANGED,
        LOGIN_ACCEPTED,
        API_ERROR,
        USER_REGISTERED,
        COMMENT_SENT,
        NEW_RESIPES_ADDED,

        ERROR_USER_EXISTS,
        ERROR_COMMENT_SNET
    }
}
