package com.rae.cnblogs.sdk.impl;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rae.cnblogs.sdk.IUserApi;
import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.exception.ApiHttpException;
import com.rae.core.sdk.net.IApiJsonResponse;
import com.rae.core.security.X509RSAEncrypt;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * 网页调用的用户接口
 * Created by ChenRui on 2017/1/15 15:50.
 */
public class WebUserApiImpl extends CnblogsBaseApi implements IUserApi {
    private X509RSAEncrypt mRSAEncrypt;

    public WebUserApiImpl(Context context) {
        super(context);
        mRSAEncrypt = new X509RSAEncrypt(null, CnblogSdkConfig.API_PUB_KEY);
    }

    @Override
    public void login(final String userName, final String password, final @Nullable String verifyCode, final ApiUiListener<LoginTokenBean> listener) {

        // 先调用网页，设置好cookie
        get(ApiUrls.API_SIGN_IN, null, Void.class, new ApiUiListener<Void>() {
            @Override
            public void onApiFailed(ApiException e, String s) {
                listener.onApiFailed(e, s);
            }

            @Override
            public void onApiSuccess(Void aVoid) {
                CookieManager.getInstance().setCookie(ApiUrls.API_SIGN_IN, "AspxAutoDetectCookieSupport=1;path=/;");
                String cookie = CookieManager.getInstance().getCookie(ApiUrls.API_SIGN_IN);
                Log.e("rae", "获取到的cookie = " + cookie);

                // 提交登录参数
                xmlHttpRequestWithJsonBody(
                        ApiUrls.API_SIGN_IN,
                        newParams()
                                .add("input1", mRSAEncrypt.publicKeyEncrypt(userName))
                                .add("input2", mRSAEncrypt.publicKeyEncrypt(password))
                                .add("remember", "true"),
                        new IApiJsonResponse() {
                            @Override
                            public void onJsonResponse(String json) {
                                try {
                                    JSONObject obj = JSON.parseObject(json);
                                    String message = obj.containsKey("message") ? obj.getString("message") : "登录失败，请重试";
                                    if (isLogin() && obj.containsKey("success") && obj.getBoolean("success")) {
                                        listener.onApiSuccess(null);
                                    } else {
                                        listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_SERVER), message);
                                    }
                                } catch (Exception e) {
                                    onJsonResponseError(ApiErrorCode.ERROR_JSON_PARSE.getErrorCode(), e);
                                }
                            }

                            @Override
                            public void onJsonResponseError(int errorCode, Throwable e) {
                                listener.onApiFailed(new ApiHttpException(errorCode, e), ApiErrorCode.ERROR_SERVER.getMessage());
                            }
                        }
                );
            }
        });
    }


    @Override
    public boolean isLogin() {
        // 是否有登录COOKIE
        CookieManager manager = CookieManager.getInstance();
        String cookie = manager.getCookie("http://www.cnblogs.com");
        return !TextUtils.isEmpty(cookie) && cookie.contains(".CNBlogsCookie");
    }

    @Override
    public void refreshLoginToken(ApiUiListener<LoginTokenBean> listener) {

    }

    @Override
    public void getUserInfo(final ApiUiListener<UserInfoBean> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_USER_INFO, null, new IApiJsonResponse() {
            @Override
            public void onJsonResponse(String json) {
                try {
                    UserInfoBean u = new UserInfoBean();
                    JSONObject object = JSON.parseObject(json);
                    String src = Jsoup.parse(object.getString("Avatar")).attr("src");
                    Document userName = Jsoup.parse(object.getString("Username"));
                    u.setAvatar(Utils.getUrl(src));
                    u.setBlogApp(userName.attr("href").replace("/u/", "").replace("/", ""));
                    u.setDisplayName(userName.text());
                    listener.onApiSuccess(u);
                } catch (Exception e) {
                    onJsonResponseError(ApiErrorCode.ERROR_SERVER.getErrorCode(), e);
                }
            }

            @Override
            public void onJsonResponseError(int errorCode, Throwable e) {
                listener.onApiFailed(new ApiHttpException(errorCode, e), ApiErrorCode.ERROR_SERVER.getMessage());
            }
        });
    }

    @Override
    public void logout() {

        // 移除所有的cookie
        CookieManager.getInstance().removeAllCookie();

        // 清除保存的登录信息
        config().clear();
    }
}
