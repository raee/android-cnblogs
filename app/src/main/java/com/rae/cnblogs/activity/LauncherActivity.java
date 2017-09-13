package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.ILauncherPresenter;
import com.rae.cnblogs.sdk.bean.BlogType;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

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
        super.onResume();

        // 判断主界面退出时间是否过短
        long mainExitTimeMillis = config().getMainExitTimeMillis();
        long span = System.currentTimeMillis() - mainExitTimeMillis;

        // 第一次或者是程序退出的时间超过1分钟(60000)，就启动当前界面
        if (mainExitTimeMillis <= 0 || span > 60000) {
            mLauncherPresenter.start();
        } else {
            // 跳过启动界面
            AppRoute.jumpToMain(this);
            Observable.timer(500, TimeUnit.MILLISECONDS)
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            finish();
                        }
                    });
        }

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
        if (!ThemeCompat.isNight()) {
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
}
