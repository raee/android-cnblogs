package com.rae.cnblogs.service;

import android.content.Context;

import com.rae.cnblogs.service.job.BlogContentJob;
import com.rae.cnblogs.service.job.IJob;

import java.util.WeakHashMap;

/**
 * 任务调度
 * Created by ChenRui on 2017/7/27 0027 15:44.
 */
public class JobScheduler {

    public static final int ACTION_BLOG_CONTENT = 0;

    private final WeakHashMap<Integer, IJob> mJobMap = new WeakHashMap<>();
    private final Context mContext;


    public JobScheduler(Context context) {
        mContext = context;
    }

    /**
     * 开始分配工作
     *
     * @param action 工作类型
     */
    public void start(int action) {
        IJob job = getJob(action);
        if (job == null) return;
        job.run();
    }

    /**
     * 下载博文任务
     */
    public void startDownloadBlogContent() {
        start(ACTION_BLOG_CONTENT);
    }

    private IJob getJob(int action) {
        IJob job = mJobMap.get(action);
        if (job == null) {
            switch (action) {
                case ACTION_BLOG_CONTENT:
                    job = new BlogContentJob(mContext);
                    break;
            }
            mJobMap.put(action, job);
        }
        return job;
    }
}
