package com.elrain.whattocook.message;

import android.os.Bundle;

/**
 * Created by elrain on 11.06.15.
 */
public class ChangeFragmentMessage {
    public final String mFragmentTag;
    public final Bundle mBundle;

    public ChangeFragmentMessage(String mFragmentTag, Bundle mBundle) {
        this.mFragmentTag = mFragmentTag;
        this.mBundle = mBundle;
    }
}
