package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.webkit.JavascriptInterface;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.widget.webclient.RaeJavaScriptBridge;


/**
 * 博文内容
 * Created by ChenRui on 2016/12/6 23:39.
 */
public class BlogContentFragment extends WebViewFragment implements IBlogContentPresenter.IBlogContentView {

    public static BlogContentFragment newInstance(Blog blog) {

        Bundle args = new Bundle();
        args.putParcelable("blog", blog);
        BlogContentFragment fragment = new BlogContentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private Blog mBlog;
    private IBlogContentPresenter mContentPresenter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContentPresenter = CnblogsPresenterFactory.getBlogContentPresenter(getContext(), this);
        if (getArguments() != null) {
            mBlog = getArguments().getParcelable("blog");
        }
    }

    @Override
    public Object getJavascriptApi() {
        return new RaeJavaScriptBridge() {

            @JavascriptInterface
            public String getBlog() {
                return JSON.toJSONString(mBlog);
            }
        };
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
    }

    @Override
    public void onLoadContentFailed(String msg) {

    }

    /**
     * 加载原文
     */
    public void loadSourceUrl() {
        loadUrl(mBlog.getUrl());
    }


}
