package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.presenter.ILauncherPresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.db.DbAdvert;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import io.reactivex.Observable;

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
        if (mAdvertBean == null) return;
        // 统计
        AppMobclickAgent.onLaunchAdClickEvent(mContext, mAdvertBean.getAd_id(), mAdvertBean.getAd_name());
        mCountDownTimer.cancel();

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
        RxObservable.dispose("thread");
        mCountDownTimer.cancel();
    }

    @Override
    public void destroy() {
        stop();
        super.destroy();
    }

    @Override
    public void start() {
        mCountDownTimer.start();

        // 异步下载新的
        createObservable(mRaeServerApi.getLauncherAd())
                .subscribe(new ApiDefaultObserver<AdvertBean>() {
                    @Override
                    protected void onError(String message) {
                        // 不处理
                    }

                    @Override
                    protected void accept(AdvertBean data) {
                        // 保存到数据，等待下一次加载
                        mDbAdvert.save(data);
                    }
                });

        // 从本地加载
        AdvertBean item = mDbAdvert.getLauncherAd();
        if (item == null) {
            mView.onNormalImage();
        } else {
            createObservable(Observable.just(item))
                    .subscribe(new ApiDefaultObserver<AdvertBean>() {
                        @Override
                        protected void onError(String message) {
                            onSuccess(null);
                        }

                        @Override
                        protected void accept(AdvertBean advertBean) {
                            onSuccess(advertBean);
                        }


                        private void onSuccess(AdvertBean data) {
                            mAdvertBean = data;
                            if (mAdvertBean == null || TextUtils.isEmpty(data.getImage_url())) {
                                mView.onNormalImage();
                                return;
                            }
                            long endTime = ApiUtils.parseDefaultDate(mAdvertBean.getAd_end_date()).getTime();
                            if (System.currentTimeMillis() > endTime) {
                                mView.onNormalImage();
                            } else {
                                // 统计
                                AppMobclickAgent.onLaunchAdExposureEvent(mContext, mAdvertBean.getAd_id(), mAdvertBean.getAd_name());
                                mView.onLoadImage(mAdvertBean.getAd_name(), mAdvertBean.getImage_url());
                            }
                        }
                    });
        }

    }
}
