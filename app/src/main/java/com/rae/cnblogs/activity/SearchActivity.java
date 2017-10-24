package com.rae.cnblogs.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.R;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.fragment.SearchFragment;

/**
 * 搜索界面
 * Created by ChenRui on 2017/8/29 0029 22:59.
 */
public class SearchActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(com.rae.cnblogs.R.anim.slide_in_bottom, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        mToolBar.setVisibility(View.GONE);
        ((View) mToolBar.getParent()).setBackgroundColor(Color.TRANSPARENT);

        findViewById(android.R.id.content).setFitsSystemWindows(true);
        AppMobclickAgent.onClickEvent(getContext(), "Search");
    }

    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected Fragment newFragment() {
        return SearchFragment.newInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, ThemeCompat.isNight() ? R.color.nightColorPrimary : R.color.white));
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, com.rae.cnblogs.R.anim.slide_out_bottom);
    }
}
