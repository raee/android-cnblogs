package com.rae.cnblogs.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.bean.Blog;

import java.io.File;
import java.util.regex.Pattern;

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

    @BindView(R.id.pb_web_view)
    ProgressBar mProgressBar;

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
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                showProgress(newProgress);
            }


        });
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dismissProgress();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // 重写URL，跳转原生的博文查看
                Log.i("rae", "URL = " + url);
                boolean isBlogContent = Pattern.matches("http://www.cnblogs.com/\\w.+/\\d+.html", url);
                if (isBlogContent) {
                    // 获取博客ID
                    String id = url.substring(url.lastIndexOf("/")).replace(".html", "").replace("/", "").trim();
                    if (!TextUtils.isEmpty(id) && id.length() > 3) {
                        Log.w("rae", "博客ID为：" + id);
                        return true;
                    }

                }

                return super.shouldOverrideUrlLoading(view, url);
            }
        });


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
        mWebView.loadUrl("file:///android_asset/view.html");
//        mWebView.loadUrl("http://192.168.1.111/view.html");
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

    private void showProgress(int progress) {
        mProgressBar.setProgress(progress);
    }

    private void dismissProgress() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_out);
        mProgressBar.startAnimation(animation);
        mProgressBar.setVisibility(View.GONE);
    }


    class BlogJavascriptApi {

        @JavascriptInterface
        public String getBlog() {
            return JSON.toJSONString(mBlog);
        }
    }
}
