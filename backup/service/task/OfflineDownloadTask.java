package com.rae.cnblogs.sdk.service.task;

import android.content.Context;

/**
 * WIFI定时离线下载任务
 * Created by ChenRui on 2017/2/1 21:36.
 */
public class OfflineDownloadTask extends BlogServiceTask {


    public OfflineDownloadTask(Context context) {
        super(context, null);
    }

    @Override
    public String getTaskName() {
        return "WIFI定时离线下载任务";
    }

    @Override
    protected void runTask() {
        // 开始装饰
        IServiceTask task = new BlogListTask(mContext, null);
        task = new NewsTask(mContext, task);
        task = new KBListTask(mContext, task);
        task = new BlogContentTask(mContext, task);
        task.startTask(); // 开始任务
    }
}
