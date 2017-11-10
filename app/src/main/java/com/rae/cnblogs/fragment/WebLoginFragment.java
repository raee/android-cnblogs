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
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.sdk.CnblogsApiException;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.widget.LoginPlaceholderView;
import com.rae.cnblogs.widget.webclient.RaeWebViewClient;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * 网页登录
 * Created by ChenRui on 2017/2/3 0003 12:01.
 */
public class WebLoginFragment extends WebViewFragment {

    private String mBlogApp;

    public static WebLoginFragment newInstance(String url) {
        Bundle args = new Bundle();
        args.putString("url", url);
        WebLoginFragment fragment = new WebLoginFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private IUserApi mUserApi;

    private LoginPlaceholderView mPlaceholderView;

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
        mPlaceholderView = new LoginPlaceholderView(view.getContext());
        mPlaceholderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mPlaceholderView.dismiss();
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlaceholderView.isRouteLogin()) {
                    mPlaceholderView.dismiss();
                    // 重新登录
                    mWebView.loadUrl(getRawUrl());

                    // 统计失败
                    AppMobclickAgent.onClickEvent(v.getContext(), "WEB_RETRY_LOGIN");

                    CrashReport.postCatchedException(new CnblogsApiException("重试登录失败，当前路径：" + mWebView.getUrl()));

                    return;
                }

                // 统计登录超时
                AppMobclickAgent.onClickEvent(v.getContext(), "WEB_LOGIN_TIMEOUT");

                mPlaceholderView.loadingWithTimer(v.getContext().getString(R.string.login_retry));
                mPlaceholderView.dismissLoadingRetry();
                mWebView.reload();
            }
        });
        parent.addView(mPlaceholderView);
    }

    @Override
    public void onDestroy() {
        RxObservable.dispose("user");
        super.onDestroy();
    }


    @Override
    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar, mAppLayout) {

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!TextUtils.isEmpty(url) && url.contains("home.cnblogs.com")) {
                    mPlaceholderView.loadingWithTimer(getString(R.string.loading_web_user_info));
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookie = CookieManager.getInstance().getCookie(url);

                // 登录成功
                if (cookie != null && cookie.contains(".CNBlogsCookie")) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                    getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }


        };
    }
}
