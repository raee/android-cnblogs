package com.rae.cnblogs.sdk.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * 博客服务
 * 用于离线下载
 * Created by ChenRui on 2017/2/1 16:54.
 */
public class BlogService extends Service {


    private BlogServiceBinder mBinder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new BlogServiceBinder(this);
        mBinder.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBinder.stop();
    }
}
