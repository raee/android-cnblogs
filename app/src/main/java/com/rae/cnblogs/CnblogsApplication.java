package com.rae.cnblogs;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.db.DbCnblogs;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.swift.session.SessionManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;

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
//            LeakCanary.install(this);
        }
    }

    /**
     * 友盟分享
     */
    private void initUmengShareConfig() {
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


    /**
     * 清除应用
     */
    public void clearCache() {
        // 清除图片缓存
        ImageLoader.getInstance().clearDiskCache();
        ImageLoader.getInstance().clearMemoryCache();

        // 清除文件缓存
        deleteDir(getExternalCacheDir());
        deleteDir(getCacheDir());

        // 清除数据库
        DbFactory.getInstance().clearCache();


    }


    private boolean deleteDir(File file) {
        if (file != null && file.exists()) {
            try {
                if (file.isDirectory()) {
                    // 删除文件夹
                    File[] files = file.listFiles();
                    if (files.length <= 0) {
                        return file.delete();
                    }
                    for (File item : files) {
                        deleteDir(item); // 递归删除子文件
                    }
                } else {
                    return file.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }
}
