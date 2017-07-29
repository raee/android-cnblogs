package com.rae.cnblogs;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.beta.Beta;
import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

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
        if (BuildConfig.DEBUG && LeakCanary.isInAnalyzerProcess(this)) {
            refWatcher = LeakCanary.install(this);
        }
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


}
