package com.rae.cnblogs.fragment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.umeng.analytics.MobclickAgent;

import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * 网页登录
 * Created by ChenRui on 2017/2/3 0003 12:01.
 */
public class WebLoginFragment extends WebViewFragment {

    public static WebLoginFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebLoginFragment fragment = new WebLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private IUserApi mUserApi;

    private PlaceholderView mPlaceholderView;

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

    private void login() {
        // 同步COOKIE
        UserProvider.getInstance().syncFormWebview();

        // 获取用户信息
        loadUserInfo();
    }

    private void loadUserInfo() {
        mPlaceholderView.loading(getString(R.string.loading_user_info));
        RxObservable.create(mUserApi.getUserBlogAppInfo(), "user")
                .flatMap(new Function<UserInfoBean, ObservableSource<UserInfoBean>>() {
                    @Override
                    public ObservableSource<UserInfoBean> apply(UserInfoBean userInfoBean) throws Exception {
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
                        MobclickAgent.reportError(getContext(), e);
                    }

                    private String getLog(Throwable e) {
                        if (e == null || e.getMessage() == null) return "没有错误信息";
                        return e.getMessage();
                    }

                    @Override
                    protected void onError(String message) {
                        mPlaceholderView.retry(message);
                    }

                    @Override
                    protected void accept(UserInfoBean userInfoBean) {
                        mPlaceholderView.dismiss();
                        UserProvider.getInstance().setLoginUserInfo(userInfoBean);
                        AppUI.success(getContext(), R.string.login_success);
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
                if (!TextUtils.isEmpty(url) && url.contains("home.cnblogs.com")) {
                    mPlaceholderView.loading(getString(R.string.loading_user_info));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookie = CookieManager.getInstance().getCookie(url);

                // 登录成功
                if (cookie != null && cookie.contains(".CNBlogsCookie")) {
                    login();
                }
            }


        };
    }
}
