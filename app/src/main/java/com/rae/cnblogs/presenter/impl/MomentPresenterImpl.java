package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.presenter.IMomentContract;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;

import java.util.List;

import io.reactivex.Observable;

/**
 * moment
 * Created by ChenRui on 2017/10/27 0027 10:57.
 */
public class MomentPresenterImpl extends BasePresenter<IMomentContract.View> implements IMomentContract.Presenter {

    private IMomentApi mMomentApi;

    private PageObservable<MomentBean> mPageObservable;


    public MomentPresenterImpl(Context context, IMomentContract.View view) {
        super(context, view);
        mMomentApi = CnblogsApiFactory.getInstance(context).getMomentApi();
        mPageObservable = new PageObservable<MomentBean>(view) {
            @Override
            protected Observable<List<MomentBean>> onCreateObserver(int page) {
                return createObservable(mMomentApi.getMoments(mView.getType(), page, System.currentTimeMillis()));
            }
        };
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
