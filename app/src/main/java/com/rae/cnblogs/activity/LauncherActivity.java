package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.ILauncherPresenter;
import com.rae.cnblogs.sdk.bean.BlogType;

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

    ILauncherPresenter mLauncherPresenter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        mLauncherPresenter = CnblogsPresenterFactory.getLauncherPresenter(this, this);
    }

    @Override
    protected void onResume() {
        mLauncherPresenter.start();
        super.onResume();
    }

    @Override
    protected void onStop() {
        mLauncherPresenter.stop();
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
        mNameView.setText(name);
        showImage(url);
    }

    @Override
    public void onJumpToWeb(String url) {
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
        mDisplayView.setImageResource(R.drawable.launcher_background);
//        mDisplayView.startAnimation(AnimationUtils.loadAnimation(this, android.R.anim.fade_out));
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
}
