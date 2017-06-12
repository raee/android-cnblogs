package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeAnim;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.AppGson;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
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


    private TextView mLikeView;
    private ImageLoadingView mBookmarksView;
    private BlogType mBlogType;

    public static BlogContentFragment newInstance(BlogBean blog, BlogType type) {
        Bundle args = new Bundle();
        args.putParcelable("blog", blog);
        args.putString("type", type.getTypeName());
        BlogContentFragment fragment = new BlogContentFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private BlogBean mBlog;
    private IBlogContentPresenter mContentPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blog_content;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mBlog = getArguments().getParcelable("blog");
            mBlogType = BlogType.typeOf(getArguments().getString("type"));
            mContentPresenter = CnblogsPresenterFactory.getBlogContentPresenter(getContext(), mBlogType, this);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWebView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    }

    @Override
    public Object getJavascriptApi() {
        return new RaeJavaScriptBridge() {

            @JavascriptInterface
            public String getBlog() {
                return AppGson.get().toJson(mBlog);
            }
        };
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mBlog == null) return;
        mLikeView = (TextView) getActivity().findViewById(R.id.tv_like_badge);
        mBookmarksView = (ImageLoadingView) getActivity().findViewById(R.id.img_content_bookmarks);
        mLikeView.setOnClickListener(this);
        mBookmarksView.setOnClickListener(this);
        mContentPresenter.loadContent();
    }

    @Override
    public BlogBean getBlog() {
        return mBlog;
    }

    @Override
    public void onLoadContentSuccess(BlogBean blog) {
        mPlaceholderView.dismiss();
        mWebView.loadUrl("file:///android_asset/view.html");
    }

    @Override
    public void onLoadContentFailed(String msg) {
        mPlaceholderView.empty();
    }

    @Override
    public void onLikeError(boolean isCancel, String msg) {
        mLikeView.setEnabled(true);
//        mLikeView.dismiss();
        RaeAnim.scaleIn(mLikeView);
        AppUI.toastInCenter(getContext(), msg);
    }

    @Override
    public void onLikeSuccess(boolean isCancel) {
        mLikeView.setEnabled(true);
//        mLikeView.dismiss();
        mLikeView.setSelected(!isCancel);
        RaeAnim.scaleIn(mLikeView);
    }

    @Override
    public void onBookmarksError(boolean isCancel, String msg) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.dismiss();
        RaeAnim.scaleIn(mBookmarksView);
        AppUI.toastInCenter(getContext(), msg);
    }

    @Override
    public void onBookmarksSuccess(boolean isCancel) {
        mBookmarksView.setEnabled(true);
        mBookmarksView.dismiss();
        RaeAnim.scaleIn(mBookmarksView);
        mBookmarksView.setSelected(!isCancel);
    }

    @Override
    public void onNeedLogin() {
        mLikeView.setEnabled(true);
        mBookmarksView.setEnabled(true);
//        mLikeView.dismiss();
        mBookmarksView.dismiss();
        AppRoute.jumpToLogin(getContext());
    }

    @Override
    public void onLoadBlogInfoSuccess(UserBlogInfo infoModel) {
        // 角标处理
        mLikeView.setSelected(infoModel.isLiked());
        mBookmarksView.setSelected(infoModel.isBookmarks());

        // 判断是否为自己的
    }

    @Override
    public BlogType getBlogType() {
        return mBlogType;
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
            case R.id.tv_like_badge:  // 点赞
                v.setEnabled(false);
//                ((ImageLoadingView) v).loading();
                mContentPresenter.doLike(v.isSelected());
                break;

            case R.id.img_content_bookmarks:  // 收藏
                v.setEnabled(false);
                ((ImageLoadingView) v).loading();
                mContentPresenter.doBookmarks(v.isSelected());
                break;
        }
    }


    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        mWebView.scrollTo(0, 0);
    }
}
