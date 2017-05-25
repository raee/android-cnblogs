package com.rae.cnblogs.sdk.service.task;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rae.cnblogs.sdk.service.BlogService;


/**
 * 每天WIFI离线任务
 * Created by ChenRui on 2017/2/1 21:06.
 */
public class BlogServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // 开始离线下载
        if (BlogService.ACTION_WIFI_OFFLINE.equals(intent.getAction())) {
            context.startService(new Intent(intent.getAction()));
        }
    }
}
