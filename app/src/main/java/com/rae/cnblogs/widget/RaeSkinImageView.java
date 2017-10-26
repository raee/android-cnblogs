package com.rae.cnblogs.widget;


import android.content.Context;
import android.util.AttributeSet;

import com.rae.cnblogs.ThemeCompat;

import skin.support.widget.SkinCompatImageView;

public class RaeSkinImageView extends SkinCompatImageView {
    public RaeSkinImageView(Context context) {
        super(context);
        init();
    }

    public RaeSkinImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RaeSkinImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化的时候不用取反
        setAlpha(ThemeCompat.isNight() ? 0.8f : 1f);
    }

    @Override
    public void applySkin() {
        super.applySkin();
        setAlpha(isNight() ? 0.8f : 1f);
    }

    public boolean isNight() {
        // 因为是先应用主题之后才会设置主题名称，所以这里取反。
        return !ThemeCompat.isNight();
    }
}
