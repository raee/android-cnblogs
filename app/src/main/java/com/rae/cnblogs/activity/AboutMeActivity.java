package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.R;

/**
 * 关于我们
 * Created by ChenRui on 2018/2/9 0009 17:21.
 */
public class AboutMeActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        showHomeAsUp();
    }
}
