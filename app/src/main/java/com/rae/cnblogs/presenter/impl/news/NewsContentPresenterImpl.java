package com.rae.cnblogs.presenter.impl.news;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogContentPresenterImpl;
import com.rae.cnblogs.sdk.api.INewsApi;
import com.rae.cnblogs.sdk.bean.BlogBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 新闻内容
 * Created by ChenRui on 2017/2/4 0004 14:49.
 */
public class NewsContentPresenterImpl extends BlogContentPresenterImpl {
    private INewsApi mNewsApi;

    public NewsContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
        mNewsApi = getApiProvider().getNewsApi();
    }

    @Override
    protected void onLoadData(BlogBean blog) {
        createObservable(mNewsApi.getNewsContent(blog.getBlogId())).subscribe(getBlogContentObserver());
    }

    @Override
    public void doLike(boolean isCancel) {

        // 不支持取消点赞
        if (isCancel) {
//            mView.onLikeError(isCancel, "您已经推荐过了");
            Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mView.onLikeSuccess(false);
                        }
                    });
        } else {
            createObservable(isCancel, mNewsApi.like(mView.getBlog().getBlogId()), true);
        }
    }
}
