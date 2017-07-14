package com.rae.cnblogs;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.swift.session.SessionManager;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
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
        SessionManager.initWithConfig(new SessionManager.ConfigBuilder().context(getApplicationContext()).userClass(UserInfoBean.class).build());
        if (!BuildConfig.BUILD_TYPE.equals("debug")) {
            CrashReport.initCrashReport(getApplicationContext(), "7e5c07e5c9", BuildConfig.DEBUG);
        }
        if (BuildConfig.DEBUG) {
            LeakCanary.install(this);
        }
    }

    /**
     * 友盟分享
     */
    private void initUmengShareConfig() {
        Config.DEBUG = true;
        UMShareAPI.get(this);
        PlatformConfig.setWeixin(AppConstant.WECHAT_APP_ID, AppConstant.WECHAT_APP_SECRET);
        PlatformConfig.setSinaWeibo(AppConstant.WEIBO_APP_ID, AppConstant.WEIBO_APP_SECRET, "http://www.raeblog.com/cnblogs/index.php/share/weibo/redirect");
        PlatformConfig.setQQZone(AppConstant.QQ_APP_ID, AppConstant.QQ_APP_SECRET);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
