package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.ILoginPresenter;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;
import com.umeng.analytics.MobclickAgent;

/**
 * 登录逻辑
 * Created by ChenRui on 2017/1/19 0019 14:56.
 */
public class LoginPresenterImpl extends BasePresenter<ILoginPresenter.ILoginView> implements ILoginPresenter, ApiUiListener<LoginTokenBean> {

    private IUserApi mUserApi;
    private boolean mFromLogin;

    public LoginPresenterImpl(Context context, ILoginView view) {
        super(context, view);
        mUserApi = getApiProvider().getUserApi();
    }

    @Override
    public void login() {
        String userName = mView.getUserName();
        String pwd = mView.getPassword();
        if (isEmpty(userName)) {
            mView.onLoginError(mContext.getString(R.string.error_username_empty));
            return;
        }
        if (isEmpty(pwd)) {
            mView.onLoginError(mContext.getString(R.string.error_password_empty));
            return;
        }

        mFromLogin = true;
        mUserApi.login(userName, pwd, null, this);
    }

    @Override
    public void loadUserInfo() {
        // 获取用户信息
        mUserApi.getUserInfo(new ApiUiListener<UserInfoBean>() {
            @Override
            public void onApiFailed(ApiException ex, String msg) {
                mView.onLoginError(msg);
            }

            @Override
            public void onApiSuccess(UserInfoBean data) {

                // 统计用户
                if (mFromLogin) {
                    MobclickAgent.onProfileSignIn(data.getBlogApp());
                }

                mView.onLoginSuccess(data);
            }
        });
    }

    @Override
    public void start() {
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        if (!TextUtils.isEmpty(msg) && msg.contains("验证码")) {
            // 验证码错误
            mView.onLoginVerifyCodeError();
        } else {
            mView.onLoginError(msg);
        }
    }

    @Override
    public void onApiSuccess(LoginTokenBean data) {
        loadUserInfo();

    }
}
