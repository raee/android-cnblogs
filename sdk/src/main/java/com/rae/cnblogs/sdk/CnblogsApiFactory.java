package com.rae.cnblogs.sdk;

import android.content.Context;

/**
 * 博客园接口实例化
 * Created by ChenRui on 2016/11/28 23:38.
 */
public final class CnblogsApiFactory {

    private static CnblogsApiProvider sProvider;

    public static CnblogsApiProvider getInstance(Context context) {

        if (sProvider == null) {
            synchronized (CnblogsApiFactory.class) {
                if (sProvider == null) {
                    initProvider(new DefaultCnblogsApiProvider(context.getApplicationContext()));
                }
            }
        }
        return sProvider;
    }

    /**
     * 初始化接口提供程序
     *
     * @param provider 提供程序
     */
    public static void initProvider(CnblogsApiProvider provider) {
        sProvider = provider;
    }
}
