package com.rae.cnblogs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.rae.cnblogs.activity.BlogContentActivity;
import com.rae.cnblogs.activity.BloggerActivity;
import com.rae.cnblogs.activity.CategoryActivity;
import com.rae.cnblogs.activity.FavoritesActivity;
import com.rae.cnblogs.activity.FriendsActivity;
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
    /*朋友界面 - 来自粉丝*/
    public static final int ACTIVITY_FRIENDS_TYPE_FANS = 1;
    /*朋友界面 - 来自关注*/
    public static final int ACTIVITY_FRIENDS_TYPE_FOLLOW = 2;
    public static final int REQ_CODE_CATEGORY = 102;
    public static final int REQ_CODE_FAVORITES = 103;

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

    public static void jumpToWebNewTask(Context context, String url) {
        Intent intent = new Intent(context, WebActivity.class);
        intent.setData(Uri.parse(url));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(context, intent);
    }

    public static void jumpToMain(Context context) {
        startActivity(context, MainActivity.class);
    }

//    public static void jumpToDownload(Context context, String url) {
//        try {
//            Intent intent = new Intent();
//            intent.setData(Uri.parse(url));
//            startActivity(context, intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

    public static void jumpToFans(Context context, String bloggerName, String userId) {
        jumpToFriends(context, ACTIVITY_FRIENDS_TYPE_FANS, bloggerName, userId);
    }

    public static void jumpToFollow(Context context, String bloggerName, String userId) {
        jumpToFriends(context, ACTIVITY_FRIENDS_TYPE_FOLLOW, bloggerName, userId);
    }

    /**
     * 跳转到朋友界面
     *
     * @param type        来源类型，参考该类{@link #ACTIVITY_FRIENDS_TYPE_FANS}
     * @param bloggerName 博主昵称
     * @param userId      博主ID
     */
    public static void jumpToFriends(Context context, int type, String bloggerName, String userId) {
        Intent intent = new Intent(context, FriendsActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("bloggerName", bloggerName);
        intent.putExtra("fromType", type);
        startActivity(context, intent);
    }

    public static void jumpToImagePreview(Context context) {
        startActivity(context, ImagePreviewActivity.class);
    }

    public static void jumpToBlogger(Context context, String blogApp) {
        if (TextUtils.isEmpty(blogApp)) {
            AppUI.toast(context, "博主信息为空！");
            return;
        }
        Intent intent = new Intent(context, BloggerActivity.class);
        intent.putExtra("blogApp", blogApp);
        startActivity(context, intent);
    }


    public static void jumpToCategoryForResult(Activity context) {
        Intent intent = new Intent(context, CategoryActivity.class);
        startActivityForResult(context, intent, REQ_CODE_CATEGORY);
    }

    /**
     * 我的收藏
     */
    public static void jumpToFavorites(Activity context) {
        startActivityForResult(context, FavoritesActivity.class, REQ_CODE_FAVORITES);
    }
}
