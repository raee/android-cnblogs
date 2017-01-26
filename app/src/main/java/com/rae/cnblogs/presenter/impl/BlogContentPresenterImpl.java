package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.IBookmarksApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

/**
 * 博客查看实现
 * Created by ChenRui on 2016/12/7 22:03.
 */
public class BlogContentPresenterImpl extends BasePresenter<IBlogContentPresenter.IBlogContentView> implements IBlogContentPresenter, ApiUiListener<String> {

    private IBlogApi mBlogApi;
    private IBookmarksApi mBookmarksApi;
    private INewsApi mNewsApi;
    private UserBlogInfo mBlogInfo; // 博客信息
    private DbBlog mDbBlog;

    public BlogContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
        mBlogApi = getApiProvider().getBlogApi();
        mNewsApi = getApiProvider().getNewsApi();
        mBookmarksApi = getApiProvider().getBookmarksApi();
        mDbBlog = new DbBlog();
    }

    @Override
    public void loadContent() {
        Blog blog = mView.getBlog();
        if (blog == null) return;

        // 获取用户的博客信息
        mBlogInfo = mDbBlog.get(blog.getId());

        if (mBlogInfo == null || mBlogInfo.getBlogId() == null) {
            mBlogInfo = new UserBlogInfo();
            mBlogInfo.setBlogId(blog.getId());
            mBlogInfo.setRead(true);
        }

        mView.onLoadBlogInfoSuccess(mBlogInfo);


        if (!TextUtils.isEmpty(mBlogInfo.getContent())) {
            blog.setContent(mBlogInfo.getContent());
            mView.onLoadContentSuccess(blog);
            return;
        }


        if (blog.isNews()) {
            // 新闻
            mNewsApi.getNewsContent(blog.getId(), this);
        } else if (blog.isKb()) {
            // 知识库
            mBlogApi.getKbContent(blog.getId(), this);
        } else {
            // 博文
            mBlogApi.getBlogContent(blog.getId(), this);
        }
    }

    @Override
    public void doLike(boolean isCancel) {
        Blog blog = mView.getBlog();

        if (isCancel) {
            mBlogApi.unLikeBlog(blog.getId(), blog.getBlogApp(), new LikeAndBookmarksListener(true, true));
        } else {
            mBlogApi.likeBlog(blog.getId(), blog.getBlogApp(), new LikeAndBookmarksListener(false, true));
        }
    }

    @Override
    public void doBookmarks(boolean isCancel) {
        Blog blog = mView.getBlog();
        BookmarksBean m = new BookmarksBean(blog.getTitle(), blog.getSummary(), blog.getUrl());

        if (isCancel) {
            mBookmarksApi.delBookmarks(blog.getUrl(), new LikeAndBookmarksListener(true, false));
        } else {
            mBookmarksApi.addBookmarks(m, new LikeAndBookmarksListener(false, false));
        }
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoadContentFailed(msg);
    }

    @Override
    public void onApiSuccess(String data) {
        // 保存博文内容
        mBlogInfo.setContent(data);
        mDbBlog.saveBlogInfo(mBlogInfo);
        mView.getBlog().setContent(data);
        mView.onLoadContentSuccess(mView.getBlog());
    }


    private class LikeAndBookmarksListener implements ApiUiListener<Void> {

        private boolean isCancel;
        private boolean isLike;

        public LikeAndBookmarksListener(boolean isCancel, boolean isLike) {
            this.isCancel = isCancel;
            this.isLike = isLike;
        }

        @Override
        public void onApiFailed(ApiException ex, String msg) {
            if (!TextUtils.isEmpty(msg) && (msg.contains("登录") || msg.contains("Authorization"))) {
                mView.onNeedLogin();
                return;
            }

            if (isLike) {
                mView.onLikeError(isCancel, msg);
            } else {
                mView.onBookmarksError(isCancel, msg);
            }
        }

        @Override
        public void onApiSuccess(Void data) {


            if (isLike) {
                mView.onLikeSuccess(isCancel);
                mBlogInfo.setLiked(!isCancel);

            } else {
                mView.onBookmarksSuccess(isCancel);
                mBlogInfo.setBookmarks(!isCancel);
            }

            // 更新信息
            mDbBlog.saveBlogInfo(mBlogInfo);
        }
    }
}
