package com.rae.cnblogs.sdk.impl;

import android.content.Context;
import android.util.Log;

import com.rae.cnblogs.sdk.IUserApi;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.config.CnblogSdkConfig;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.net.ApiRequest;
import com.rae.core.sdk.net.IApiJsonResponse;
import com.rae.core.security.X509RSAEncrypt;

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
    public void login(String userName, String password, ApiUiListener<LoginTokenBean> listener) {
        String url = "https://passport.cnblogs.com/user/signin";
        ApiRequest req = newApiRequestBuilder(url,
                newParams()
                        .add("input1", mRSAEncrypt.publicKeyEncrypt(userName))
                        .add("input2", mRSAEncrypt.publicKeyEncrypt(password)))
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("Content-Type", "application/json; charset=UTF-8")
                .postMethod()
                .asJsonBody()
                .listener(new IApiJsonResponse() {
                    @Override
                    public void onJsonResponse(String s) {
                        Log.d("api", s);
                    }

                    @Override
                    public void onJsonResponseError(int i, Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }).build();

        sendRequest(req);
    }

    @Override
    public boolean isLogin() {
        return false;
    }

    @Override
    public void refreshLoginToken(ApiUiListener<LoginTokenBean> listener) {

    }

    @Override
    public void getUserInfo(ApiUiListener<UserInfoBean> listener) {

    }
}
