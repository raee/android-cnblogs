package com.rae.cnblogs.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.rae.cnblogs.R;
import com.rae.cnblogs.widget.RaeWebView;
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
    private String mRawUrl;
    private RaeJavaScriptBridge mJavaScriptApi;
    private WebViewClient mRaeWebViewClient;

    public static WebViewFragment newInstance(String url) {

        Bundle args = new Bundle();
        args.putString("url", url);
        WebViewFragment fragment = new WebViewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    // @BindView(R.id.web_view_blog_content)
    WebView mWebView;

    @BindView(R.id.content)
    FrameLayout mContentLayout;

    @BindView(R.id.pb_web_view)
    ProgressBar mProgressBar;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_web;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);


        mWebView = new RaeWebView(getContext().getApplicationContext());
        mWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mContentLayout.addView(mWebView);

        return view;
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface", "JavascriptInterface"})
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mJavaScriptApi = new RaeJavaScriptBridge();
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setDomStorageEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAllowFileAccess(true);

        File cacheDir = getContext().getExternalCacheDir();

        if (cacheDir != null && cacheDir.canRead() && cacheDir.canWrite()) {
            settings.setAppCacheEnabled(true);
            settings.setAppCachePath(cacheDir.getPath());
        }


        mRaeWebViewClient = getWebViewClient();
        mWebView.addJavascriptInterface(getJavascriptApi(), "app");
        mWebView.setWebChromeClient(getWebChromeClient());
        mWebView.setWebViewClient(mRaeWebViewClient);

        if (mWebView != null && mUrl != null) {
            loadUrl(mUrl);
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRawUrl = getArguments().getString("url");
            mUrl = mRawUrl;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mWebView.removeAllViews();
        mWebView.destroy();
        if (mRaeWebViewClient != null && mRaeWebViewClient instanceof RaeWebViewClient) {
            ((RaeWebViewClient) mRaeWebViewClient).destroy();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public String getUrl() {
        return mWebView.getUrl();
    }

    /**
     * 获取网页内容
     *
     * @return
     */
    public String getContent() {
        return mJavaScriptApi.getHtml();
    }

    public WebChromeClient getWebChromeClient() {
        return new RaeWebChromeClient(mProgressBar);
    }

    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar);
    }

    public Object getJavascriptApi() {
        return mJavaScriptApi;
    }

    public void loadUrl(String url) {
        mWebView.loadUrl(url);
    }

}
