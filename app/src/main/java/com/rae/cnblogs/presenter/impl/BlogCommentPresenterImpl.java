package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.rae.cnblogs.presenter.IBlogCommentPresenter;
import com.rae.cnblogs.sdk.IBlogApi;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogComment;
import com.rae.cnblogs.sdk.bean.BlogType;
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
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mPage++;
            return false;
        }
    });

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
        if (BlogType.typeOf(blog.getBlogType()) == BlogType.NEWS) {
            mNewsApi.getNewsComment(blog.getBlogId(), mPage, this);
        } else {
            mBlogApi.getBlogComments(mPage, blog.getBlogId(), blog.getBlogApp(), this);
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

            return;
        }

        mCommentList.removeAll(data);
        if (mPage <= 1) {
            mCommentList.addAll(0, data);
        } else {
            mCommentList.addAll(data);
        }

        mView.onLoadCommentSuccess(mCommentList);

        // 由于第一次会加载缓存，所以要等待一段时间才处理
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }
}
