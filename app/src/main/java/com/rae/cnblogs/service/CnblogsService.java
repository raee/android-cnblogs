package com.rae.cnblogs.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.rae.cnblogs.AppDataManager;
import com.rae.cnblogs.sdk.db.DbFactory;

/**
 * 博客服务
 * Created by ChenRui on 2017/7/27 0027 15:12.
 */
public class CnblogsService extends Service {

    private CnblogsServiceBinder mBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new CnblogsServiceBinder(this);
        checkCacheSize();
    }

    @Override
    public void onDestroy() {
        mBinder.getJobScheduler().onDestroy();
        super.onDestroy();
    }

    /**
     * 检查缓存大小，超过大小自动清理
     */
    private void checkCacheSize() {
        try {
            AppDataManager appDataManager = new AppDataManager(this);
            boolean isInsufficient = appDataManager.isInsufficient(); // 是否空间不足
            double dbSize = appDataManager.getDatabaseTotalSize();
//            Log.i("rae-service", "是否空间不够：" + isInsufficient + "; 数据库缓存大小：" + dbSize);
            // 当数据大于50MB，清空博客缓存数据
            if (dbSize > 50 || isInsufficient) {
//                Log.i("rae-service", "清除数据！" + dbSize);
                DbFactory.getInstance().clearData();
            }

            // 清除缓存目录
            long cacheSize = appDataManager.getCacheSize();
            // 缓存大小超过1G，或者空间不足的时候清除缓存目录
            if (cacheSize > 1024 || isInsufficient) {
//                Log.i("rae-service", "清除缓存！" + cacheSize);
                appDataManager.clearCache();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
