package com.rae.cnblogs;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.swift.session.SessionManager;
import com.squareup.leakcanary.LeakCanary;
import com.tencent.bugly.Bugly;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 集成热更新的应用程序
 * Created by ChenRui on 2017/7/25 0025 19:15.
 */
public class CnblogsApplication extends TinkerApplication {

    public CnblogsApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.rae.cnblogs.CnblogsApplicationProxy");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 级别较高的初始化操作
        DbCnblogs.init(getApplication());
        // 日志上报
        Bugly.init(getApplication(), BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            LeakCanary.install(this);
        }

        // LeanCloud用户反馈初始化，要在主线程总
        AVOSCloud.initialize(getApplication(), BuildConfig.LEAN_CLOUD_APP_ID, BuildConfig.LEAN_CLOUD_APP_KEY);
        FeedbackThread.getInstance();
//        SkinCompatManager.withoutActivity(getApplication()).loadSkin("night", SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN);

        // 一些要求不高的初始化操作放到线程中去操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserProvider.init(getApplication());
                SessionManager.initWithConfig(new SessionManager.ConfigBuilder().context(getApplication()).userClass(UserInfoBean.class).build());
                initUmengShareConfig();
            }
        }).start();
    }

    /**
     * 清除应用
     */
    public void clearCache() {
        // 清除图片缓存
        RaeImageLoader.clearCache(getApplicationContext());
        // 清除数据库
        DbFactory.getInstance().clearCache();
        new AppDataManager(this).clearCache();
    }

    /**
     * 友盟分享
     */
    private void initUmengShareConfig() {
        UMShareAPI.get(getApplication());
        PlatformConfig.setWeixin(AppConstant.WECHAT_APP_ID, AppConstant.WECHAT_APP_SECRET);
        PlatformConfig.setSinaWeibo(AppConstant.WEIBO_APP_ID, AppConstant.WEIBO_APP_SECRET, "http://www.raeblog.com/cnblogs/index.php/share/weibo/redirect");
        PlatformConfig.setQQZone(AppConstant.QQ_APP_ID, AppConstant.QQ_APP_SECRET);
    }

    public Application getApplication() {
        return this;
    }
}
