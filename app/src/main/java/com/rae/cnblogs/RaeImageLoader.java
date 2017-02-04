package com.rae.cnblogs;

import android.content.Context;

import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 图片加载器
 * Created by ChenRui on 2016/12/3 17:01.
 */
public final class RaeImageLoader {
    private static DisplayImageOptions.Builder sDisplayImageOptions;

    static {
        sDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(false);
    }

    public static DisplayImageOptions.Builder defaultOptions() {
        return sDisplayImageOptions;
    }

    //
    public static DisplayImageOptions.Builder fadeOptions(int durationMillis) {
        return defaultOptions().displayer(new FadeInBitmapDisplayer(durationMillis));
    }

    /**
     * 头像的默认配置
     *
     * @return
     */
    public static DisplayImageOptions headerOption() {
        return defaultOptions()
                .displayer(new FadeInBitmapDisplayer(800))
                .showImageForEmptyUri(R.drawable.ic_default_user_avatar)
                .showImageOnLoading(R.drawable.ic_default_user_avatar)
                .showImageOnFail(R.drawable.ic_default_user_avatar)
                .build();
    }

    /**
     * 头像的默认配置
     *
     * @return
     */
    public static DisplayImageOptions headerOptionWithoutAnim() {
        return defaultOptions()
                .showImageForEmptyUri(R.drawable.ic_default_user_avatar)
                .showImageOnLoading(R.drawable.ic_default_user_avatar)
                .showImageOnFail(R.drawable.ic_default_user_avatar)
                .build();
    }


    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(5)//线程池
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程优先级
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LargestLimitedMemoryCache(1024))//内存缓存
                .memoryCacheSize(1024)//内存缓存大小
                .diskCacheSize(50 * 1024 * 1024)//存储卡缓存大小
                .diskCacheFileCount(300)//存储卡文件个数
//                .memoryCacheSizePercentage(13) // default
//                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // default
                .defaultDisplayImageOptions(defaultOptions().build()) // default
//                .writeDebugLogs()
//                .tasksProcessingOrder(QueueProcessingType.FIFO)  //先进先出
                .build();

        ImageLoader.getInstance().init(configuration);
    }
}
