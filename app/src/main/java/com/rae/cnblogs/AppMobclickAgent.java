package com.rae.cnblogs;

import android.annotation.SuppressLint;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * APP 统计
 * Created by ChenRui on 2017/2/8 0008 11:44.
 */
public final class AppMobclickAgent {

    /**
     * 统计打开时间
     */
    public static void onAppOpenEvent(Context context) {
        @SuppressLint("SimpleDateFormat")
        String openDate = new SimpleDateFormat("HH:mm").format(new Date());
        MobclickAgent.onEvent(context, "APP_OPEN_EVENT", openDate);
    }

    private static void onAdEvent(Context context, Map<String, String> map) {
        MobclickAgent.onEvent(context, " APP_AD_EVENT", map);
    }

    /**
     * 启动页广告曝光次数
     *
     * @param context
     * @param id      广告ID
     */
    public static void onLaunchAdExposureEvent(Context context, String id, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "Exposure");
        map.put("id", id);
        map.put("name", name);
        onAdEvent(context, map);
    }

    /**
     * 启动页广告点击次数
     *
     * @param context
     * @param id      广告ID
     */
    public static void onLaunchAdClickEvent(Context context, String id, String name) {
        Map<String, String> map = new HashMap<>();
        map.put("type", "Click");
        map.put("id", id);
        map.put("name", name);
        onAdEvent(context, map);
    }
}
