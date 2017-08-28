package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.bean.AdvertBean;
import com.rae.cnblogs.sdk.bean.VersionInfo;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 个人接口
 * Created by ChenRui on 2016/12/22 22:57.
 */
public interface IRaeServerApi {

    @GET(ApiUrls.RAE_API_AD_LAUNCHER)
    Observable<AdvertBean> getLauncherAd();


    /**
     * @param versionCode 版本代码
     * @param channel     渠道
     */
    @GET(ApiUrls.RAE_API_CHECK_VERSION)
    Observable<VersionInfo> versionInfo(@Path("versionCode") int versionCode, @Query("channel") String channel);
}
