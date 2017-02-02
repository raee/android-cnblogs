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
import com.rae.cnblogs.RaeAnim;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.cnblogs.widget.ImageLoadingView;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.webclient.RaeJavaScriptBridge;
import com.rae.cnblogs.widget.webclient.RaeWebViewClient;

import butterknife.BindView;


/**
 * 博文内容
 * Created by ChenRui on 2016/12/6 23:39.
 */
public class BlogContentFragment extends WebViewFragment implements IBlogContentPresenter.IBlogContentView, View.OnClickListener {

    @BindView(R.id.view_holder)
    PlaceholderView mPlaceholderView;

    private ImageLoadingView mLikeView;
    private ImageLoadingView mBookmarksView;

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
        mLikeView = (ImageLoadingView) getActivity().findViewById(R.id.img_content_like);
        mBookmarksView = (ImageLoadingView) getActivity().findViewById(R.id.img_content_bookmarks);
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
        mPlaceholderView.dismiss();
        mWebView.loadUrl("file:///android_asset/view.html");
    }

    @Override
    public void onLoadContentFailed(String msg) {
        mPlaceholderView.empty(msg);
    }

    @Override
    public void onLikeError(boolean isCancel, String msg) {
        mLikeView.setEnabled(true);
        mLikeView.dismiss();
        RaeAnim.scaleIn(mLikeView);
        AppUI.toast(getContext(), "点赞失败：" + msg);
    }

    @Override
    public void onLikeSuccess(boolean isCancel) {
        mLikeView.setEnabled(true);
        mLikeView.dismiss();
        mLikeView.setSelected(!isCancel);
        RaeAnim.scaleIn(mLikeView);
        AppUI.toast(getContext(), "点赞成功：" + isCancel);
    }

    @Override
    public void onBookmarksError(boolean isCancel, String msg) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.dismiss();
        RaeAnim.scaleIn(mBookmarksView);
        AppUI.toast(getContext(), "收藏失败：" + msg);
    }

    @Override
    public void onBookmarksSuccess(boolean isCancel) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.dismiss();
        RaeAnim.scaleIn(mBookmarksView);
        mBookmarksView.setSelected(!isCancel);
        AppUI.toast(getContext(), "收藏成功：" + isCancel);
    }

    @Override
    public void onNeedLogin() {
        mLikeView.setEnabled(true);
        mBookmarksView.setEnabled(true);
        mLikeView.dismiss();
        mBookmarksView.dismiss();
        AppRoute.jumpToLogin(getContext());
    }

    @Override
    public void onLoadBlogInfoSuccess(UserBlogInfo infoModel) {
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
                ((ImageLoadingView) v).loading();
                mContentPresenter.doLike(v.isSelected());
                break;

            case R.id.img_content_bookmarks:  // 收藏
                v.setEnabled(false);
                ((ImageLoadingView) v).loading();
                mContentPresenter.doBookmarks(v.isSelected());
                break;
        }
    }
}
