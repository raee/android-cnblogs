package com.rae.cnblogs;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogAppConfig;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.swift.session.SessionManager;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.tencent.bugly.beta.tinker.TinkerApplicationLike;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import skin.support.SkinCompatManager;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * 热更新应用程序代理类，实例化在：{@link CnblogsTinkerApplication} 中操作
 * Created by ChenRui on 2016/12/1 21:35.
 */
public class CnblogsApplicationProxy extends TinkerApplicationLike {


    public CnblogsApplicationProxy(Application application, int tinkerFlags, boolean tinkerLoadVerifyFlag, long applicationStartElapsedTime, long applicationStartMillisTime, Intent tinkerResultIntent) {
        super(application, tinkerFlags, tinkerLoadVerifyFlag, applicationStartElapsedTime, applicationStartMillisTime, tinkerResultIntent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 级别较高的初始化操作
        if (!BuildConfig.DEBUG) {
            // 正式环境
            Bugly.init(getApplication(), BuildConfig.BUGLY_APP_ID, false);
        }

        // 初始化路由
        AppRoute.init(getApplication(), BuildConfig.DEBUG);
        DbFactory.init(getApplication());


        // LeanCloud用户反馈初始化，要在主线程
        AVOSCloud.initialize(getApplication(), BuildConfig.LEAN_CLOUD_APP_ID, BuildConfig.LEAN_CLOUD_APP_KEY);
        FeedbackThread.getInstance();

        // 加载皮肤
        SkinCompatManager.init(getApplication());
        SkinActivityLifecycleCompat.init(getApplication());
        SkinCompatManager.getInstance()
                .addHookInflater(new ThemeCompat.CnblogsThemeHookInflater())
                .addInflater(new CnblogsLayoutInflater())
                .addInflater(new SkinMaterialViewInflater());

        SkinCompatManager.getInstance().loadSkin();

        // 一些要求不高的初始化操作放到线程中去操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                initUmengConfig();
                UserProvider.init(getApplication());
                SessionManager.initWithConfig(new SessionManager.ConfigBuilder().context(getApplication()).userClass(UserInfoBean.class).build());

            }
        }).start();
    }


    /**
     * 友盟
     */
    private void initUmengConfig() {
        // 初始化友盟
        String channel = ApplicationCompat.getChannel(getApplication());
        CnblogAppConfig.APP_CHANNEL = channel;
        AppMobclickAgent.init(getApplication(), BuildConfig.UMENG_APPKEY, channel);

        // 初始化友盟分享
        UMShareAPI.get(getApplication());
        PlatformConfig.setWeixin(AppConstant.WECHAT_APP_ID, AppConstant.WECHAT_APP_SECRET);
        PlatformConfig.setSinaWeibo(AppConstant.WEIBO_APP_ID, AppConstant.WEIBO_APP_SECRET, "https://raedev.io/cnblogs/share/weibo/redirect");
        PlatformConfig.setQQZone(AppConstant.QQ_APP_ID, AppConstant.QQ_APP_SECRET);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == ComponentCallbacks2.TRIM_MEMORY_RUNNING_LOW || level == ComponentCallbacks2.TRIM_MEMORY_BACKGROUND) {
            RaeImageLoader.clearCache(getApplication());
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void onBaseContextAttached(Context base) {
        super.onBaseContextAttached(base);
        // 安装tinker
        Beta.installTinker(this);
    }
}
