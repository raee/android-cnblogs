package com.rae.cnblogs.sdk.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;

/**
 * 接口配置项
 * Created by ChenRui on 2017/1/14 01:53.
 */
public class CnblogSdkConfig {

    // 访问密钥 clientID:clientSecret
    // cdfb6ec8-e78d-4c70-82df-7b1651a98808:TFgkwiEVDBGZwPncPL9b5a9_z7E2pipUPFHo9OWIeOXkGTImxr_-LQBMw9_gQLX94Faqkbs9VbL_CKk-
    public static final String API_AUTH_KEY = "Y2RmYjZlYzgtZTc4ZC00YzcwLTgyZGYtN2IxNjUxYTk4ODA4OlRGZ2t3aUVWREJHWndQbmNQTDliNWE5X3o3RTJwaXBVUEZIbzlPV0llT1hrR1RJbXhyXy1MUUJNdzlfZ1FMWDk0RmFxa2JzOVZiTF9DS2st";
    public static final String API_PUB_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCp0wHYbg/NOPO3nzMD3dndwS0MccuMeXCHgVlGOoYyFwLdS24Im2e7YyhB0wrUsyYf0/nhzCzBK8ZC9eCWqd0aHbdgOQT6CuFQBMjbyGYvlVYU2ZP7kG9Ft6YV6oc9ambuO7nPZh+bvXH0zDKfi02prknrScAKC0XhadTHT3Al0QIDAQAB";


    private static CnblogSdkConfig sInstance;

    public static CnblogSdkConfig getsInstance(Context applicationContext) {
        if (sInstance == null) {
            sInstance = new CnblogSdkConfig(applicationContext);
        }
        return sInstance;
    }

    private SharedPreferences mConfig;

    private CnblogSdkConfig(Context context) {
        mConfig = context.getApplicationContext().getSharedPreferences("CNBLOG_SDK_CONFIG", Context.MODE_PRIVATE);
    }


    /**
     * 保存登录TOKEN
     *
     * @param token 凭证
     */
    public void saveLoginToken(LoginTokenBean token) {
        if (token == null) return;
        mConfig.edit().putString("login_token", JSON.toJSONString(token)).apply();
    }

    /**
     * 获取登录TOKEN
     */
    public LoginTokenBean getLoginToken() {
        String json = mConfig.getString("login_token", null);
        if (TextUtils.isEmpty(json)) return null;
        return JSON.parseObject(json, LoginTokenBean.class);
    }
}
