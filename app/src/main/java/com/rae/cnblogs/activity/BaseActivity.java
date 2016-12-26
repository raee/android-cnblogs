package com.rae.cnblogs.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.rae.cnblogs.R;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * 基类
 * Created by ChenRui on 2016/12/1 21:35.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected void bindView() {
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    protected void showHomeAsUp(Toolbar toolbar) {
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeAsUpIndicator(R.drawable.ic_back);
            bar.setDisplayShowHomeEnabled(false);
            bar.setDisplayShowTitleEnabled(false);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onBackPressed();
                }
            });
        }
    }

    protected int parseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return 0;
    }

    private BaseActivity getContext() {
        return this;
    }
}
