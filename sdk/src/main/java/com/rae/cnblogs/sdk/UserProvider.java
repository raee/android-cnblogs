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
        cookieManager.setCookie(url, ".CNBlogsCookie=D87FFA7E12173CC051241037F901A795EB794E22620528D1E1E878BB2268BB532FEFE56D4ED695C7C91755730F641EA6223708544BFABBAFA5BD107848E8EE3A75952285118A93EF3014C8DEEE242CE6A1111A4B; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.setCookie(url, ".Cnblogs.AspNetCore.Cookies=CfDJ8PhlBN8IFxtHhqIV3s0LCDngiZhPetC8nTWqWIpm_YV4toKRrGPfBedwKqrr-7t6qJ6iE2d0BR2VG6059Dzuebd7xCyl2BYU-dpfPrqYgx-v5lqY62Qn_sToxAtvp6RX1TbJn1OlKwgkwjeaHL6iUZFnlaynqJQk2C4L1Qha-bnQkMA3XVdLL54DwtLHpOUYd2PjwLBDQxTxjVkQgEzvwmcQvXlW9zs51U-w5trAQ6aTiHELwuYR3avr1gE9n61chtmnBkMGaigR4p6d8UiAT5oO8tLe28RGIm9Tj5wI70q8WOCRJEm2cPLPgxhpoTdDEQ; domain=.cnblogs.com; path=/; HttpOnly");
        cookieManager.flush();
        UserProvider.getInstance().setLoginUserInfo(userInfo);
    }
}
