package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.os.CountDownTimer;

import com.github.raee.cnblogs.sdk.IAdvertApi;
import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.presenter.ILauncherPresenter;
import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.db.DbAdvert;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.utils.RaeDateUtil;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:56.
 */
public class LauncherPresenterImpl extends BasePresenter<ILauncherPresenter.ILauncherView> implements ILauncherPresenter {

    private IAdvertApi mAdvertApi;
    private DbAdvert mDbAdvert;
    private AdvertBean mAdvertBean;
    private CountDownTimer mCountDownTimer;

    public LauncherPresenterImpl(Context context, ILauncherView view) {
        super(context, view);
        mAdvertApi = getServerApi().getAdvertApi();
        mDbAdvert = new DbAdvert();
        mCountDownTimer = new CountDownTimer(3500, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                mView.onJumpToMain();
            }
        };
    }

    @Override
    public void advertClick() {
        mCountDownTimer.cancel();

        // 统计
        AppMobclickAgent.onLaunchAdClickEvent(mContext, mAdvertBean.getAd_id(), mAdvertBean.getAd_name());

        if ("URL".equalsIgnoreCase(mAdvertBean.getJump_type())) {
            mView.onJumpToWeb(mAdvertBean.getAd_url());
        } else if ("BLOG_CONTENT".equalsIgnoreCase(mAdvertBean.getJump_type())) {
            mView.onJumpToBlog("");
        } else {
            mView.onJumpToMain();
        }

    }

    @Override
    public void stop() {
        mCountDownTimer.cancel();
    }

    @Override
    public void start() {

        mCountDownTimer.start();
        mAdvertBean = mDbAdvert.getLauncherAd();

        if (mAdvertBean != null) {
            long endTime = RaeDateUtil.parseDefaultDate(mAdvertBean.getAd_end_date()).getTime();
            if (System.currentTimeMillis() > endTime) {
                mView.onNormalImage();
            } else {
                // 统计
                AppMobclickAgent.onLaunchAdExposureEvent(mContext, mAdvertBean.getAd_id(), mAdvertBean.getAd_name());
                mView.onLoadImage(mAdvertBean.getAd_name(), mAdvertBean.getImage_url());
            }
        }

        mAdvertApi.getLauncherAd(new ApiUiListener<AdvertBean>() {
            @Override
            public void onApiFailed(ApiException ex, String msg) {

                if (mAdvertBean == null) {
                    mView.onNormalImage();
                }

            }

            @Override
            public void onApiSuccess(AdvertBean data) {
                // 保存到数据，下一次加载
                mDbAdvert.save(data);
            }
        });

    }
}
