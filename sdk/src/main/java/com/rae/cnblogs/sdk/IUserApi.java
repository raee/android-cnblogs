package com.rae.cnblogs.sdk;

import android.support.annotation.Nullable;

import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiListener;

/**
 * 用户接口，用户管理对应{@link UserProvider} 类
 * Created by ChenRui on 2017/1/14 01:00.
 */
public interface IUserApi {
    /**
     * 登录
     *
     * @param userName   用户名
     * @param password   密码
     * @param verifyCode 验证码，可为空
     */
    void login(String userName, String password, @Nullable String verifyCode, ApiUiListener<LoginTokenBean> listener);

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
     * 获取用户信息，不需再保存登录信息了。你可以通过{@link UserProvider} 来管理用户
     */
    void getUserInfo(ApiUiListener<UserInfoBean> listener);

}
