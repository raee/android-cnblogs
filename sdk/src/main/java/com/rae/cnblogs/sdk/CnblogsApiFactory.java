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
            sProvider = loadProviderPatch(context, new DefaultCnblogsApiProvider(context.getApplicationContext()));
        }

        return sProvider;

    }

    /**
     * @param context
     * @param provider
     * @return
     */
    private static CnblogsApiProvider loadProviderPatch(Context context, CnblogsApiProvider provider) {
//        try {
//            // 反射创建示例
//            File dexFile = new File(Environment.getExternalStorageDirectory(), "classes.dex");
//            if (!dexFile.exists() && dexFile.canRead() && dexFile.canWrite()) {
//                return provider;
//            }
//            DexClassLoader loader = new DexClassLoader(dexFile.getPath(), context.getDir("sdk_dex", 0).getAbsolutePath(), null, DexClassLoader.getSystemClassLoader());
//            Class<?> aClass = loader.loadClass("com.rae.cnblogs.sdk.DefaultCnblogsApiProvider");
//            Class[] paramsClass = {Context.class};
//            Object[] params = {context};
//            Constructor constructor = aClass.getConstructor(paramsClass);
//            CnblogsApiProvider instance = (CnblogsApiProvider) constructor.newInstance(params);
//            return instance.getApiVersion() > provider.getApiVersion() ? instance : provider;
//        } catch (Throwable e) {
//            e.printStackTrace();
//        }
        return provider;
    }

}
