package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.rae.cnblogs.R;

/**
 * 单个Fragment页面
 * Created by ChenRui on 2017/8/29 0029 22:59.
 */
public abstract class BaseFragmentActivity extends SwipeBackBaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        showHomeAsUp();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content, newFragment())
                .commitNowAllowingStateLoss();

    }

    protected abstract Fragment newFragment();

    public int getLayoutId() {
        return R.layout.activity_single_fragment;
    }
}
