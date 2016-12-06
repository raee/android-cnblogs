package com.rae.cnblogs.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 文本
 * Created by ChenRui on 2016/12/1 23:00.
 */
public class RaeTextView extends TextView {
    public RaeTextView(Context context) {
        super(context);
        init();
    }

    public RaeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public RaeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public RaeTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();

    }

    private void init() {
//        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),"fonts/cnblogs.ttf");
//        setTypeface(tf);
    }
}
