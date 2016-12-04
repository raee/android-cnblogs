package com.rae.cnblogs;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LargestLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * 图片加载器
 * Created by ChenRui on 2016/12/3 17:01.
 */
public final class RaeImageLoader {
    private static DisplayImageOptions.Builder sDisplayImageOptions;

    static {
        sDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true);
    }

    public static DisplayImageOptions.Builder defaultOptions() {
        return sDisplayImageOptions;
    }


    public static void initImageLoader(Context context) {

        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(context)
                .threadPoolSize(5)//线程池
                .threadPriority(Thread.NORM_PRIORITY - 2)//线程优先级
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LargestLimitedMemoryCache(2 * 1024 * 1024))//内存缓存
                .memoryCacheSize(2 * 1024 * 1024)//内存缓存大小
                .diskCacheSize(50 * 1024 * 1024)//存储卡缓存大小
                .diskCacheFileCount(100)//存储卡文件个数
                .memoryCacheSizePercentage(13) // default
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // default
                .defaultDisplayImageOptions(defaultOptions().build()) // default
//                .writeDebugLogs()
                .tasksProcessingOrder(QueueProcessingType.FIFO)  //先进先出
                .build();

        ImageLoader.getInstance().init(configuration);
    }
}
