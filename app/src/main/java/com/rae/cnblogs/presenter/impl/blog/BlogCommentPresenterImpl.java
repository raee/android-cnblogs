package com.rae.cnblogs.presenter.impl.blog;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.rae.cnblogs.RxObservable;
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
    private final Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mPage++;
            return false;
        }
    });

//    private final ApiUiListener<Void> mCommentListener = new ApiUiListener<Void>() {
//        @Override
//        public void onApiFailed(ApiException ex, String msg) {
//            mView.onPostCommentFailed(msg);
//        }
//
//        @Override
//        public void onApiSuccess(Void data) {
//            mView.onPostCommentSuccess();
//        }
//    };

//    protected final BlogCommentListener mDelCommentListener = new BlogCommentListener();

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

        if (parent == null) {
            Observable<Empty> observable = mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), "0", mView.getCommentContent());
            createObservable(observable).subscribe(subscriber);
            return;
        }

        // 引用评论
        if (mView.enableReferenceComment()) {
            createObservable(mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), ApiUtils.getCommentContent(parent, mView.getCommentContent()), mView.getCommentContent()))
                    .subscribe(subscriber);
        } else {
            createObservable(mBlogApi.addBlogComment(blog.getBlogId(), blog.getBlogApp(), parent.getId(), mView.getCommentContent())).subscribe(subscriber);
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
                mView.onLoadCommentEmpty();
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

        // 由于第一次会加载缓存，所以要等待一段时间才处理
        if (mPage <= 1) {
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0, 1500);
        }
    }
}
