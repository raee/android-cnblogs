package com.rae.cnblogs.sdk.impl;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.android.volley.Request;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.cnblogs.sdk.parser.CnblogApiResponse;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.RaeBaseApi;
import com.rae.core.sdk.net.ApiRequest;
import com.rae.core.sdk.net.IApiJsonResponse;

import java.util.HashMap;

/**
 * 请求父类,所有接口请求都继承该类
 * Created by ChenRui on 2016/11/30 00:00.
 */
class CnblogsBaseApi extends RaeBaseApi {

    boolean mShouldCache = true;

    private UserProvider mUserProvider;


    public CnblogsBaseApi(Context context) {
        super(context);
        mUserProvider = UserProvider.getInstance();
    }

    UserProvider user() {
        return mUserProvider;
    }

    /**
     * 获取接口配置文件
     */
    CnblogSdkConfig config() {
        return CnblogSdkConfig.getsInstance(mContext.getApplicationContext());
    }

    @Override
    protected boolean enablePerCache(String url, HashMap<String, String> params) {

        // 不启用缓存
        if (!mShouldCache) {
            return false;
        }

        return super.enablePerCache(url, params);
    }

    /**
     * DELETE 请求
     *
     * @param url    路径
     * @param params 参数
     */
    protected ApiRequest.Builder newDelRequestBuilder(String url, HashMap<String, String> params) {
        ApiRequest.Builder builder = newApiRequestBuilder(url, params);
        builder.method(Request.Method.DELETE);
        return builder;
    }

    @Override
    protected <T> IApiJsonResponse getDefaultJsonResponse(Class<T> cls, ApiUiListener<T> listener) {
        return new CnblogApiResponse<>(cls, listener);
    }

    @Override
    protected <T> IApiJsonResponse getDefaultListJsonResponse(Class<T> cls, ApiUiArrayListener<T> listener) {
        return new CnblogApiResponse<>(cls, listener);
    }

    @Override
    protected ApiRequest.Builder newApiRequestBuilder(String url, HashMap<String, String> params) {
        ApiRequest.Builder builder = super.newApiRequestBuilder(url, params);
//        // 添加官方接口授权信息
//        LoginTokenBean loginToken = config().getLoginToken();
//        if (loginToken != null) {
//            builder.addHeader("authorization", String.format("Bearer %s", loginToken.getAccess_token()));
//        }

        // 添加用户信息
        UserInfoBean userInfo = config().getUserInfo();
        if (userInfo != null) {
            builder.addHeader("blogApp", userInfo.getBlogApp());
        }

        return builder;
    }

    protected ApiRequest postWithJsonBody(String url, HashMap<String, String> params, IApiJsonResponse response) {
        ApiRequest req = newApiRequestBuilder(url, params).postMethod().contentType("application/json; charset=UTF-8").listener(response).build();
        sendRequest(req);
        return req;
    }

    protected ApiRequest xmlHttpRequestWithJsonBody(String url, HashMap<String, String> params, IApiJsonResponse response) {
        ApiRequest req = newApiRequestBuilder(url, params).postMethod().contentType("application/json; charset=UTF-8").addHeader("X-Requested-With", "XMLHttpRequest").listener(response).build();
        sendRequest(req);
        return req;
    }


    @Override
    protected void sendRequest(ApiRequest request) {

        // 设置标志，用于取消请求
        request.setTag("CNBLOGS_API_REQUEST");

        // 添加cookie 信息
        String cookie = CookieManager.getInstance().getCookie("http://www.cnblogs.com");
        if (!TextUtils.isEmpty(cookie)) {
            request.getHeaders().put("Cookie", cookie);
        }
        super.sendRequest(request);
    }

    public boolean isLogin() {
        return user().isLogin();
    }

    boolean isNotLogin() {
        return !isLogin();
    }

    public void setShouldCache(boolean shouldCache) {
        mShouldCache = shouldCache;
    }

}
