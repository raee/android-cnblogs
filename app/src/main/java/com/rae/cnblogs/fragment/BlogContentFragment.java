package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.alibaba.fastjson.JSON;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.db.model.UserBlogInfoModel;
import com.rae.cnblogs.widget.webclient.RaeJavaScriptBridge;
import com.rae.cnblogs.widget.webclient.RaeWebViewClient;


/**
 * 博文内容
 * Created by ChenRui on 2016/12/6 23:39.
 */
public class BlogContentFragment extends WebViewFragment implements IBlogContentPresenter.IBlogContentView, View.OnClickListener {

    private View mLikeView;
    private View mBookmarksView;

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
        mLikeView = getActivity().findViewById(R.id.img_content_like);
        mBookmarksView = getActivity().findViewById(R.id.img_content_bookmarks);
        mLikeView.setOnClickListener(this);
        mBookmarksView.setOnClickListener(this);
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

    @Override
    public void onLikeError(boolean isCancel, String msg) {
        mLikeView.setEnabled(true);
        AppUI.toast(getContext(), "点赞失败：" + msg);
    }

    @Override
    public void onLikeSuccess(boolean isCancel) {
        mLikeView.setEnabled(true);
        mLikeView.setSelected(isCancel);
        AppUI.toast(getContext(), "点赞成功：" + isCancel);
    }

    @Override
    public void onBookmarksError(boolean isCancel, String msg) {
        mBookmarksView.setEnabled(true);
        AppUI.toast(getContext(), "收藏失败：" + msg);
    }

    @Override
    public void onBookmarksSuccess(boolean isCancel) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.setSelected(isCancel);
        AppUI.toast(getContext(), "收藏成功：" + isCancel);
    }

    @Override
    public void onNeedLogin() {
        mLikeView.setEnabled(true);
        mBookmarksView.setEnabled(true);
        AppRoute.jumpToLogin(getContext());
    }

    @Override
    public void onLoadBlogInfoSuccess(UserBlogInfoModel infoModel) {
        mLikeView.setSelected(infoModel.isLiked());
        mBookmarksView.setSelected(infoModel.isBookmarks());
    }

    @Override
    public WebViewClient getWebViewClient() {
        return new RaeWebViewClient(mProgressBar) {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                AppRoute.jumpToWeb(view.getContext(), url);
                return true;
            }
        };
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_content_like:  // 点赞
                v.setEnabled(false);
                mContentPresenter.doLike(v.isSelected());
                break;

            case R.id.img_content_bookmarks:  // 收藏
                v.setEnabled(false);
                mContentPresenter.doBookmarks(v.isSelected());
                break;
        }
    }
}
