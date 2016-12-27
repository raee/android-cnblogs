package com.rae.cnblogs.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rae.cnblogs.R;
import com.rae.cnblogs.widget.webclient.RaeJavaScriptBridge;
import com.rae.cnblogs.widget.webclient.RaeWebChromeClient;
import com.rae.cnblogs.widget.webclient.RaeWebViewClient;

import java.io.File;

import butterknife.BindView;

/**
 * 网页查看
 * Created by ChenRui on 2016/12/27 23:07.
 */
public class WebViewFragment extends BaseFragment {

    private String mUrl;

    public static WebViewFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.web_view_blog_content)
    WebView mWebView;

    @BindView(R.id.pb_web_view)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_content;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);

        File cacheDir = getContext().getExternalCacheDir();

        if (cacheDir != null && cacheDir.canRead() && cacheDir.canWrite()) {
            settings.setAppCacheEnabled(true);
            settings.setAppCachePath(cacheDir.getPath());
        }
        mWebView.addJavascriptInterface(getJavascriptApi(), "app");
        mWebView.setWebChromeClient(getWebChromeClient());
        mWebView.setWebViewClient(getWebViewClient());

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUrl = getArguments().getString("url");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (mWebView != null && mUrl != null) {
            loadUrl(mUrl);
        }
    }

    public String getUrl() {
        return mWebView.getUrl();
    }

    public WebChromeClient getWebChromeClient() {
        return new RaeWebChromeClient(mProgressBar);
    }

    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar);
    }

    public Object getJavascriptApi() {
        return new RaeJavaScriptBridge();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

}
