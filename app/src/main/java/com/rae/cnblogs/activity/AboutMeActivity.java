package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;

/**
 * 关于我们
 * Created by ChenRui on 2018/2/9 0009 17:21.
 */
@Route(path = AppRoute.PATH_ABOUT_ME)
public class AboutMeActivity extends BasicActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        showHomeAsUp();
    }
}
