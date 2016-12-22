package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.IAdvertApi;
import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.core.sdk.ApiUiListener;

/**
 * 广告接口实现
 * Created by ChenRui on 2016/12/22 22:58.
 */
public class AdvertApiImpl extends RaeBlogBaseApi implements IAdvertApi {

    public AdvertApiImpl(Context context) {
        super(context);
    }

    @Override
    public void getLauncherAd(ApiUiListener<AdvertBean> listener) {
        post(url(ApiUrls.RAE_API_URL_LAUNCHER_AD), null, AdvertBean.class, listener);
    }
}
