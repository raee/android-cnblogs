package com.rae.cnblogs.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rae.cnblogs.widget.webclient.RaeWebViewClient;

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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 清除COOKIE
        CookieManager.getInstance().removeAllCookie();
    }

    @Override
    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar) {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                String cookie = CookieManager.getInstance().getCookie(url);

                // 登录成功
                if (cookie != null && cookie.contains(".CNBlogsCookie")) {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
        };
    }
}
