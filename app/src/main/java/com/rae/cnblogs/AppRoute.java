package com.rae.cnblogs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.rae.cnblogs.activity.BlogContentActivity;
import com.rae.cnblogs.activity.LoginActivity;
import com.rae.cnblogs.activity.MainActivity;
import com.rae.cnblogs.activity.WebActivity;
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

    public static void jumpToWeb(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.setData(Uri.parse(url));
        startActivity(context, intent);
    }

    public static void jumpToMain(Context context) {
        startActivity(context, MainActivity.class);
    }

    public static void jumpToDownload(Context context, String url) {
        try {
            Intent intent = new Intent();
            intent.setData(Uri.parse(url));
            startActivity(context, intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     */
    public static void jumpToLogin(Context context) {
        startActivity(context, LoginActivity.class);
    }
}
