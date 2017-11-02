package com.rae.cnblogs.sdk.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * 接口配置项
 * Created by ChenRui on 2017/1/14 01:53.
 */
public class CnblogSdkConfig {

    // 访问密钥 clientID:clientSecret
    // cdfb6ec8-e78d-4c70-82df-7b1651a98808:TFgkwiEVDBGZwPncPL9b5a9_z7E2pipUPFHo9OWIeOXkGTImxr_-LQBMw9_gQLX94Faqkbs9VbL_CKk-
//    public static final String API_AUTH_KEY = "Y2RmYjZlYzgtZTc4ZC00YzcwLTgyZGYtN2IxNjUxYTk4ODA4OlRGZ2t3aUVWREJHWndQbmNQTDliNWE5X3o3RTJwaXBVUEZIbzlPV0llT1hrR1RJbXhyXy1MUUJNdzlfZ1FMWDk0RmFxa2JzOVZiTF9DS2st";
//    public static final String API_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp0wHYbg/NOPO3nzMD3dndwS0MccuMeXCHgVlGOoYyFwLdS24Im2e7YyhB0wrUsyYf0/nhzCzBK8ZC9eCWqd0aHbdgOQT6CuFQBMjbyGYvlVYU2ZP7kG9Ft6YV6oc9ambuO7nPZh+bvXH0zDKfi02prknrScAKC0XhadTHT3Al0QIDAQAB";
    public static final byte[] API_PUB_KEY_BYTE = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp0wHYbg/NOPO3nzMD3dndwS0MccuMeXCHgVlGOoYyFwLdS24Im2e7YyhB0wrUsyYf0/nhzCzBK8ZC9eCWqd0aHbdgOQT6CuFQBMjbyGYvlVYU2ZP7kG9Ft6YV6oc9ambuO7nPZh+bvXH0zDKfi02prknrScAKC0XhadTHT3Al0QIDAQAB".getBytes();


    private static CnblogSdkConfig sInstance;
    private Gson mGson = new Gson();

    public static CnblogSdkConfig getsInstance(Context applicationContext) {
        if (sInstance == null) {
            sInstance = new CnblogSdkConfig(applicationContext.getApplicationContext());
        }
        return sInstance;
    }

    private SharedPreferences mConfig;

    private CnblogSdkConfig(Context context) {
        mConfig = context.getApplicationContext().getSharedPreferences("CNBLOG_SDK_CONFIG", Context.MODE_PRIVATE);
    }

    /**
     * 清除所有的配置项目
     */
    public void clear() {
        mConfig.edit().clear().apply();
    }

    /**
     * 清除所有的配置项目
     */
    public void clearUserInfo() {
        mConfig.edit()
                .remove("UserInfo")
                .apply();
    }

    /**
     * 保存用户信息
     *
     * @param userInfo 用户信息
     */
    public void saveUserInfo(UserInfoBean userInfo) {
        if (userInfo != null)
            mConfig.edit().putString("UserInfo", mGson.toJson(userInfo)).apply();
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public UserInfoBean getUserInfo() {
        String json = mConfig.getString("UserInfo", null);
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return mGson.fromJson(json, UserInfoBean.class);
    }


    /**
     * 指引-评论缓存提示
     */
    public boolean hasCommentGuide() {
        return mConfig.getBoolean("hasCommentGuide", false);
    }

    /**
     * 指引-评论缓存提示
     */
    public void commentGuide() {
        mConfig.edit().putBoolean("hasCommentGuide", true).apply();
    }

    /**
     * 指引-点赞缓存提示
     */
    public boolean hasLikeGuide() {
        return mConfig.getBoolean("hasLikeGuide", false);
    }

    /**
     * 指引-点赞缓存提示
     */
    public void likeGuide() {
        mConfig.edit().putBoolean("hasLikeGuide", true).apply();
    }

    /**
     * 指引-登录提示
     */
    public boolean hasLoginGuide() {
        return mConfig.getBoolean("hasLoginGuide", false);
    }

    /**
     * 指引-登录提示
     */
    public void loginGuide() {
        mConfig.edit().putBoolean("hasLoginGuide", true).apply();
    }


    /**
     * 记录首页退出时间
     */
    public long getMainExitTimeMillis() {
        return mConfig.getLong("MainExitTimeMillis", 0);
    }

    public void setMainExitTimeMillis(long value) {
        mConfig.edit().putLong("MainExitTimeMillis", value).apply();
    }

    /**
     * 设置消息数量
     */
    public void setMessageCount(int count) {
        mConfig.edit().putInt("messageCount", count).apply();
    }

    /**
     * 获取消息数量
     */
    public int getMessageCount() {
        return mConfig.getInt("messageCount", 0);
    }

    /**
     * 获取字体大小，注意判断是否为0，单位：PX
     */
    public int getPageTextSize() {
        return mConfig.getInt("PageTextSize", 0);
    }

    /**
     * 设置字体大小，单位：PX
     */
    public void setPageTextSize(int size) {
        mConfig.edit().putInt("PageTextSize", size).apply();
    }

    /**
     * 发布动态提示信息
     */
    public boolean getPostMomentInProgressTips() {
        return mConfig.getBoolean("PostMomentInProgressTips", false);
    }

    /**
     * 发布动态提示信息
     */
    public void setPostMomentInProgressTips(boolean value) {
        mConfig.edit().putBoolean("PostMomentInProgressTips", value).apply();
    }
}
