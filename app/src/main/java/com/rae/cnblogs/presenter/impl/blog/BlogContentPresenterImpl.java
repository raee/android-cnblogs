package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.presenter.IBlogContentPresenter;
import com.rae.cnblogs.presenter.impl.BasePresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.db.DbBlog;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

import io.reactivex.Observable;

/**
 * 博客查看实现
 * Created by ChenRui on 2016/12/7 22:03.
 */
public class BlogContentPresenterImpl extends BasePresenter<IBlogContentPresenter.IBlogContentView> implements IBlogContentPresenter {

    protected IBlogApi mBlogApi;
    private IBookmarksApi mBookmarksApi;
    private UserBlogInfo mBlogInfo; // 博客信息
    private DbBlog mDbBlog;

    public BlogContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
        mBlogApi = getApiProvider().getBlogApi();
        mBookmarksApi = getApiProvider().getBookmarksApi();
        mDbBlog = DbFactory.getInstance().getBlog();
    }

    @Override
    public void loadContent() {
        BlogBean blog = mView.getBlog();
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

    protected void onLoadData(BlogBean blog) {
        RxObservable.create(mBlogApi.getBlogContent(blog.getBlogId())).subscribe(getBlogContentObserver());
    }

    @NonNull
    protected ApiDefaultObserver<String> getBlogContentObserver() {
        return new ApiDefaultObserver<String>() {
            @Override
            protected void onError(String message) {
                mView.onLoadContentFailed(message);
            }

            @Override
            protected void accept(String data) {
                // 保存博文内容
                mBlogInfo.setContent(data);
                mDbBlog.saveBlogInfo(mBlogInfo);

                // 回调
                mView.getBlog().setContent(data);
                mView.onLoadContentSuccess(mView.getBlog());
            }
        };
    }

    @Override
    public void doLike(final boolean isCancel) {
        BlogBean blog = mView.getBlog();

        Observable<Empty> observable;
        boolean liked;
        if (isCancel) {
            liked = false;
            observable = mBlogApi.unLikeBlog(blog.getBlogId(), blog.getBlogApp());
        } else {
            liked = true;
            observable = mBlogApi.likeBlog(blog.getBlogId(), blog.getBlogApp());
        }


        createObservable(isCancel, observable, liked);
    }

    @Override
    public void doBookmarks(boolean isCancel) {
        BlogBean blog = mView.getBlog();
        BookmarksBean m = new BookmarksBean(blog.getTitle(), blog.getSummary(), blog.getUrl());
        Observable<Empty> observable;
        boolean isLike;
        if (isCancel) {
            isLike = false;
            observable = mBookmarksApi.delBookmarks(blog.getUrl());
        } else {
            isLike = true;
            observable = mBookmarksApi.addBookmarks(m.getTitle(), m.getSummary(), m.getLinkUrl());
        }
        createObservable(isCancel, observable, isLike);
    }

    protected void createObservable(final boolean isCancel, Observable<Empty> observable, final boolean isLike) {
        RxObservable.create(observable).subscribe(new ApiDefaultObserver<Empty>() {
            @Override
            protected void onError(String msg) {
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
            protected void accept(Empty empty) {

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
        });
    }
}
