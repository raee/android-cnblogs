package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.presenter.IBlogCommentPresenter;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 * Created by ChenRui on 2016/12/26 0026 8:49.
 */
public class BlogCommentPresenterImpl extends BasePresenter<IBlogCommentPresenter.IBlogCommentView> implements IBlogCommentPresenter, ApiUiArrayListener<BlogComment> {

    private IBlogApi mBlogApi;
    private INewsApi mNewsApi;
    private int mPage = 1;
    private final List<BlogComment> mCommentList = new ArrayList<>();

    public BlogCommentPresenterImpl(Context context, IBlogCommentView view) {
        super(context, view);

        mBlogApi = getApiProvider().getBlogApi();
        mNewsApi = getApiProvider().getNewsApi();
    }

    @Override
    public void loadMore() {
        loadComments();
    }

    @Override
    public void start() {
        mPage = 1;
        loadComments();
    }

    private void loadComments() {
        Blog blog = mView.getBlog();
        if (blog == null) {
            mView.onLoadCommentEmpty();
            return;
        }
        if (blog.isNews()) {
            mNewsApi.getNewsComment(blog.getId(), mPage, this);
        } else {
            mBlogApi.getComment(mPage, blog.getId(), blog.getBlogApp(), this);
        }
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoadCommentEmpty();
        mPage--;
    }

    @Override
    public void onApiSuccess(List<BlogComment> data) {
        if (Rae.isEmpty(data)) {
            if (mPage <= 1)
                mView.onLoadCommentEmpty();
            else
                mView.onLoadMoreCommentEmpty();
        }

        mCommentList.removeAll(data);
        if (mPage <= 1) {
            mCommentList.addAll(0, data);
        } else {
            mCommentList.addAll(data);
        }

        mView.onLoadCommentSuccess(mCommentList);
        mPage++;
    }
}
