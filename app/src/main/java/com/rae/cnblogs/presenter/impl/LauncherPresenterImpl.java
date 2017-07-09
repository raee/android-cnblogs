package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.os.CountDownTimer;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.presenter.ILauncherPresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.db.DbAdvert;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.utils.ApiUtils;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:56.
 */
public class LauncherPresenterImpl extends BasePresenter<ILauncherPresenter.ILauncherView> implements ILauncherPresenter {

    private IRaeServerApi mRaeServerApi;
    private DbAdvert mDbAdvert;
    private AdvertBean mAdvertBean;
    private CountDownTimer mCountDownTimer;

    public LauncherPresenterImpl(Context context, ILauncherView view) {
        super(context, view);
        mRaeServerApi = getApiProvider().getRaeServerApi();
        mDbAdvert = DbFactory.getInstance().getAdvert();
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
            long endTime = ApiUtils.parseDefaultDate(mAdvertBean.getAd_end_date()).getTime();
            if (System.currentTimeMillis() > endTime) {
                mView.onNormalImage();
            } else {
                // 统计
                AppMobclickAgent.onLaunchAdExposureEvent(mContext, mAdvertBean.getAd_id(), mAdvertBean.getAd_name());
                mView.onLoadImage(mAdvertBean.getAd_name(), mAdvertBean.getImage_url());
            }
        }

        createObservable(mRaeServerApi.getLauncherAd()).subscribe(new ApiDefaultObserver<AdvertBean>() {
            @Override
            protected void onError(String message) {
                if (mAdvertBean == null) {
                    mView.onNormalImage();
                }
            }

            @Override
            protected void accept(AdvertBean data) {
                // 保存到数据，下一次加载
                mDbAdvert.save(data);
            }
        });

    }
}
