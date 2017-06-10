package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.api.ISearchApi;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.converter.ConverterFactory;
import com.rae.cnblogs.sdk.db.DbCnblogs;
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
        OkHttpClient client = new OkHttpExtBuilder()
                .cache(context)
                .debug("CNBLOGS-API")
                .https()
                .cookie()
                .build()
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(RequestInterceptor.create())
                .retryOnConnectionFailure(true)
                .build();

        mRetrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://www.cnblogs.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ConverterFactory.create())
                .build();

        DbCnblogs.init(context);

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
     * 取消所有请求
     */
    public abstract void cancel();
}
