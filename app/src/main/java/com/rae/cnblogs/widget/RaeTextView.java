package com.rae.cnblogs.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 文本
 * Created by ChenRui on 2016/12/1 23:00.
 */
public class RaeTextView extends AppCompatTextView {
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

    private void init() {
//        Typeface tf = Typeface.createFromAsset(getResources().getAssets(),"fonts/cnblogs.ttf");
//        setTypeface(tf);
    }
}
