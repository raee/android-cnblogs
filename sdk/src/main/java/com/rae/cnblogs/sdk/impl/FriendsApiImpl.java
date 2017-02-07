package com.rae.cnblogs.sdk.impl;

import android.content.Context;

import com.rae.cnblogs.sdk.IFriendsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.parser.CnBlogsWebApiResponse;
import com.rae.cnblogs.sdk.parser.FriendsBlogListParser;
import com.rae.cnblogs.sdk.parser.FriendsInfoParser;
import com.rae.cnblogs.sdk.parser.FriendsListParser;
import com.rae.cnblogs.sdk.parser.UserTimelineParser;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;

/**
 * 朋友圈
 * Created by ChenRui on 2017/2/7 0007 15:28.
 */
public class FriendsApiImpl extends CnblogsBaseApi implements IFriendsApi {

    public FriendsApiImpl(Context context) {
        super(context);
    }

    @Override
    public void getBlogList(int page, String blogApp, ApiUiArrayListener<BlogBean> listener) {
        get(ApiUrls.API_FRIENDS_BLOG_LIST.replace("@blogApp", blogApp).replace("@page", String.valueOf(page)), null, new FriendsBlogListParser(listener));
    }

    @Override
    public void getFriendsInfo(String blogApp, ApiUiListener<FriendsInfoBean> listener) {
        get(ApiUrls.API_USER_CENTER.replace("@blogApp", blogApp), null, new FriendsInfoParser(listener));
    }


    @Override
    public void getFeeds(String blogApp, ApiUiArrayListener<UserFeedBean> listener) {
        if (blogApp == null && isLogin()) {
            blogApp = user().getLoginUserInfo().getBlogApp();
        }
        if (blogApp == null) {
            listener.onApiFailed(null, "blog app is null!");
            return;
        }

        get(ApiUrls.API_USER_CENTER.replace("@blogApp", blogApp), null, new UserTimelineParser(listener));

    }

    @Override
    public void follow(String userId, ApiUiListener<Void> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_FRIENDS_FOLLOW, newParams().add("userId", userId).add("remark", ""), new CnBlogsWebApiResponse<>(Void.class, listener));
    }

    public void unFollow(String userId, ApiUiListener<Void> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_FRIENDS_UN_FOLLOW, newParams().add("userId", userId).add("isRemoveGroup", false), new CnBlogsWebApiResponse<>(Void.class, listener));
    }

    private void getFollowList(String userId, int page, boolean isFollowed, ApiUiArrayListener<UserInfoBean> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_FRIENDS_FOLLOW_LIST,
                newParams()
                        .add("uid", userId == null ? user().getLoginUserInfo().getUserId() : userId)
                        .add("groupId", "00000000-0000-0000-0000-000000000000")
                        .add("page", page).add("isFollowes", isFollowed),
                new FriendsListParser(listener));
    }

    @Override
    public void getFollowList(String userId, int page, ApiUiArrayListener<UserInfoBean> listener) {
        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }
        // {"uid":"fdeed5f3-11fb-e111-aa3f-842b2b196315","groupId":"00000000-0000-0000-0000-000000000000","page":1,"isFollowes":true}
        getFollowList(userId, page, true, listener);
    }

    @Override
    public void getFansList(String userId, int page, ApiUiArrayListener<UserInfoBean> listener) {
        getFollowList(userId, page, false, listener);
    }
}
