package com.elrain.whattocook.util;

import android.content.Context;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * Created by elrain on 14.07.15.
 */
public class OnLongPress {
    private EditText mEt;
    private Context mContext;
    public OnLongPress(Context context, EditText et){
        mEt = et;
        mContext = context;
    }
    private final GestureDetector gestureDetector = new GestureDetector(new GestureDetector.SimpleOnGestureListener() {
        public void onLongPress(MotionEvent e) {
            mEt.setInputType(InputType.TYPE_CLASS_TEXT);
        }

    });

    public View.OnTouchListener getListener(){
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_POINTER_DOWN:
                        mEt.requestFocus();
                        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(mEt, InputMethodManager.SHOW_IMPLICIT);
                        return gestureDetector.onTouchEvent(event);
                    case MotionEvent.ACTION_MOVE:
                        return gestureDetector.onTouchEvent(event);
                    case MotionEvent.ACTION_UP:
                        mEt.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        mEt.setSelection(mEt.getText().length());
                        return gestureDetector.onTouchEvent(event);
                }
                return false;
            }
        };
    }
}
