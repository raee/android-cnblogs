package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.presenter.ILauncherPresenter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IAdvertApi;
import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.db.DbAdvert;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:56.
 */
public class LauncherPresenterImpl extends BasePresenter<ILauncherPresenter.ILauncherView> implements ILauncherPresenter {

    IAdvertApi mAdvertApi;
    DbAdvert mDbAdvert;
    AdvertBean mAdvertBean;

    public LauncherPresenterImpl(Context context, ILauncherView view) {
        super(context, view);
        mAdvertApi = CnblogsApiFactory.getAdvertApi(context);
        mDbAdvert = new DbAdvert(context);
    }

    @Override
    public void advertClick() {

    }

    @Override
    public void start() {

        mAdvertBean = mDbAdvert.getLauncherAd();

        if (mAdvertBean != null) {
            mView.onLoadImage(mAdvertBean.getAd_url());
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
