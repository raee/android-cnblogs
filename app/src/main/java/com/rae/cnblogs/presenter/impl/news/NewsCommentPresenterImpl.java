package com.rae.cnblogs.presenter.impl.news;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogCommentPresenterImpl;
import com.rae.cnblogs.sdk.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;

/**
 * 评论
 * Created by ChenRui on 2016/12/26 0026 8:49.
 */
public class NewsCommentPresenterImpl extends BlogCommentPresenterImpl {

    private INewsApi mNewsApi;


    public NewsCommentPresenterImpl(Context context, IBlogCommentView view) {
        super(context, view);
        mNewsApi = getApiProvider().getNewsApi();
    }

    @Override
    protected void onLoadData(BlogBean blog, int page) {
        mNewsApi.getNewsComment(blog.getBlogId(), page, this);
    }

    @Override
    public void post(BlogCommentBean parent) {
        BlogBean blog = mView.getBlog();
        if (parent == null) {
            mNewsApi.addNewsComment(blog.getBlogId(), "", mView.getCommentContent(), getCommentListener());
            return;
        }

        parent.setBlogApp(parent.getAuthorName());

        // 引用评论
        if (mView.enableReferenceComment()) {
            mNewsApi.addNewsComment(blog.getBlogId(), parent, mView.getCommentContent(), getCommentListener());
        } else {
            mNewsApi.addNewsComment(blog.getBlogId(), parent.getId(), mView.getCommentContent(), getCommentListener());
        }
    }

    @Override
    public void delete(BlogCommentBean item) {
        mDelCommentListener.setBlogComment(item);
        mNewsApi.deleteNewsComment(mView.getBlog().getBlogId(), item.getId(), mDelCommentListener);
    }
}
