package com.rae.cnblogs.service.job;

import android.util.Log;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 异步下载任务
 * Created by ChenRui on 2017/7/27 0027 15:59.
 */
public abstract class AsyncDownloadJob implements IJob {

    protected final ScheduledThreadPoolExecutor mExecutorService;

    public AsyncDownloadJob() {
        mExecutorService = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
    }

    protected void execute(Runnable runnable) {
        if (mExecutorService == null || mExecutorService.isTerminated()) {
            Log.e("rae", "线程池已经结束！");
            return;
        }
        // 延迟周期运行，避免过多的CPU消耗
        mExecutorService.schedule(runnable, 1500, TimeUnit.MILLISECONDS);
    }

    @Override
    public void cancel() {
        mExecutorService.shutdown();
    }
}
