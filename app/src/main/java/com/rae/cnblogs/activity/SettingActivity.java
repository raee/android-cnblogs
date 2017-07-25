package com.rae.cnblogs.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.CnblogsApplication;
import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.dialog.impl.HintCardDialog;
import com.rae.cnblogs.dialog.impl.ShareDialog;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.widget.ImageLoadingView;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 设置
 * Created by ChenRui on 2017/7/24 0024 1:18.
 */
public class SettingActivity extends SwipeBackBaseActivity {

    @BindView(R.id.img_clear_cache)
    ImageLoadingView mClearCacheView;

    @BindView(R.id.tv_clear_cache)
    TextView mClearCacheTextView;

    @BindView(R.id.ll_logout)
    View mLogoutLayout;

    private ShareDialog mShareDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        showHomeAsUp();

        if (!UserProvider.getInstance().isLogin()) {
            mLogoutLayout.setVisibility(View.GONE);
        }

    }

    /**
     * 清除缓存
     */
    @OnClick(R.id.ll_clear_cache)
    public void onClearCacheClick() {
        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Exception {
                        // 显示loading
                        mClearCacheView.setVisibility(View.VISIBLE);
                        mClearCacheView.loading();
                        mClearCacheTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    }
                })
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mClearCacheView.anim();

                        // 3秒后隐藏
                        mClearCacheView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                mClearCacheView.startAnimation(AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out));
                                mClearCacheView.setVisibility(View.GONE);

                                mClearCacheTextView.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.default_right_arrow, 0);
                            }
                        }, 5000);
                    }
                })
                .flatMap(new Function<Integer, ObservableSource<Long>>() {
                    @Override
                    public ObservableSource<Long> apply(Integer o) throws Exception {
                        // 清除缓存
                        CnblogsApplication app = (CnblogsApplication) getApplication();
                        app.clearCache();
                        return Observable.timer(1000, TimeUnit.MILLISECONDS);
                    }
                })
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {

                    }
                });
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btn_logout)
    public void onLogoutClick() {

        HintCardDialog dialog = new HintCardDialog(getContext());
        dialog.setMessage(getString(R.string.tips_logout));
        dialog.setOnEnSureListener(new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                dialog.dismiss();
                MobclickAgent.onProfileSignOff();
                UserProvider.getInstance().logout();
                finish();
            }
        });
        dialog.showCloseButton();
        dialog.setEnSureText(getString(R.string.logout));
        dialog.show();

    }

    /**
     * 分享
     */
    @OnClick(R.id.ll_share)
    public void onShareClick() {
        if (mShareDialog == null) {
            mShareDialog = new ShareDialog(getContext());
            mShareDialog.setShareWeb(getString(R.string.share_app_url), getString(R.string.share_app_title), getString(R.string.share_app_desc), R.drawable.ic_share_app);
            mShareDialog.setExtLayoutVisibility(View.GONE);
        }
        mShareDialog.show();
    }

    /**
     * 好评
     */
    @OnClick(R.id.praises)
    public void onPraisesClick() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.market_url))));
        } catch (Exception e) {
            AppUI.failed(this, getString(R.string.praises_error));
        }
    }

}
