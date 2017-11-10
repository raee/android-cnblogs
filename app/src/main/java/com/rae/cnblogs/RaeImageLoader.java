package com.rae.cnblogs;

import android.content.Context;
import android.widget.ImageView;

/**
 * 图片加载器
 * Created by ChenRui on 2016/12/3 17:01.
 */
public final class RaeImageLoader {

    /**
     * 头像显示
     */
    public static void displayHeaderImage(String url, ImageView view) {
        Context context = view.getContext();
        GlideApp.with(context)
                .load(url)
                .placeholder(R.drawable.ic_default_user_avatar)
                .into(view);
    }

    public static void displayImage(String url, ImageView view) {
        Context context = view.getContext();
        GlideApp.with(context)
                .load(url)
                .placeholder(R.color.background_divider)
                .into(view);
    }

    /**
     * 清除缓存
     */
    public static void clearCache(final Context applicationContext) {
        clearMemoryCache(applicationContext); // 在主线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 在线程中
                GlideApp.get(applicationContext).clearDiskCache();
            }
        }).start();
    }

    /**
     * 清除缓存
     */
    public static void clearMemoryCache(Context applicationContext) {
        GlideApp.get(applicationContext).clearMemory();
    }
}
