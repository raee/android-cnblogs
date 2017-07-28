package com.rae.cnblogs.presenter.impl.kb;

import android.content.Context;

import com.rae.cnblogs.presenter.impl.blog.BlogContentPresenterImpl;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.bean.BlogBean;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * 知识库
 * Created by ChenRui on 2017/2/4 0004 14:49.
 */
public class KBContentPresenterImpl extends BlogContentPresenterImpl {

    public KBContentPresenterImpl(Context context, IBlogContentView view) {
        super(context, view);
    }

    @Override
    protected void onLoadData(BlogBean blog) {
        createObservable(mBlogApi.getKbContent(blog.getBlogId())).subscribe(getBlogContentObserver());
    }


    @Override
    public void doLike(boolean isCancel) {

        // 不支持取消点赞
        if (isCancel) {
            Observable.timer(1000, TimeUnit.MILLISECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Long>() {
                        @Override
                        public void accept(Long aLong) throws Exception {
                            mView.onLikeSuccess(false);
                        }
                    });
            return;
        }
        Observable<Empty> observable = mBlogApi.likeKb(mView.getBlog().getBlogId());
        createObservable(false, observable, true);
    }
}
