package com.rae.cnblogs.activity;

import android.support.v7.app.AppCompatActivity;

import butterknife.ButterKnife;

/**
 * 基类
 * Created by ChenRui on 2016/12/1 21:35.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected void bindView() {
        ButterKnife.bind(this);
    }
}
