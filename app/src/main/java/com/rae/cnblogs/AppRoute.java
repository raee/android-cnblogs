package com.rae.cnblogs;

import android.content.Context;
import android.content.Intent;

import com.rae.cnblogs.activity.BlogContentActivity;
import com.rae.cnblogs.activity.MainActivity;
import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 路由
 * Created by ChenRui on 2016/12/6 23:49.
 */
public final class AppRoute {

    private static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private static void startActivity(Context context, Class<?> cls) {
        startActivity(context, new Intent(context, cls));
    }

    public static void jumpToBlogContent(Context context, Blog blog) {
        Intent intent = new Intent(context, BlogContentActivity.class);
        intent.putExtra("blog", blog);
        startActivity(context, intent);
    }

    public static void jumpToWeb(String url) {
        // TODO WEB VIEW ACTIVITY
    }

    public static void jumpToMain(Context context) {
        startActivity(context, MainActivity.class);
    }
}
