package com.rae.cnblogs.presenter.impl.news;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogCommentPresenterImpl;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;
import com.rae.cnblogs.sdk.utils.ApiUtils;

import java.util.List;

import io.reactivex.Observer;

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
        createObservable(mNewsApi.getNewsComment(blog.getBlogId(), page)).subscribe(new ApiDefaultObserver<List<BlogCommentBean>>() {
            @Override
            protected void onError(String message) {
                mView.onLoadCommentEmpty();
                mPage--;
            }

            @Override
            protected void accept(List<BlogCommentBean> data) {
                onLoadCommentsSuccess(data);
            }
        });
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

        if (parent == null) {
            createObservable(mNewsApi.addNewsComment(blog.getBlogId(), "0", mView.getCommentContent())).subscribe(subscriber);
            return;
        }

        parent.setBlogApp(parent.getAuthorName());

        // 引用评论
        if (mView.enableReferenceComment()) {
            createObservable(mNewsApi.addNewsComment(blog.getBlogId(), ApiUtils.getCommentContent(parent, mView.getCommentContent()), mView.getCommentContent())).subscribe(subscriber);
        } else {
            createObservable(mNewsApi.addNewsComment(blog.getBlogId(), parent.getId(), mView.getCommentContent())).subscribe(subscriber);
        }
    }

    @Override
    public void delete(final BlogCommentBean item) {
        createObservable(mNewsApi.deleteNewsComment(mView.getBlog().getBlogId(), item.getId())).subscribe(new ApiDefaultObserver<Empty>() {
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
}
