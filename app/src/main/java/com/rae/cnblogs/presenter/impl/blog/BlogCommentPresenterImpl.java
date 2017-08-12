package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;

import com.rae.cnblogs.presenter.IBlogCommentPresenter;
import com.rae.cnblogs.presenter.impl.BasePresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;
import com.rae.swift.Rx;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;

/**
 * 评论
 * Created by ChenRui on 2016/12/26 0026 8:49.
 */
public class BlogCommentPresenterImpl extends BasePresenter<IBlogCommentPresenter.IBlogCommentView> implements IBlogCommentPresenter {

    private IBlogApi mBlogApi;
    protected int mPage = 1;
    private final List<BlogCommentBean> mCommentList = new ArrayList<>();

    public BlogCommentPresenterImpl(Context context, IBlogCommentView view) {
        super(context, view);
        mBlogApi = getApiProvider().getBlogApi();
    }

    @Override
    public void loadMore() {
        onLoadData(mView.getBlog(), mPage);
    }

    @Override
    public void post(BlogCommentBean parent) {
        BlogBean blog = mView.getBlog();
        Observer<Empty> subscriber = new ApiDefaultObserver<Empty>() {
            @Override
            protected void onError(String message) {
                mView.onPostCommentFailed(message);
            }

            @Override
            protected void accept(Empty o) {
                mView.onPostCommentSuccess();
            }
        };

        // 不引用评论
        if (parent == null) {
            Observable<Empty> observable = mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), "0", mView.getCommentContent());
            createObservable(observable).subscribe(subscriber);
            return;
        }

        // 引用评论
        if (mView.enableReferenceComment()) {
            createObservable(mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), parent.getId(), ApiUtils.getCommentContent(parent, mView.getCommentContent())))
                    .subscribe(subscriber);
        } else {
            createObservable(mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), parent.getId(), ApiUtils.getAtCommentContent(parent, mView.getCommentContent()))).subscribe(subscriber);
        }
    }

    @Override
    public void delete(final BlogCommentBean item) {
        Observable<Empty> observable = mBlogApi.deleteBlogComment(item.getId());
        createObservable(observable).subscribe(new ApiDefaultObserver<Empty>() {
            @Override
            protected void onError(String message) {
                mView.onDeleteCommentFailed(message);
            }

            @Override
            protected void accept(Empty empty) {
                mView.onDeleteCommentSuccess(item);
            }
        });
    }

    @Override
    public void start() {
        mPage = 1;
        BlogBean blog = mView.getBlog();
        if (blog == null) {
            mView.onLoadCommentEmpty();
            return;
        }

        onLoadData(blog, mPage);
    }

    protected void onLoadData(BlogBean blog, int page) {
        createObservable(mBlogApi.getBlogComments(page, blog.getBlogId(), blog.getBlogApp())).subscribe(new ApiDefaultObserver<List<BlogCommentBean>>() {
            @Override
            protected void onError(String message) {
                if (mPage <= 1) {
                    mView.onLoadCommentFailed(message);
                } else {
                    mView.onLoadMoreCommentEmpty();
                }
                mPage--;
            }

            @Override
            protected void accept(List<BlogCommentBean> data) {
                onLoadCommentsSuccess(data);
            }
        });
    }

    protected void onLoadCommentsSuccess(List<BlogCommentBean> data) {
        if (Rx.isEmpty(data)) {
            if (mPage <= 1)
                mView.onLoadCommentEmpty();
            else
                mView.onLoadMoreCommentEmpty();

            return;
        }

        if (mPage <= 1) {
            mCommentList.clear();
            mCommentList.addAll(0, data);
        } else {
            mCommentList.removeAll(data);
            mCommentList.addAll(data);
        }

        mView.onLoadCommentSuccess(mCommentList);
        mPage++;
    }
}
