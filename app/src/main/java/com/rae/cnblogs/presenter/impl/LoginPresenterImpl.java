package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.presenter.ILoginPresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.sdk.utils.ApiEncrypt;
import com.umeng.analytics.MobclickAgent;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 登录逻辑
 * Created by ChenRui on 2017/1/19 0019 14:56.
 */
public class LoginPresenterImpl extends BasePresenter<ILoginPresenter.ILoginView> implements ILoginPresenter {

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
        RxObservable.create(mUserApi.login(ApiEncrypt.encrypt(userName), ApiEncrypt.encrypt(pwd)))
                .flatMap(new Function<Empty, ObservableSource<UserInfoBean>>() {
                    @Override
                    public ObservableSource<UserInfoBean> apply(Empty empty) throws Exception {
                        return RxObservable.create(mUserApi.getUserInfo());
                    }
                })
                .subscribe(new ApiDefaultObserver<UserInfoBean>() {
                    @Override
                    protected void onError(String msg) {
                        if (!TextUtils.isEmpty(msg) && msg.contains("验证码")) {
                            // 验证码错误
                            mView.onLoginVerifyCodeError();
                        } else {
                            mView.onLoginError(msg);
                        }
                    }

                    @Override
                    protected void accept(UserInfoBean data) {
                        // 统计用户
                        if (mFromLogin) {
                            MobclickAgent.onProfileSignIn(data.getBlogApp());
                        }

                        mView.onLoginSuccess(data);
                    }
                });
    }

    @Override
    public void loadUserInfo() {

    }

    @Override
    public void start() {
    }

}
