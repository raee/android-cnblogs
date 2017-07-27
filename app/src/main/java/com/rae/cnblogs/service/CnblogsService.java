package com.rae.cnblogs.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * 博客服务
 * Created by ChenRui on 2017/7/27 0027 15:12.
 */
public class CnblogsService extends Service {

    private JobScheduler mJobScheduler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("rae", "服务启动了");
        mJobScheduler = new JobScheduler(this);
        // 开始任务
        mJobScheduler.start(JobScheduler.ACTION_BLOG_CONTENT);
    }
}
