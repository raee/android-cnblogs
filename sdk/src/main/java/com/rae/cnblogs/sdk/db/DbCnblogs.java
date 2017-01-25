package com.rae.cnblogs.sdk.db;

import android.content.Context;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;

/**
 * 本地数据库
 * Created by ChenRui on 2016/12/1 00:24.
 */
public abstract class DbCnblogs<T> {

    public static void init(Context applicationContext) {
        Configuration config = new Configuration
                .Builder(applicationContext)
                .setDatabaseName("rae.db")
                .setDatabaseVersion(1)
                .create();
        ActiveAndroid.initialize(config, true);
    }

    public DbCnblogs() {
    }

}
