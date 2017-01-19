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
    public void login(final String userName, final String password, final ApiUiListener<LoginTokenBean> listener) {
        String url = "https://passport.cnblogs.com/user/signin";

        ApiRequest req = newApiRequestBuilder(url,
                newParams()
                        .add("input1", mRSAEncrypt.publicKeyEncrypt(userName))
                        .add("input2", mRSAEncrypt.publicKeyEncrypt(password)))
                .addHeader("X-Requested-With", "XMLHttpRequest")
//                .addHeader("Accept", "*/*")
//                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.87 Safari/537.36")
                .addHeader("Content-Type", "application/json")
                .postMethod()
                .asJsonBody()
                .listener(new IApiJsonResponse() {
                    @Override
                    public void onJsonResponse(String s) {

                        Log.d("api", s);
                        listener.onApiSuccess(null);
                    }

                    @Override
                    public void onJsonResponseError(int i, Throwable throwable) {
                        throwable.printStackTrace();
                        listener.onApiSuccess(null);
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
