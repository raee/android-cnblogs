package com.rae.cnblogs.sdk.db;

import android.content.Context;
import android.database.sqlite.SQLiteFullException;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.query.Delete;
import com.rae.cnblogs.sdk.BuildConfig;
import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

/**
 * 本地数据库
 * Created by ChenRui on 2016/12/1 00:24.
 */
public abstract class DbCnblogs {

    DbCnblogs() {
    }

    // 注册数据库
    public static void init(Context applicationContext) {
        Configuration config = new Configuration
                .Builder(applicationContext)
                .setDatabaseName("cnblogs.db")
                .setDatabaseVersion(1)
                .addModelClass(CategoryBean.class)
                .addModelClass(UserBlogInfo.class)
                .addModelClass(AdvertBean.class)
                .addModelClass(BlogBean.class)
                .create();
        ActiveAndroid.initialize(config, BuildConfig.LOG_DEBUG);
    }

    protected void executeTransaction(Runnable runnable) {
        try {
            ActiveAndroid.beginTransaction();
            runnable.run();
            ActiveAndroid.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ActiveAndroid.endTransaction();
            } catch (SQLiteFullException e) {
                // 数据库缓存已经满了，清除缓存
                // fix bug #690
                new Delete().from(UserBlogInfo.class).execute();
                new Delete().from(BlogBean.class).execute();
                ActiveAndroid.clearCache();
            }
        }
    }


}
