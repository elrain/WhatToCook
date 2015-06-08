package com.elrain.whattocook.message;

/**
 * Created by elrain on 08.06.15.
 */
public class CommonMessage {
    public MessageEvent mMessageEvent;

    public CommonMessage(MessageEvent mMessageEvent) {
        this.mMessageEvent = mMessageEvent;
    }

    public enum MessageEvent{
        DATA_LOAD_FINISHED
    }
}
