package com.rae.cnblogs.sdk;

import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiListener;

/**
 * 用户接口
 * Created by ChenRui on 2017/1/14 01:00.
 */
public interface IUserApi {
    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     */
    void login(String userName, String password, ApiUiListener<LoginTokenBean> listener);

    /**
     * 是否已经登录
     */
    boolean isLogin();

    /**
     * 刷新登录TOKEN
     * 前提是一定是处于登录状态通过refreshToken去刷新
     */
    void refreshLoginToken(final ApiUiListener<LoginTokenBean> listener);

    /**
     * 获取用户信息
     */
    void getUserInfo(ApiUiListener<UserInfoBean> listener);
}
