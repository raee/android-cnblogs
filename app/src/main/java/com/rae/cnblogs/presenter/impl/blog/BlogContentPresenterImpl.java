package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.presenter.impl.BasePresenter;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.IBookmarksApi;
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

    protected IBlogApi mBlogApi;
    private IBookmarksApi mBookmarksApi;
    private UserBlogInfo mBlogInfo; // 博客信息
    private DbBlog mDbBlog;
    private LikeAndBookmarksListener mLikeAndBookmarksListener;

    public BlogContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
        mBlogApi = getApiProvider().getBlogApi();
        mBookmarksApi = getApiProvider().getBookmarksApi();
        mDbBlog = new DbBlog();
    }

    @Override
    public void loadContent() {
        Blog blog = mView.getBlog();
        if (blog == null) return;

        // 获取用户的博客信息
        mBlogInfo = mDbBlog.get(blog.getBlogId());

        if (mBlogInfo == null || mBlogInfo.getBlogId() == null) {
            mBlogInfo = new UserBlogInfo();
            mBlogInfo.setBlogId(blog.getBlogId());
            mBlogInfo.setBlogType(blog.getBlogType());
            mBlogInfo.setRead(true);
        }

        mView.onLoadBlogInfoSuccess(mBlogInfo);


        if (!TextUtils.isEmpty(mBlogInfo.getContent())) {
            blog.setContent(mBlogInfo.getContent());
            mView.onLoadContentSuccess(blog);
            return;
        }
        onLoadData(blog);
    }

    protected void onLoadData(Blog blog) {
        mBlogApi.getBlogContent(blog.getBlogId(), this);
    }

    @Override
    public void doLike(boolean isCancel) {
        Blog blog = mView.getBlog();

        if (isCancel) {
            mBlogApi.unLikeBlog(blog.getBlogId(), blog.getBlogApp(), getLikeAndBookmarksListener(true, true));
        } else {
            mBlogApi.likeBlog(blog.getBlogId(), blog.getBlogApp(), getLikeAndBookmarksListener(false, true));
        }
    }

    protected ApiUiListener<Void> getLikeAndBookmarksListener(boolean isCancel, boolean isLike) {
        if (mLikeAndBookmarksListener == null) {
            mLikeAndBookmarksListener = new LikeAndBookmarksListener(isCancel, isLike);
        }
        mLikeAndBookmarksListener.setCancel(isCancel);
        mLikeAndBookmarksListener.setLike(isLike);
        return mLikeAndBookmarksListener;
    }

    @Override
    public void doBookmarks(boolean isCancel) {
        Blog blog = mView.getBlog();
        BookmarksBean m = new BookmarksBean(blog.getTitle(), blog.getSummary(), blog.getUrl());

        if (isCancel) {
            mBookmarksApi.delBookmarks(blog.getUrl(), getLikeAndBookmarksListener(true, false));
        } else {
            mBookmarksApi.addBookmarks(m, getLikeAndBookmarksListener(false, false));
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

        // 回调
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

        public void setCancel(boolean cancel) {
            isCancel = cancel;
        }

        public void setLike(boolean like) {
            isLike = like;
        }

        @Override
        public void onApiFailed(ApiException ex, String msg) {
            if (!TextUtils.isEmpty(msg) && (msg.contains("登录") || msg.contains("Authorization"))) {
                mView.onNeedLogin();
                return;
            }

            // 新闻推荐过了
            if (msg.contains("您已经推荐过")) {
                mView.onLikeSuccess(isCancel);
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
