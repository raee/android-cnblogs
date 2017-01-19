package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by ChenRui on 2017/1/19 0019 14:35.
 */
public class RaeEditText extends EditText {
    public RaeEditText(Context context) {
        super(context);
    }

    public RaeEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RaeEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RaeEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
}
