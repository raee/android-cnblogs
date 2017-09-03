package com.rae.cnblogs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

import com.rae.cnblogs.R;

import skin.support.content.res.SkinCompatResources;
import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatHelper;
import skin.support.widget.SkinCompatSupportable;

import static skin.support.widget.SkinCompatHelper.INVALID_ID;


public class RaeTabLayout extends TabLayout implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;

    private int mIndicatorColorResId = INVALID_ID;
    private int mTextSelectColorResId = INVALID_ID;
    private int mTextUnselectColorResId = INVALID_ID;

    public RaeTabLayout(Context context) {
        super(context);
        initViews(null, 0);
    }

    public RaeTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(attrs, 0);
    }

    public RaeTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(attrs, defStyleAttr);
    }


    private void initViews(AttributeSet attrs, int defStyleAttr) {
        obtainAttributes(getContext(), attrs);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TabLayout);
        mIndicatorColorResId = ta.getResourceId(R.styleable.TabLayout_tabIndicatorColor, INVALID_ID);
        mIndicatorColorResId = SkinCompatHelper.checkResourceId(mIndicatorColorResId);


        mTextSelectColorResId = ta.getResourceId(R.styleable.TabLayout_tabSelectedTextColor, INVALID_ID);
        mTextSelectColorResId = SkinCompatHelper.checkResourceId(mTextSelectColorResId);

        mTextUnselectColorResId = ta.getResourceId(R.styleable.TabLayout_tabTextColor, INVALID_ID);
        mTextUnselectColorResId = SkinCompatHelper.checkResourceId(mTextUnselectColorResId);
        ta.recycle();
        applyTabLayoutResources();
    }

    private void applyTabLayoutResources() {
        SkinCompatResources resources = SkinCompatResources.getInstance();
        if (mIndicatorColorResId != INVALID_ID) {
            setSelectedTabIndicatorColor(resources.getColor(mIndicatorColorResId));
        }
        if (mTextSelectColorResId != INVALID_ID && mTextUnselectColorResId != INVALID_ID) {
            setTabTextColors(resources.getColor(mTextUnselectColorResId), resources.getColor(mTextSelectColorResId));
        }

        // 找到所有的TAB，TODO:TAB VIEW
        int tabCount = getTabCount();
        for (int i = 0; i < tabCount; i++) {
            Tab tab = getTabAt(i);
        }
    }

    @Override
    public void applySkin() {
        applyTabLayoutResources();
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }

    }
}
