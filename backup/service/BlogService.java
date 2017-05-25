//package com.rae.cnblogs.sdk.service;
//
//import android.app.AlarmManager;
//import android.app.IntentService;
//import android.app.PendingIntent;
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//
//import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
//import com.rae.cnblogs.sdk.config.OfflineConfig;
//import com.rae.cnblogs.sdk.service.task.BlogContentTask;
//import com.rae.cnblogs.sdk.service.task.OfflineDownloadTask;
//import com.rae.cnblogs.sdk.service.task.OnTaskFinishListener;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * 博客服务
// * 用于离线下载
// * Created by ChenRui on 2017/2/1 16:54.
// */
//public class BlogService extends IntentService {
//
//    public static final String ACTION_OFFLINE_BLOG_CONTENT = "ACTION_OFFLINE_BLOG_CONTENT";
//    public static final String ACTION_WIFI_OFFLINE = "ACTION_WIFI_OFFLINE";
//    public static final String ACTION_WIFI_OFFLINE_START_TASK = "ACTION_WIFI_OFFLINE_START_TASK";
//
//    public static final String ACTION_OFFLINE_BLOG_CONTENT_RESULT = "ACTION_OFFLINE_BLOG_CONTENT_RESULT";
//
//    // 保证只有一个任务运行
//    private static final Set<String> sTaskSet = new HashSet<>();
//
//
//    public BlogService() {
//        super("BlogIntentService");
//    }
//
//    @Override
//    public void onTrimMemory(int level) {
//        super.onTrimMemory(level);
//        sTaskSet.clear();
//    }
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        if (intent == null) return;
//
//        switch (intent.getAction()) {
//            // 离线下载内容任务
//            case ACTION_OFFLINE_BLOG_CONTENT:
//                startOfflineBlogContentTask();
//                break;
//
//            // WIFI 定时离线
//            case ACTION_WIFI_OFFLINE:
//                startWifiOfflineTask();
//                break;
//
//            // WIFI 定时离线开始
//            case ACTION_WIFI_OFFLINE_START_TASK:
//                start();
//                break;
//        }
//    }
//
//    /**
//     * WIFI 定时离线
//     */
//    private void startWifiOfflineTask() {
//        OfflineDownloadTask task = new OfflineDownloadTask(this);
//        task.startTask();
//    }
//
//    /**
//     * 离线下载内容任务
//     */
//    private void startOfflineBlogContentTask() {
//        if (sTaskSet.contains("BlogContentTask")) return;
//        BlogContentTask task = new BlogContentTask(this, null);
//        task.setOnTaskFinishListener(new OnTaskFinishListener() {
//            @Override
//            public void onTaskFinish(String taskName) {
//                // 发送广播
//                sendBroadcast(new Intent(ACTION_OFFLINE_BLOG_CONTENT_RESULT));
//                sTaskSet.remove("BlogContentTask");
//            }
//        });
//        task.startTask();
//        sTaskSet.add("BlogContentTask");
//    }
//
//
//    /**
//     * 启动WIFI 定时离线
//     */
//    public void start() {
//
//        // 先停止
//        stop();
//
//        // 计算离线开始时间
//        Calendar calendar = Calendar.getInstance();
//        OfflineConfig mConfig = CnblogSdkConfig.getsInstance(this).getOfflineConfig();
//        calendar.set(Calendar.HOUR_OF_DAY, mConfig.getStartHour());
//        calendar.set(Calendar.MINUTE, mConfig.getStartMinute());
//        calendar.set(Calendar.SECOND, 0);
//        calendar.set(Calendar.MILLISECOND, 0);
//
//        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
//            calendar.add(Calendar.DATE, 1);
//        }
//
//        // 添加到系统闹钟里面去
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        // 每天重复
//        alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 86400000, getPendingIntent());
//
//        Log.i("BlogServiceBinder", "博客服务启动成功！博客下载时间为：" + new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(calendar.getTime()));
//
//    }
//
//    private PendingIntent getPendingIntent() {
//        return PendingIntent.getBroadcast(this, 0, new Intent(ACTION_WIFI_OFFLINE), 0);
//    }
//
//    public void stop() {
//        // 停止闹钟
//        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//        alarm.cancel(getPendingIntent());
//    }
//}
