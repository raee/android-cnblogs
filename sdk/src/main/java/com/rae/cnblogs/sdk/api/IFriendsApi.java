//package com.rae.cnblogs.sdk.api;
//
//import com.rae.cnblogs.sdk.bean.BlogBean;
//import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
//import com.rae.cnblogs.sdk.bean.UserFeedBean;
//import com.rae.cnblogs.sdk.bean.UserInfoBean;
//import com.rae.core.sdk.ApiUiArrayListener;
//import com.rae.core.sdk.ApiUiListener;
//
///**
// * 朋友圈/社交圈接口
// * Created by ChenRui on 2017/2/7 0007 15:24.
// */
//public interface IFriendsApi {
//
//    /**
//     * 获取博主的博客列表
//     *
//     * @param page    页码
//     * @param blogApp 博主ID
//     */
//    void getBlogList(int page, String blogApp, ApiUiArrayListener<BlogBean> listener);
//
//    /**
//     * 获取关注和粉丝个数
//     */
//    void getFriendsInfo(String blogApp, ApiUiListener<FriendsInfoBean> listener);
//
//
//    /**
//     * 获取用户动态
//     *
//     * @param listener
//     */
//    void getFeeds(int page, String blogApp, ApiUiArrayListener<UserFeedBean> listener);
//
//    /**
//     * 关注博主
//     *
//     * @param blogApp 博主
//     */
//    void follow(String blogApp, ApiUiListener<Void> listener);
//
//    /**
//     * 取消关注
//     *
//     * @param userId
//     */
//    void unFollow(String userId, ApiUiListener<Void> listener);
//
//
//    /**
//     * 获取我的关注列表
//     *
//     * @param userId 用户ID，不是blogApp, 当前登录用户可以传空
//     * @param page   页码
//     */
//    void getFollowList(String userId, int page, ApiUiArrayListener<UserInfoBean> listener);
//
//    /**
//     * 获取我的粉丝列表
//     *
//     * @param userId 用户ID，不是blogApp, 当前登录用户可以传空
//     * @param page   页码
//     */
//    void getFansList(String userId, int page, ApiUiArrayListener<UserInfoBean> listener);
//}
