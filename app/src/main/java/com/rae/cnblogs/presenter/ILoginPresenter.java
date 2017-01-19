package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.UserInfoBean;

/**
 * 登录逻辑
 * Created by ChenRui on 2017/1/19 0019 14:55.
 */
public interface ILoginPresenter extends IRaePresenter {
    void login();

    interface ILoginView {

        String getUserName();

        String getPassword();

        void onLoginSuccess(UserInfoBean userInfo);

        void onLoginError(String message);
    }
}
