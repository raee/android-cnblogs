package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.presenter.IMomentMessageContract;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * moment
 * Created by ChenRui on 2017/10/27 0027 10:57.
 */
public class MomentMessagePresenterImpl extends BasePresenter<IMomentMessageContract.View> implements IMomentMessageContract.Presenter {

    private IMomentApi mMomentApi;

    private PageObservable<MomentCommentBean> mPageObservable;


    public MomentMessagePresenterImpl(Context context, IMomentMessageContract.View view) {
        super(context, view);
        mMomentApi = CnblogsApiFactory.getInstance(context).getMomentApi();
        mPageObservable = new PageObservable<MomentCommentBean>(view) {
            @Override
            protected Observable<List<MomentCommentBean>> onCreateObserver(int page) {
                return createObservable(mMomentApi.getReplyMeMoments(IMomentApi.MOMENT_TYPE_REPLY_ME, page, System.currentTimeMillis()));
            }
        };
    }

    @Override
    public void postComment(String ingId, String userId, String commentId, String content) {
        createObservable(mMomentApi.postComment(ingId, userId, commentId, content))
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        mView.onPostCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        mView.onPostCommentSuccess();
                    }
                });
    }

    @Override
    public void start() {
        super.start();
        mPageObservable.start();
    }

    @Override
    public void destroy() {
        super.destroy();
        mPageObservable.destroy();
    }

    @Override
    public void loadMore() {
        mPageObservable.loadMore();
    }

}
