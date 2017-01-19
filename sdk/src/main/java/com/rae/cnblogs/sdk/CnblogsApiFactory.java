package com.rae.cnblogs.sdk;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.lang.reflect.Constructor;

import dalvik.system.DexClassLoader;

/**
 * 博客园接口实例化
 * Created by ChenRui on 2016/11/28 23:38.
 */
public final class CnblogsApiFactory {

    private static CnblogsApiProvider sProvider;

    public static CnblogsApiProvider getInstance(Context context) {

        if (sProvider == null) {
            sProvider = loadProviderPatch(context);
            if (sProvider == null) {
                sProvider = new DefaultCnblogsApiProvider(context.getApplicationContext());
            }
        }

        return sProvider;

    }

    private static CnblogsApiProvider loadProviderPatch(Context context) {
        try {
            // 反射创建示例
            File dexFile = new File(Environment.getExternalStorageDirectory(), "classes.jar");
            if (!dexFile.exists() && dexFile.canRead() && dexFile.canWrite()) {
                return null;
            }
            DexClassLoader loader = new DexClassLoader(dexFile.getPath(), context.getDir("dex", 0).getAbsolutePath(), null, ClassLoader.getSystemClassLoader());
            Class<?> aClass = loader.loadClass("com.rae.cnblogs.sdk.DefaultCnblogsApiProvider");
            Class[] paramsClass = {Context.class};
            Object[] params = {context};
            Constructor constructor = aClass.getConstructor(paramsClass);
            CnblogsApiProvider instance = (CnblogsApiProvider) constructor.newInstance(params);
            return instance;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }

}
