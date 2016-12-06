package com.rae.cnblogs.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.Blog;

import butterknife.BindView;

/**
 * 博文内容
 * Created by ChenRui on 2016/12/6 23:39.
 */
public class BlogContentFragment extends BaseFragment {

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

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBlog = getArguments().getParcelable("blog");
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        WebSettings settings = mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(false);
        mWebView.setWebChromeClient(new WebChromeClient());
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("http://www.cnblogs.com/wenJiaQi/p/6139599.html");
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mBlog == null) return;
        mWebView.loadUrl(mBlog.getUrl());
    }
}
