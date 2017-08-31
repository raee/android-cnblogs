package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.rae.cnblogs.fragment.SearchFragment;

/**
 * 搜索界面
 * Created by ChenRui on 2017/8/29 0029 22:59.
 */
public class SearchActivity extends BaseFragmentActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(com.rae.cnblogs.R.anim.slide_in_bottom, 0);
        super.onCreate(savedInstanceState);
        mToolBar.setVisibility(View.GONE);
    }

    @Override
    protected Fragment newFragment() {
        return SearchFragment.newInstance();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, com.rae.cnblogs.R.anim.slide_out_bottom);
    }
}
