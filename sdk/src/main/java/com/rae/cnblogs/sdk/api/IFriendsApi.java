package com.rae.cnblogs.sdk.api;

import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.JsonBody;
import com.rae.cnblogs.sdk.Parser;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.FriendsBlogListParser;
import com.rae.cnblogs.sdk.parser.FriendsInfoParser;
import com.rae.cnblogs.sdk.parser.UserTimelineParser;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * 朋友圈/社交圈接口
 * Created by ChenRui on 2017/2/7 0007 15:24.
 */
public interface IFriendsApi {

    /**
     * 获取博主的博客列表
     *
     * @param page    页码
     * @param blogApp 博主ID
     */
    @GET(ApiUrls.API_FRIENDS_BLOG_LIST)
    @Parser(FriendsBlogListParser.class)
    Observable<List<BlogBean>> getBlogList(@Path("page") int page, @Path("blogApp") String blogApp);

    /**
     * 获取关注和粉丝个数
     */
    @GET(ApiUrls.API_USER_CENTER)
    @Parser(FriendsInfoParser.class)
    Observable<FriendsInfoBean> getFriendsInfo(@Path("blogApp") String blogApp);


    /**
     * 获取用户动态
     */
    @GET(ApiUrls.API_USER_FEED)
    @Parser(UserTimelineParser.class)
    Observable<List<UserFeedBean>> getFeeds(@Path("page") int page, @Path("blogApp") String blogApp);

    /**
     * 关注博主
     *
     * @param userId 博主
     */
    @POST(ApiUrls.API_FRIENDS_FOLLOW)
    @FormUrlEncoded
    @Headers(JsonBody.CONTENT_TYPE)
    Observable<Empty> follow(@Field("userId") String userId);

    /**
     * 取消关注
     *
     * @param userId
     */
    @POST(ApiUrls.API_FRIENDS_UN_FOLLOW)
    @FormUrlEncoded
    @Headers(JsonBody.CONTENT_TYPE)
    Observable<Empty>  unFollow(@Field("userId") String userId);


    /**
     * 获取我的关注列表
     *
     * @param userId 用户ID，不是blogApp, 要获取用户的ID
     * @param page   页码
     */
    @POST(ApiUrls.API_FRIENDS_FOLLOW_LIST)
    @FormUrlEncoded
    @Headers(JsonBody.CONTENT_TYPE)
    Observable<List<UserInfoBean>> getFollowList(@Field("uid") String userId,@Field("page") int page);
//
//    /**
//     * 获取我的粉丝列表
//     *
//     * @param userId 用户ID，不是blogApp, 当前登录用户可以传空
//     * @param page   页码
//     */
//    void getFansList(String userId, int page, ApiUiArrayListener<UserInfoBean> listener);
}
