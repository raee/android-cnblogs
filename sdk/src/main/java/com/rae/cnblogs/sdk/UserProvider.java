package com.rae.cnblogs.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.swift.session.SessionManager;

import io.reactivex.annotations.Nullable;

/**
 * 用户提供者，保存用户信息，对已登录的用户进行操作。
 * <pre>
 * 使用前请先调用{@link #init(Context)}方法进行初始化
 * </pre>
 * Created by ChenRui on 2017/2/3 0003 15:58.
 */
public final class UserProvider {

    private static UserProvider sUserProvider;
    private UserInfoBean mUserInfo;
    private CnblogSdkConfig mConfig;

    public static UserProvider getInstance() {
        if (sUserProvider == null) {
            throw new NullPointerException("用户提供程序没有初始化！");
        }
        return sUserProvider;
    }

    public static void init(Context applicationContext) {
        if (sUserProvider == null) {
            sUserProvider = new UserProvider(applicationContext);
        }
    }

    public UserProvider(Context context) {
        mConfig = CnblogSdkConfig.getsInstance(context);
    }


    /**
     * 保存当前用户信息，一般在登录的时候调用该方法
     *
     * @param userInfo 用户信息
     */
    public void setLoginUserInfo(UserInfoBean userInfo) {
        this.mUserInfo = userInfo;
        // 保存到偏好中去
        if (userInfo != null) {
            mConfig.saveUserInfo(userInfo);
        }
    }

    /**
     * 获取当前登录用户信息
     *
     * @return 用户信息视图
     */
    @Nullable
    public UserInfoBean getLoginUserInfo() {
        // 如果用户为空，从偏好中获取
        if (mUserInfo == null) {
            mUserInfo = mConfig.getUserInfo();
        }
        return mUserInfo;
    }

    /**
     * 是否登录
     */
    public boolean isLogin() {
        UserInfoBean info = getLoginUserInfo();
        return info != null && !TextUtils.isEmpty(info.getUserId());
    }

    /**
     * 退出登录，释放资源
     */
    public void logout() {
        mUserInfo = null;

        SessionManager.getDefault().clear(); // 清除用户信息

        // 移除所有的cookie
        CookieManager.getInstance().removeAllCookie();

        // 清除保存的登录信息
        mConfig.clearUserInfo();

    }

    // TODO:调试登录
    public void debugLogin() {
        UserInfoBean userInfo = new UserInfoBean();
        userInfo.setAvatar("http://pic.cnblogs.com/avatar/446312/20170124105915.png");
        userInfo.setBlogApp("chenrui7");
        userInfo.setDisplayName("RAE");
        userInfo.setUserId("fdeed5f3-11fb-e111-aa3f-842b2b196315");
        String url = "www.cnblogs.com";
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        cookieManager.setCookie(url, ".CNBlogsCookie=C306D7D18BD1D816E4B20C87DD46D505A31D4B6783A89222D18774BDDCD934BEBE2994CAC5C3DB9BE9CB12B654CD9598417DAF4D5A7512425762DE872974EE0A80916EDAD8D45C7D9C44E2834773DBC87700B8A2; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.setCookie(url, ".Cnblogs.AspNetCore.Cookies=CfDJ8PhlBN8IFxtHhqIV3s0LCDkQKg5ot40UkHy5gGP_E6YuYQit2qSrbqu2_cNJqMFW2F2XbFgCUxnHBGKE_mpb-YZYETu5VFEU0CaBIzqrgmzgC_o9bvgtInaBXnsMTY-wkmQqW1Jp9pazhzDlsFXmYgPwDIueh8aiEkWbTFiK2DX9tHUdmfW89aezOAL6bSpE7DEfxb09zspl6xEYhlDZBiuVqu1pwj8nwg1JyxNLXBEeF2BVtbRjYDQfWviZfSWME6gUSw39OwULDJVLxSAHgTQDXJa6_qUlA9CNeOp4vAD3; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.setCookie(url, "_ga=GA1.2.1156272232.1485224862; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.setCookie(url, "_gid=GA1.2.1870524709.1500184527; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.flush();
        UserProvider.getInstance().setLoginUserInfo(userInfo);
    }
}
