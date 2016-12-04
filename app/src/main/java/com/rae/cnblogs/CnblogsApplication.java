package com.rae.cnblogs;

import android.app.Application;

/**
 * 应用程序
 * Created by ChenRui on 2016/12/1 21:35.
 */
public class CnblogsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RaeImageLoader.initImageLoader(this);
    }
}
