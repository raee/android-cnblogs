package com.rae.cnblogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.rae.cnblogs.activity.BlogContentActivity;
import com.rae.cnblogs.activity.BloggerActivity;
import com.rae.cnblogs.activity.ImagePreviewActivity;
import com.rae.cnblogs.activity.LoginActivity;
import com.rae.cnblogs.activity.MainActivity;
import com.rae.cnblogs.activity.WebActivity;
import com.rae.cnblogs.activity.WebLoginActivity;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;

/**
 * 路由
 * Created by ChenRui on 2016/12/6 23:49.
 */
public final class AppRoute {

    // WEB 登录
    public static final int REQ_CODE_WEB_LOGIN = 100;

    private static void startActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }

    private static void startActivity(Context context, Class<?> cls) {
        startActivity(context, new Intent(context, cls));
    }

    private static void startActivityForResult(Activity context, Intent intent, int requestCode) {
        context.startActivityForResult(intent, requestCode);
    }

    private static void startActivityForResult(Activity context, Class<?> cls, int requestCode) {
        startActivityForResult(context, new Intent(context, cls), requestCode);
    }

    public static void jumpToBlogContent(Context context, BlogBean blog, BlogType type) {
        Intent intent = new Intent(context, BlogContentActivity.class);
        intent.putExtra("blog", blog);
        intent.putExtra("type", type.getTypeName());
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

    /**
     * 登录
     */
    public static void jumpToWebLogin(Activity context) {
        startActivityForResult(context, WebLoginActivity.class, REQ_CODE_WEB_LOGIN);
    }

    /**
     * 个人中心
     */
    public static void jumpToUserCenter(Context context) {

    }

    public static void jumpToImagePreview(Context context) {
        startActivity(context, ImagePreviewActivity.class);
    }

    public static void jumpToBlogger(Context context, String blogApp) {
        startActivity(context, BloggerActivity.class);
    }
}
