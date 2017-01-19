package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.ILoginPresenter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IUserApi;
import com.rae.cnblogs.sdk.bean.LoginTokenBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

/**
 * Created by ChenRui on 2017/1/19 0019 14:56.
 */
public class LoginPresenterImpl extends BasePresenter<ILoginPresenter.ILoginView> implements ILoginPresenter, ApiUiListener<LoginTokenBean> {

    private IUserApi mUserApi;

    public LoginPresenterImpl(Context context, ILoginView view) {
        super(context, view);
        mUserApi = CnblogsApiFactory.getUserApi(context);
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

        mUserApi.login(userName, pwd, this);
    }

    @Override
    public void start() {

    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoginError(msg);
    }

    @Override
    public void onApiSuccess(LoginTokenBean data) {
        // 获取用户信息
        mUserApi.getUserInfo(new ApiUiListener<UserInfoBean>() {
            @Override
            public void onApiFailed(ApiException ex, String msg) {
                mView.onLoginError(msg);
            }

            @Override
            public void onApiSuccess(UserInfoBean data) {
                mView.onLoginSuccess(data);
            }
        });
    }
}
