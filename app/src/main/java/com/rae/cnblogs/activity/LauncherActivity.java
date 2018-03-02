package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.ILauncherPresenter;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.widget.CountDownTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:08.
 */
public class LauncherActivity extends BaseActivity implements ILauncherPresenter.ILauncherView {

    @BindView(R.id.img_launcher_display)
    ImageView mDisplayView;

    @BindView(R.id.tv_launcher_name)
    TextView mNameView;

    @BindView(R.id.tv_skip)
    CountDownTextView mCountDownTextView;

    ILauncherPresenter mLauncherPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mLauncherPresenter = CnblogsPresenterFactory.getLauncherPresenter(this, this);
        Log.w("rae", "渠道为：" + getChannel());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLauncherPresenter.start();
        mCountDownTextView.start();
    }

    @Override
    protected void onStop() {
        mLauncherPresenter.cancel();
        mCountDownTextView.stop();
        mCountDownTextView.reset();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        mLauncherPresenter.destroy();
        super.onDestroy();
    }

    private void showImage(String url) {
        RaeImageLoader.displayImage(url, mDisplayView);
    }

    @Override
    public void onLoadImage(String name, String url) {
        if (TextUtils.isEmpty(url)) {
            onNormalImage();
            return;
        }
        mNameView.setText(Html.fromHtml(name));
        showImage(url);
    }

    @Override
    public void onJumpToWeb(String url) {
        // 网页路径为空不跳转
        if (TextUtils.isEmpty(url)) {
            return;
        }
        AppRoute.jumpToMain(this);
        AppRoute.jumpToWeb(this, url);
        finish();
    }

    @Override
    public void onJumpToBlog(String id) {
        AppRoute.jumpToMain(this);
        AppRoute.jumpToBlogContent(this, id, BlogType.BLOG);
        finish();
    }

    @Override
    public void onNormalImage() {
        // 加载默认
        if (!ThemeCompat.isNight() && mDisplayView != null) {
            mDisplayView.setImageResource(R.drawable.launcher_background);
        }
    }

    @OnClick(R.id.img_launcher_display)
    public void onAdClick() {
        mLauncherPresenter.advertClick();
    }

    @Override
    public void onJumpToMain() {
        AppRoute.jumpToMain(this);
        finish();
    }


    @OnClick(R.id.tv_skip)
    public void onSkipClick() {
        mLauncherPresenter.stop();
        mCountDownTextView.stop();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
