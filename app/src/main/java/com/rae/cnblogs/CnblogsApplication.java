package com.rae.cnblogs;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.swift.session.SessionManager;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

/**
 * 集成热更新的应用程序
 * Created by ChenRui on 2017/7/25 0025 19:15.
 */
public class CnblogsApplication extends TinkerApplication {

    private static RefWatcher refWatcher;


    public static void watch(Object obj) {
        if (refWatcher != null) {
            refWatcher.watch(obj);
        }
    }

    public CnblogsApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.rae.cnblogs.CnblogsApplicationProxy");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // 级别较高的初始化操作
        DbCnblogs.init(getApplication());
        RaeImageLoader.initImageLoader(getApplication());
        if (!LeakCanary.isInAnalyzerProcess(this)) {
            refWatcher = LeakCanary.install(this);
        }

        // LeanCloud用户反馈初始化
        AVOSCloud.initialize(getApplication(), BuildConfig.LEAN_CLOUD_APP_ID, BuildConfig.LEAN_CLOUD_APP_KEY);
        AVOSCloud.setDebugLogEnabled(BuildConfig.DEBUG);


        // 一些要求不高的初始化操作放到线程中去操作
        new Thread(new Runnable() {
            @Override
            public void run() {
                UserProvider.init(getApplication());
                SessionManager.initWithConfig(new SessionManager.ConfigBuilder().context(getApplication()).userClass(UserInfoBean.class).build());
                initUmengShareConfig();


                // 日志上报
//                Bugly.init(getApplication(), BuildConfig.BUGLY_APP_ID, BuildConfig.DEBUG);

                if (BuildConfig.BUILD_TYPE.equals("debug") || BuildConfig.DEBUG) {
                    onDebugMode();
                }
            }
        }).start();
    }

    /**
     * 进入调试模式
     */
    private void onDebugMode() {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        // DEX分包
        MultiDex.install(base);

        // 安装tinker
        Beta.installTinker(this);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_LOW || level == TRIM_MEMORY_BACKGROUND) {
            ImageLoader.getInstance().getMemoryCache().clear();
        }
    }

    /**
     * 清除应用
     */
    public void clearCache() {
        // 清除图片缓存
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();
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
