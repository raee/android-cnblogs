package com.rae.cnblogs;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.squareup.leakcanary.LeakCanary;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 应用程序
 * Created by ChenRui on 2016/12/1 21:35.
 */
public class CnblogsApplication extends Application {

    private static CnblogsApplication sCnblogsApplication;

    public static CnblogsApplication getInstance() {
        return sCnblogsApplication;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sCnblogsApplication = this;
        DbCnblogs.init(this);
        RaeImageLoader.initImageLoader(this);
        initUmengShareConfig();
        UserProvider.init(this);
        LeakCanary.install(this);
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

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
