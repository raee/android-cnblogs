package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.IMomentDetailContract;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.swift.Rx;

import java.util.List;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 16:59.
 */
public class MomentDetailPresenterImpl extends BasePresenter<IMomentDetailContract.View> implements IMomentDetailContract.Presenter {

    private IMomentApi mMomentApi;

    public MomentDetailPresenterImpl(Context context, IMomentDetailContract.View view) {
        super(context, view);
        mMomentApi = CnblogsApiFactory.getInstance(context).getMomentApi();
    }

    @Override
    public void start() {
        super.start();
        MomentBean momentInfo = mView.getMomentInfo();
        createObservable(mMomentApi.getMomentSingleComments(momentInfo.getId(), momentInfo.getUserAlias(), System.currentTimeMillis()))
                .subscribe(new ApiDefaultObserver<List<MomentCommentBean>>() {
                    @Override
                    protected void onError(String message) {
                        mView.onEmptyComment(message);
                    }

                    @Override
                    protected void accept(List<MomentCommentBean> momentCommentBeans) {
                        if (Rx.isEmpty(momentCommentBeans)) {
                            mView.onEmptyComment(getString(R.string.empty_comment));
                            return;
                        }
                        mView.onLoadComments(momentCommentBeans);
                    }
                });
    }

    @Override
    public void loadMore() {
// TODO:闪存评论加载更多
    }
}
