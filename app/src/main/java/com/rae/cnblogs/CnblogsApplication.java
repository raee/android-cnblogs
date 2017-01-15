package com.rae.cnblogs;

import android.app.Application;

import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 应用程序
 * Created by ChenRui on 2016/12/1 21:35.
 */
public class CnblogsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RaeImageLoader.initImageLoader(this);
        initUmengShareConfig();
    }

    /**
     * 友盟分享
     */
    private void initUmengShareConfig() {
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(AppConstant.WECHAT_APP_ID, AppConstant.WECHAT_APP_SECRET);
        PlatformConfig.setSinaWeibo(AppConstant.WEIBO_APP_ID, AppConstant.WEIBO_APP_SECRET);
        PlatformConfig.setQQZone(AppConstant.QQ_APP_ID, AppConstant.QQ_APP_SECRET);
    }
}
