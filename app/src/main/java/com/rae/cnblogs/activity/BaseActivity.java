package com.rae.cnblogs.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.LayoutRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * 基类
 * Created by ChenRui on 2016/12/1 21:35.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected View mBackView;

    private void bindView() {
        ButterKnife.bind(this);
        mBackView = findViewById(R.id.back);
        if (mBackView != null) {
            mBackView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackClick(v);
                }
            });
        }
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

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        bindView();
    }

    protected void showHomeAsUp(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setDisplayHomeAsUpEnabled(true);
            bar.setHomeAsUpIndicator(getHomeAsUpIndicator());
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

    /**
     * 返回按钮的图片
     */
    protected int getHomeAsUpIndicator() {
        return R.drawable.ic_back;
    }

    protected BaseActivity getContext() {
        return this;
    }

    public void onBackClick(View view) {
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 721 && resultCode == RESULT_OK) {
            onLoginCallBack();
        }
    }

    /**
     * 跳登录的时候回调
     */
    protected void onLoginCallBack() {
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        onCancelHttpRequest();
        ImageLoader.getInstance().getMemoryCache().clear();
    }

    protected void onCancelHttpRequest() {
        // 取消HTTP请求
        CnblogsApiFactory.getInstance(this).cancel();
    }


    public int getVersionCode() {
        try {
            return getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_META_DATA).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }
}
