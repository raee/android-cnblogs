package com.rae.cnblogs.sdk;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.swift.session.SessionManager;

import java.net.CookieHandler;

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
    private final Context mContext;
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
        mContext = context;
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
        CookieSyncManager.createInstance(mContext);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
        CookieSyncManager.getInstance().sync();

        CookieHandler cookieHandler = java.net.CookieManager.getDefault();
        if (cookieHandler instanceof java.net.CookieManager) {
            ((java.net.CookieManager) cookieHandler).getCookieStore().removeAll();
        }

        // 清除保存的登录信息
        mConfig.clearUserInfo();

    }


//    // TODO:调试登录
//    public void debugLogin() {
//        UserInfoBean userInfo = new UserInfoBean();
//        userInfo.setAvatar("http://pic.cnblogs.com/avatar/446312/20170124105915.png");
//        userInfo.setBlogApp("chenrui7");
//        userInfo.setDisplayName("RAE");
//        userInfo.setUserId("fdeed5f3-11fb-e111-aa3f-842b2b196315");
//        URI url = URI.create("http://www.cnblogs.com");
//        List<String> cookieStrings = new ArrayList<>();
//        cookieStrings.add(".CNBlogsCookie=A6D444EE9778C74F6AA5CEAC14D066BBE664E14112B3CDC72E293E3DF655EDC5D1603941C7A6E05110E4CB740A199738060238CAB3352A36093C234E856EBBBED3355479F26DDB2979995C1F5E78B0F24FE7D53E4; domain=.cnblogs.com; path=/; HttpOnly");
//        cookieStrings.add(".Cnblogs.AspNetCore.Cookies=ACfDJ8PhlBN8IFxtHhqIV3s0LCDkWF14xMqaw7HoJdr52uJMVeiBBOToAnOhpBLr2zDHJflPhXpcTrPY0PKSuCJPTpwIElSqNHmY_pffF-jZRP1YIHwseg2-4sRqXo123HL7BO5ZaNmUc9gl4iKD457TENHN00yZ-Ysue7bBsZ9QhYWe32unM8N3U3KENIVoYbCgLqJUQMauRuyjaPPZkU3BRkdT5x03JaDhjOUuXrHsGA6PFv7ovsxw0ktuYRuBztxgWQXBthK8jsZFR7ONdumCmvGyNehgGr1W_yerajtCqCWd9jjRO4ajllcwU1A7M74tNnw; domain=.cnblogs.com; path=/; HttpOnly");
//
//
//        Map<String, List<String>> map = Collections.singletonMap("Set-Cookie", cookieStrings);
//        CookieHandler cookieManager = java.net.CookieManager.getDefault();
//        try {
//            cookieManager.put(url, map);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        UserProvider.getInstance().setLoginUserInfo(userInfo);
//    }
}
