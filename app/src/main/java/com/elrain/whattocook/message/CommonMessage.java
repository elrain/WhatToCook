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
        DATA_LOAD_FINISHED, INGRIDIENT_ADDED
    }
}
