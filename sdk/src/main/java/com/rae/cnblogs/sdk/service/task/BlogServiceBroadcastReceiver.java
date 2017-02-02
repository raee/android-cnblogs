package com.rae.cnblogs.sdk.service.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 每天WIFI离线任务
 * Created by ChenRui on 2017/2/1 21:06.
 */
public class BlogServiceBroadcastReceiver extends BroadcastReceiver {

    public static final String ACTION_WIFI_OFFLINE = "com.rae.cnblogs.sdk.service.task.BlogServiceBroadcastReceiver#ACTION_WIFI_OFFLINE";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (ACTION_WIFI_OFFLINE.equalsIgnoreCase(intent.getAction())) {
            // 开始离线下载
            new OfflineDownloadTask(context).start();
        }
    }
}
