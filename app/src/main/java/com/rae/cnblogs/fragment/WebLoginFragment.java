package com.rae.cnblogs.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.webclient.RaeWebViewClient;
import com.tencent.bugly.crashreport.CrashReport;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 网页登录
 * Created by ChenRui on 2017/2/3 0003 12:01.
 */
public class WebLoginFragment extends WebViewFragment {


    /**
     * 网页登录回调
     */
    public interface WebLoginListener {

        /**
         * 当网页加载完毕触发
         */
        void onWebLoadingFinish();

        /**
         * 当验证码发生错误的时候触发
         *
         * @param url 新的验证码图片地址
         */
        void onLoginVerifyCodeError(String url);

        /**
         * 当网页需要验证码的时候触发
         *
         * @param url 验证码图片地址
         */
        void onNeedVerifyCode(String url);

        /**
         * 登录错误时候触发
         *
         * @param msg 错误消息
         */
        void onLoginError(String msg);

        /**
         * 登录中
         *
         * @param msg 消息
         */
        void onLoggingIn(String msg);

        /**
         * 登录成功
         *
         * @param data 用户信息
         */
        void onLoginSuccess(UserInfoBean data);
    }


    private String mBlogApp;

    public static WebLoginFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebLoginFragment fragment = new WebLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private IUserApi mUserApi;

    private PlaceholderView mPlaceholderView;
    private WebLoginListener mWebLoginListener; // 登录回调

    // 网页是否加载完毕
    private boolean mIsLoadFinish;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
        // 清除COOKIE
        CookieSyncManager.createInstance(getContext());
        CookieManager.getInstance().removeAllCookie();
        CookieSyncManager.getInstance().sync();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() instanceof WebLoginListener) {
            mWebLoginListener = (WebLoginListener) getActivity();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ViewGroup parent = (ViewGroup) mContentLayout.getParent();
        mPlaceholderView = new PlaceholderView(view.getContext());
        mPlaceholderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPlaceholderView.dismiss();
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadUserInfo();
            }
        });
        parent.addView(mPlaceholderView);
    }


    @Override
    public void onDestroy() {
        RxObservable.dispose("user");
        super.onDestroy();
    }


    /**
     * 执行登录
     *
     * @param username   用户名
     * @param password   密码
     * @param verifyCode 验证码
     */
    public void performLogin(@NonNull final String username, @NonNull final String password, @Nullable final String verifyCode) {


        Observable.just(1)
                .subscribeOn(Schedulers.newThread())
                .map(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer integer) throws Exception {
                        long startTimeMillis = System.currentTimeMillis();
                        while (!mIsLoadFinish) {
                            // 网页加载超时
                            if ((System.currentTimeMillis() - startTimeMillis) > 15 * 1000)
                                return -1;
                        }

                        return 1;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        if (mWebLoginListener == null) return;
                        if (integer == -1) {
                            mWebLoginListener.onLoginError("网页登录加载超时，请检查网络连接");
                            return;
                        }


                        String js = "javascript:rae.login('@u','@p','@c')"
                                .replace("@u", username)
                                .replace("@p", password)
                                .replace("@c", verifyCode == null ? "" : verifyCode);

                        mWebView.loadUrl(js);
                    }
                });
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {

        String msg = getString(R.string.loading_user_info);
        mPlaceholderView.loading(msg);

        if (mWebLoginListener != null)
            mWebLoginListener.onLoggingIn(msg);

        RxObservable.create(mUserApi.getUserBlogAppInfo(), "user")
                .flatMap(new Function<UserInfoBean, ObservableSource<UserInfoBean>>() {
                    @Override
                    public ObservableSource<UserInfoBean> apply(UserInfoBean userInfoBean) throws Exception {
                        mBlogApp = userInfoBean.getBlogApp();
                        return RxObservable.create((mUserApi.getUserInfo(userInfoBean.getBlogApp())), "user"); // 获取用户信息
                    }
                })
                .subscribe(new ApiDefaultObserver<UserInfoBean>() {
                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        // 统计错误信息
                        AppMobclickAgent.onLoginEvent(getContext(), "WEB-ERROR", false, getLog(e));
                        // 报告错误信息
                        CrashReport.postCatchedException(e);
                    }

                    private String getLog(Throwable e) {
                        if (e == null || e.getMessage() == null) return "没有错误信息";
                        return e.getMessage();
                    }

                    @Override
                    protected void onError(String message) {
                        notifyLoginError(message);
                    }

                    private void notifyLoginError(String message) {
                        mPlaceholderView.retry(message);
                        if (mWebLoginListener != null)
                            mWebLoginListener.onLoginError(message);
                    }

                    @Override
                    protected void accept(UserInfoBean data) {
                        mPlaceholderView.dismiss();
                        if (TextUtils.isEmpty(data.getUserId())) {
                            notifyLoginError("获取用户信息失败，该用户没有用户ID");
                            AppMobclickAgent.onLoginEvent(getContext(), "ERROR", false, "没有获取到用户ID");
                            return;
                        }

                        // 如果blogApp为空则重新设置，这里的blogApp一定不为空了
                        if (TextUtils.isEmpty(data.getBlogApp())) {
                            data.setBlogApp(mBlogApp);
                        }

                        UserProvider.getInstance().setLoginUserInfo(data);
                        AppUI.success(getContext(), R.string.login_success);

                        //  通知成功
                        if (mWebLoginListener != null) {
                            mWebLoginListener.onLoginSuccess(data);
                        }

                        getActivity().setResult(Activity.RESULT_OK);
                        getActivity().finish();
                    }
                });
    }

    @Override
    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar, mAppLayout) {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mIsLoadFinish = false;
                if (!TextUtils.isEmpty(url) && url.contains("home.cnblogs.com")) {
                    mPlaceholderView.loading(getString(R.string.loading_user_info));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                injectJavascriptFromAssets(view, "js/rae-login.js");
                mIsLoadFinish = true;
                String cookie = CookieManager.getInstance().getCookie(url);

                // 登录成功
                if (cookie != null && cookie.contains(".CNBlogsCookie")) {
                    // 同步COOKIE
                    UserProvider.getInstance().syncFormWebview();
                    loadUserInfo();
                }
            }

        };
    }
}
