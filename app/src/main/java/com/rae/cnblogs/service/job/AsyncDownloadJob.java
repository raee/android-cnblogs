package com.rae.cnblogs.service.job;

import android.util.Log;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步下载任务
 * Created by ChenRui on 2017/7/27 0027 15:59.
 */
public abstract class AsyncDownloadJob implements IJob {

    private final ExecutorService mExecutorService;

    public AsyncDownloadJob() {
        mExecutorService = Executors.newFixedThreadPool(3);
        Log.i("rae", "创建线程池!" + mExecutorService.isTerminated());
    }

    protected void execute(Runnable runnable) {
        if (mExecutorService == null || mExecutorService.isTerminated()) {
            Log.e("rae", "线程池已经结束！");
        }
        mExecutorService.execute(runnable);
    }

    @Override
    public void cancel() {
        mExecutorService.shutdown();
    }
}
