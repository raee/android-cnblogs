package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.R;

/**
 * 我的收藏
 * Created by ChenRui on 2017/7/14 0014 14:58.
 */
public class FavoritesActivity extends SwipeBackBaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        showHomeAsUp();
    }
}
