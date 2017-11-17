package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.api.IPostApi;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.converter.ConverterFactory;
import com.rae.cnblogs.sdk.interceptor.RequestInterceptor;
import com.squareup.okhttp3.OkHttpExtBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 接口提供者
 * Created by ChenRui on 2017/1/19 20:41.
 */
public abstract class CnblogsApiProvider {

    protected final Retrofit mRetrofit;
    protected final Context mContext;

    protected CnblogsApiProvider(Context context) {
        mContext = context;
        OkHttpExtBuilder builder = new OkHttpExtBuilder();

        if (BuildConfig.LOG_DEBUG) {
            builder.debug("CNBLOGS-API");
        }

        OkHttpClient client = builder
//                .cache(context, 2)
                .https()
                .cookie()
                .build()
                .connectTimeout(45, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(90, TimeUnit.SECONDS)
                .addInterceptor(RequestInterceptor.create(context))
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://www.cnblogs.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ConverterFactory.create())
                .build();

    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    /**
     * 获取接口版本号
     */
    public abstract int getApiVersion();

    /**
     * 获取博客接口
     */
    public abstract IBlogApi getBlogApi();

    /**
     * 获取博客分类接口
     */
    public abstract ICategoryApi getCategoriesApi();


    /**
     * 获取用户接口
     */
    public abstract IUserApi getUserApi();

    /**
     * 获取收藏接口
     */
    public abstract IBookmarksApi getBookmarksApi();


    /**
     * 获取新闻接口
     */
    public abstract INewsApi getNewsApi();

    /**
     * 获取朋友圈接口
     */
    public abstract IFriendsApi getFriendApi();

    /**
     * 获取搜索接口
     */
    public abstract ISearchApi getSearchApi();

    /**
     * 获取闪存接口
     */
    public abstract IMomentApi getMomentApi();

    /**
     * 获取闪存接口
     */
    public abstract IPostApi getPostApi();

    /**
     * 个人的接口
     */
    public abstract IRaeServerApi getRaeServerApi();


}
