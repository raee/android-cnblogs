package com.rae.cnblogs.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.rae.cnblogs.R;

/**
 * 带loading状态的Image View
 * Created by ChenRui on 2017/1/26 0026 11:36.
 */
public class ImageLoadingView extends ImageView {

    private Drawable mSourceDrawable;
    private Drawable mLoadingDrawable;

    public ImageLoadingView(Context context) {
        super(context);
        init();
    }

    public ImageLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ImageLoadingView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        mLoadingDrawable = getResources().getDrawable(R.drawable.default_loading);
    }

    public void loading() {
        if (getDrawable() != mLoadingDrawable) {
            mSourceDrawable = getDrawable();
        }

        setImageDrawable(mLoadingDrawable);
    }

    public void dismiss() {
        if (mSourceDrawable != null) {
            setImageDrawable(mSourceDrawable);
        }
    }
}
