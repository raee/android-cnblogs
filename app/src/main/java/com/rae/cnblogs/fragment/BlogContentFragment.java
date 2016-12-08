package com.rae.cnblogs.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.bean.Blog;

import java.io.File;

import butterknife.BindView;


/**
 * 博文内容
 * Created by ChenRui on 2016/12/6 23:39.
 */
public class BlogContentFragment extends BaseFragment implements IBlogContentPresenter.IBlogContentView {

    public static BlogContentFragment newInstance(Blog blog) {

        Bundle args = new Bundle();
        args.putParcelable("blog", blog);
        BlogContentFragment fragment = new BlogContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @BindView(R.id.web_view_blog_content)
    WebView mWebView;

    private Blog mBlog;
    private IBlogContentPresenter mContentPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentPresenter = CnblogsPresenterFactory.getBlogContentPresenter(getContext(), this);
        if (getArguments() != null) {
            mBlog = getArguments().getParcelable("blog");
        }
    }

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
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
        mWebView.addJavascriptInterface(new BlogJavascriptApi(), "app");
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mBlog == null) return;
        mContentPresenter.loadContent();
    }


    @Override
    public Blog getBlog() {
        return mBlog;
    }

    @Override
    public void onLoadContentSuccess(Blog blog) {
//        mWebView.loadUrl("file:///android_asset/view.html");
        mWebView.loadUrl("http://192.168.168.21/view.html");
    }

    @Override
    public void onLoadContentFailed(String msg) {

    }


    /**
     * 加载原文
     */
    public void loadSourceUrl() {
        mWebView.loadUrl(mBlog.getUrl());
    }

    public String getUrl() {
        return mWebView.getUrl();
    }


    class BlogJavascriptApi {

        @JavascriptInterface
        public String getBlog() {
            return JSON.toJSONString(mBlog);
        }
    }
}
