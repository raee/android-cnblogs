package com.rae.cnblogs.sdk.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.cnblogs.sdk.config.OfflineConfig;
import com.rae.cnblogs.sdk.service.task.BlogContentTask;
import com.rae.cnblogs.sdk.service.task.BlogServiceBroadcastReceiver;
import com.rae.cnblogs.sdk.service.task.BlogServiceTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * 博客服务绑定器
 * Created by ChenRui on 2017/2/1 16:54.
 */
public class BlogServiceBinder extends Binder {

    private final OfflineConfig mConfig;
    private Context mContext;
    private static final int TYPE_BLOG_CONTENT = 100;

    // 任务列表
    private final HashMap<Integer, BlogServiceTask> mTaskHashMap = new HashMap<>();

    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {

            // 开始执行任务
            BlogServiceTask task;
            if (mTaskHashMap.containsKey(msg.what)) {
                task = mTaskHashMap.get(msg.what);
            } else {
                task = createTask(msg.what);
            }

            if (task != null && task.isFinish()) {
                mTaskHashMap.remove(msg.what);
                task = createTask(msg.what);
            }

            if (task != null && !task.isAlive() && !task.isFinish()) {
                task.start(); // 启动任务
            }

            return false;
        }
    });
    private BroadcastReceiver mReceiver;

    public BlogServiceBinder(Context context) {
        mContext = context;

        // 获取离线配置
        mConfig = CnblogSdkConfig.getsInstance(context).getOfflineConfig();

    }


    /**
     * 启动WIFI 定时离线
     */
    public void start() {

        // 先停止
        stop();

        // 计算离线开始时间
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, mConfig.getStartHour());
        calendar.set(Calendar.MINUTE, mConfig.getStartMinute());
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DATE, 1);
        }

        // 添加到系统闹钟里面去
        AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        // 每天重复
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, getPendingIntent());


        // WIFI注册广播
        mReceiver = new BlogServiceBroadcastReceiver();
        mContext.registerReceiver(mReceiver, new IntentFilter(BlogServiceBroadcastReceiver.ACTION_WIFI_OFFLINE));

        Log.i("BlogServiceBinder", "博客服务启动成功！博客下载时间为：" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(calendar.getTime()));

    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(BlogServiceBroadcastReceiver.ACTION_WIFI_OFFLINE);
        return PendingIntent.getBroadcast(mContext, 0, intent, 0);
    }

    public void stop() {

        // 停止闹钟
        AlarmManager alarm = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(getPendingIntent());

        // 移除广播
        if (mReceiver != null) {
            mContext.unregisterReceiver(mReceiver);
        }

    }


    private BlogServiceTask createTask(int type) {
        BlogServiceTask task = null;
        if (type == TYPE_BLOG_CONTENT) {
            task = new BlogContentTask(mContext, null);
        }

        if (task != null) {
            mTaskHashMap.put(type, task);
        }

        return task;
    }

    private void startTask(int type) {
        // 避免重复调用造成多个任务
        mHandler.removeMessages(type);
        mHandler.sendEmptyMessageDelayed(type, 1500);
    }


    /**
     * 开始异步下载博文内容
     */
    public void asyncBlogContent() {
        startTask(TYPE_BLOG_CONTENT);
    }

}
