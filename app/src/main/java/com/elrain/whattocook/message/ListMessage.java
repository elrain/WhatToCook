package com.elrain.whattocook.message;

import java.util.List;

/**
 * Created by elrain on 24.06.15.
 */
public class ListMessage<T> {
    public final List<T> list;

    public ListMessage(List<T> list) {
        this.list = list;
    }
}
