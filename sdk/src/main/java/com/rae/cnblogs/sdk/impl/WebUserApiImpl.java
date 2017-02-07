package com.rae.cnblogs.sdk.impl;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.CookieManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.rae.cnblogs.sdk.IUserApi;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.Utils;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.cnblogs.sdk.parser.UserInfoParser;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;
import com.rae.core.sdk.exception.ApiHttpException;
import com.rae.core.sdk.net.IApiJsonResponse;
import com.rae.core.security.X509RSAEncrypt;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;

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
    protected boolean shouldCache(String url, HashMap<String, String> params) {
        // 该接口都不缓存
        return false;
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
                                    String cookie = CookieManager.getInstance().getCookie(ApiUrls.API_SIGN_IN);
                                    boolean hasCookie = !TextUtils.isEmpty(cookie) && cookie.contains("CNBlogsCookie");
                                    if (hasCookie && obj.containsKey("success") && obj.getBoolean("success")) {
                                        listener.onApiSuccess(null);
                                    } else {
                                        listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_SERVER), message);
                                    }
                                } catch (JSONException e) {
                                    listener.onApiFailed(new ApiException(e), ApiErrorCode.ERROR_JSON_PARSE.getMessage());
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
    public void refreshLoginToken(ApiUiListener<LoginTokenBean> listener) {
    }

    @Override
    public void getUserInfo(final ApiUiListener<UserInfoBean> listener) {
        if (isNotLogin()) {
            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
            return;
        }

        xmlHttpRequestWithJsonBody(ApiUrls.API_USER_INFO, null, new IApiJsonResponse() {
            @Override
            public void onJsonResponse(String json) {
                try {
                    UserInfoBean u = new UserInfoBean();
                    JSONObject object = JSON.parseObject(json);
                    String src = Jsoup.parse(object.getString("Avatar")).select("img").attr("src");
                    Elements userName = Jsoup.parse(object.getString("Username")).select("a");
                    u.setAvatar(Utils.getUrl(src));
                    u.setBlogApp(userName.attr("href").replace("/u/", "").replace("/", ""));
                    u.setDisplayName(userName.text());

                    // 保存登录信息
                    UserProvider.getInstance().setLoginUserInfo(u);

                    // 获取用户信息
                    getCurrentUserInfo(u.getBlogApp(), listener);

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


    /**
     * 获取当前用户信息
     */
    private void getCurrentUserInfo(String blogApp, final ApiUiListener<UserInfoBean> listener) {
        xmlHttpRequestWithJsonBody(ApiUrls.API_USER_CENTER.replace("@blogApp", blogApp), null, new UserInfoParser(listener) {
            @Override
            protected void onParseUserInfo(UserInfoBean result, Document document) {
                super.onParseUserInfo(result, document);
                // 保存用户信息
                user().setLoginUserInfo(result);
            }
        });
    }

}
