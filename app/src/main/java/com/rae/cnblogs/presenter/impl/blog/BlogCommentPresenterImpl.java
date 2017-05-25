package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.rae.cnblogs.presenter.IBlogCommentPresenter;
import com.rae.cnblogs.presenter.impl.BasePresenter;
import com.rae.cnblogs.sdk.api.IBlogApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论
 * Created by ChenRui on 2016/12/26 0026 8:49.
 */
public class BlogCommentPresenterImpl extends BasePresenter<IBlogCommentPresenter.IBlogCommentView> implements IBlogCommentPresenter, ApiUiArrayListener<BlogCommentBean> {

    private IBlogApi mBlogApi;
    private int mPage = 1;
    private final List<BlogCommentBean> mCommentList = new ArrayList<>();
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mPage++;
            return false;
        }
    });

    private final ApiUiListener<Void> mCommentListener = new ApiUiListener<Void>() {
        @Override
        public void onApiFailed(ApiException ex, String msg) {
            mView.onPostCommentFailed(msg);
        }

        @Override
        public void onApiSuccess(Void data) {
            mView.onPostCommentSuccess();
        }
    };

    protected final BlogCommentListener mDelCommentListener = new BlogCommentListener();

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
        if (parent == null) {
            mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), "", mView.getCommentContent(), mCommentListener);
            return;
        }

        // 引用评论
        if (mView.enableReferenceComment()) {
            mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), parent, mView.getCommentContent(), mCommentListener);
        } else {
            mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), parent.getId(), mView.getCommentContent(), mCommentListener);
        }
    }

    @Override
    public void delete(BlogCommentBean item) {
        mDelCommentListener.setBlogComment(item);
        mBlogApi.deleteBlogComment(item.getId(), mDelCommentListener);
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
        mBlogApi.getBlogComments(page, blog.getBlogId(), blog.getBlogApp(), this);
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoadCommentEmpty();
        mPage--;
    }

    @Override
    public void onApiSuccess(List<BlogCommentBean> data) {
        if (Rae.isEmpty(data)) {
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

        // 由于第一次会加载缓存，所以要等待一段时间才处理
        if (mPage <= 1) {
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0, 1500);
        }
    }

    public ApiUiListener<Void> getCommentListener() {
        return mCommentListener;
    }


    public class BlogCommentListener implements ApiUiListener<Void> {

        private BlogCommentBean mItem;

        public void setBlogComment(BlogCommentBean item) {
            mItem = item;
        }

        @Override
        public void onApiFailed(ApiException ex, String msg) {
            mView.onDeleteCommentFailed(msg);
        }

        @Override
        public void onApiSuccess(Void data) {
            mView.onDeleteCommentSuccess(mItem);
            mItem = null;
        }
    }
}
