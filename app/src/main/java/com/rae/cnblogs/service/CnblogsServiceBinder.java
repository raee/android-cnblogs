package com.rae.cnblogs.service;

import android.content.Context;
import android.os.Binder;

/**
 * 服务进程绑定器
 * Created by ChenRui on 2017/7/28 0028 17:52.
 */
public class CnblogsServiceBinder extends Binder {

    private JobScheduler mJobScheduler;

    public CnblogsServiceBinder(Context context) {
        mJobScheduler = new JobScheduler(context);
    }

    public JobScheduler getJobScheduler() {
        return mJobScheduler;
    }
}
