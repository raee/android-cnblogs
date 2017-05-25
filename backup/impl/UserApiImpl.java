//package com.rae.cnblogs.sdk.impl;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//
//import com.rae.cnblogs.sdk.IUserApi;
//import com.rae.cnblogs.sdk.bean.LoginTokenBean;
//import com.rae.cnblogs.sdk.bean.UserInfoBean;
//import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
//import com.rae.core.sdk.ApiUiListener;
//import com.rae.core.sdk.exception.ApiErrorCode;
//import com.rae.core.sdk.exception.ApiException;
//import com.rae.core.sdk.net.ApiRequest;
//import com.rae.core.security.X509RSAEncrypt;
//
//
///**
// * 用户接口实现
// * Created by ChenRui on 2017/1/14 01:01.
// */
//public class UserApiImpl extends CnblogsBaseApi implements IUserApi {
//
//    private final X509RSAEncrypt mRSAEncrypt;
//
//    public UserApiImpl(Context context) {
//        super(context);
//        mRSAEncrypt = new X509RSAEncrypt(null, CnblogSdkConfig.API_PUB_KEY);
//    }
//
//    /**
//     * TOKEN 请求
//     *
//     * @param type 认证类型
//     */
//    private ApiRequest.Builder newTokenRequestBuild(String type) {
//        return new ApiRequest.Builder(ApiUrls.API_LOGIN_TOKEN)
//                .postMethod()
//                .addHeader("authorization", "Basic " + CnblogSdkConfig.API_AUTH_KEY)
//                .addParams("grant_type", type)
//                .shouldCache(false)
//                .perCacheEnable(false);
//    }
//
//    /**
//     * 创建用户请求
//     *
//     * @param token 登录TOKEN
//     */
//    private ApiRequest.Builder newUserRequestBuild(@NonNull LoginTokenBean token) {
//        return new ApiRequest.Builder(ApiUrls.API_USER_INFO)
//                .addHeader("authorization", String.format("Bearer %s", token.getAccess_token()))
//                .shouldCache(false)
//                .perCacheEnable(false);
//    }
//
//    @Override
//    public boolean isLogin() {
//        LoginTokenBean token = config().getLoginToken();
//        return token != null && !TextUtils.isEmpty(token.getAccess_token());
//    }
//
//    @Override
//    public void login(String userName, String password, @Nullable String verifyCode, final ApiUiListener<LoginTokenBean> listener) {
//        ApiRequest request = newTokenRequestBuild("password")
//                .addParams("username", mRSAEncrypt.publicKeyEncrypt(userName))
//                .addParams("password", mRSAEncrypt.publicKeyEncrypt(password))
//                .listener(getDefaultJsonResponse(LoginTokenBean.class, new ApiUiListener<LoginTokenBean>() {
//                    @Override
//                    public void onApiFailed(ApiException e, String s) {
//                        listener.onApiFailed(e, s);
//                    }
//
//                    @Override
//                    public void onApiSuccess(LoginTokenBean loginTokenBean) {
//                        config().saveLoginToken(loginTokenBean);
//                        listener.onApiSuccess(loginTokenBean);
//                    }
//                }))
//                .build();
//        sendRequest(request);
//    }
//
//    /**
//     * 刷新用户登录TOKEN
//     */
//    @Override
//    public void refreshLoginToken(final ApiUiListener<LoginTokenBean> listener) {
//        if (!isLogin()) {
//            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
//            return;
//        }
//
//        LoginTokenBean loginToken = config().getLoginToken();
//        ApiRequest request = newTokenRequestBuild("refresh_token")
//                .addParams("refresh_token", loginToken.getRefresh_token())
//                .listener(getDefaultJsonResponse(LoginTokenBean.class, new ApiUiListener<LoginTokenBean>() {
//                    @Override
//                    public void onApiFailed(ApiException e, String s) {
//                        listener.onApiFailed(e, s);
//                    }
//
//                    @Override
//                    public void onApiSuccess(LoginTokenBean loginTokenBean) {
//                        config().saveLoginToken(loginTokenBean);
//                        listener.onApiSuccess(loginTokenBean);
//                    }
//                }))
//                .build();
//        sendRequest(request);
//    }
//
//    @Override
//    public void getUserInfo(ApiUiListener<UserInfoBean> listener) {
//        if (!isLogin()) {
//            listener.onApiFailed(new ApiException(ApiErrorCode.ERROR_NOT_LOGIN), ApiErrorCode.ERROR_NOT_LOGIN.getMessage());
//            return;
//        }
//
//        ApiRequest request = newUserRequestBuild(config().getLoginToken())
//                .listener(getDefaultJsonResponse(UserInfoBean.class, listener))
//                .build();
//        sendRequest(request);
//    }
//}
