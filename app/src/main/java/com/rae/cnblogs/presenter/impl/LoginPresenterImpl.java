package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.ILoginPresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.UserProvider;
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

        // 清除cookie
        UserProvider.getInstance().logout();

        mFromLogin = true;
        createObservable(mUserApi.login(ApiEncrypt.encrypt(userName), ApiEncrypt.encrypt(pwd)))
                .flatMap(new Function<Empty, ObservableSource<UserInfoBean>>() {
                    @Override
                    public ObservableSource<UserInfoBean> apply(Empty empty) throws Exception {
                        return createObservable(mUserApi.getUserBlogAppInfo()); // 获取BlogApp信息
                    }
                })
                .flatMap(new Function<UserInfoBean, ObservableSource<UserInfoBean>>() {
                    @Override
                    public ObservableSource<UserInfoBean> apply(UserInfoBean userInfoBean) throws Exception {
                        return createObservable(mUserApi.getUserInfo(userInfoBean.getBlogApp())); // 获取用户信息
                    }
                })
                .subscribe(new ApiDefaultObserver<UserInfoBean>() {
                    @Override
                    public void onError(Throwable e) {
                        // 统计错误信息
                        AppMobclickAgent.onLoginEvent(mApplicationContext, "ERROR", false, e == null ? "没有错误信息" : Log.getStackTraceString(e));
                        super.onError(e);
                    }

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
                        // 统计登录事件
                        AppMobclickAgent.onLoginEvent(mApplicationContext, data.getBlogApp(), true, null);
                        // 友盟统计用户
                        if (mFromLogin) {
                            MobclickAgent.onProfileSignIn(data.getBlogApp());
                        }

                        // [重要] 同步Cookie登录信息
                        UserProvider.getInstance().syncFormCookieJar();

                        mView.onLoginSuccess(data);
                    }
                });
    }

    @Override
    public void loadUserInfo() {

    }

    @Override
    public void cancel() {
        cancelRequest();
    }

    @Override
    public void start() {
    }

}
