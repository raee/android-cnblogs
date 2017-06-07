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
    public static final String API_AUTH_KEY = "Y2RmYjZlYzgtZTc4ZC00YzcwLTgyZGYtN2IxNjUxYTk4ODA4OlRGZ2t3aUVWREJHWndQbmNQTDliNWE5X3o3RTJwaXBVUEZIbzlPV0llT1hrR1RJbXhyXy1MUUJNdzlfZ1FMWDk0RmFxa2JzOVZiTF9DS2st";
    public static final String API_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp0wHYbg/NOPO3nzMD3dndwS0MccuMeXCHgVlGOoYyFwLdS24Im2e7YyhB0wrUsyYf0/nhzCzBK8ZC9eCWqd0aHbdgOQT6CuFQBMjbyGYvlVYU2ZP7kG9Ft6YV6oc9ambuO7nPZh+bvXH0zDKfi02prknrScAKC0XhadTHT3Al0QIDAQAB";
    public static final byte[] API_PUB_KEY_BYTE = API_PUB_KEY.getBytes();


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

//
//    /**
//     * 保存登录TOKEN
//     *
//     * @param token 凭证
//     */
//    public void saveLoginToken(LoginTokenBean token) {
//        if (token == null) return;
//        mConfig.edit().putString("login_token", JSON.toJSONString(token)).apply();
//    }
//
//    /**
//     * 获取登录TOKEN
//     */
//    public LoginTokenBean getLoginToken() {
//        String json = mConfig.getString("login_token", null);
//        if (TextUtils.isEmpty(json)) return null;
//        return JSON.parseObject(json, LoginTokenBean.class);
//    }

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
    }


    /**
     * 获取离线配置
     *
     * @return
     */
    public OfflineConfig getOfflineConfig() {
        String config = mConfig.getString("OfflineConfig", null);
        return config == null ? new OfflineConfig() : mGson.fromJson(config, OfflineConfig.class);// JSON.parseObject(config, OfflineConfig.class);
    }

    /**
     * 保存离线配置
     *
     * @param config
     */
    public void setOfflineConfig(OfflineConfig config) {
        if (config != null) {
            mConfig.edit().putString("OfflineConfig", mGson.toJson(config)).apply(); // JSON.toJSONString(config)
        }
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
}
