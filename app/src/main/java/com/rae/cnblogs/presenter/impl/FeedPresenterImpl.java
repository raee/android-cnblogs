package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.presenter.IFeedPresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.swift.Rx;

import java.util.ArrayList;
import java.util.List;

/**
 * 动态逻辑
 * Created by ChenRui on 2017/3/16 16:20.
 */
public class FeedPresenterImpl extends BasePresenter<IFeedPresenter.IFeedView> implements IFeedPresenter {

    private int mPage;
    private final List<UserFeedBean> mDataList = new ArrayList<>();

    public FeedPresenterImpl(Context context, IFeedView view) {
        super(context, view);
    }

    @Override
    public void start() {
        mPage = 1;
        loadData();
    }


    private void loadData() {
        createObservable(getApiProvider().getFriendApi().getFeeds(mPage, mView.getBlogApp()))
                .subscribe(new ApiDefaultObserver<List<UserFeedBean>>() {
                    @Override
                    protected void onError(String message) {
                        if (mPage <= 1) {
                            mView.onLoadFeedFailed(message);
                        } else {
                            mView.onLoadMoreFeedFailed(message);
                        }
                    }

                    @Override
                    protected void onEmpty(List<UserFeedBean> userFeedBeans) {
                        if (mPage > 1) {
                            mView.onLoadMoreFinish();
                        }
                    }

                    @Override
                    protected void accept(List<UserFeedBean> data) {
                        if (mPage <= 1) {
                            mDataList.clear();
                        } else {
                            if (Rx.isEmpty(data)) {
                                mView.onLoadMoreFinish();
                                return;
                            }
                        }
                        mDataList.addAll(data);
                        mView.onLoadFeedSuccess(mDataList);
                        mPage++;
                    }
                });


    }

    @Override
    public void loadMore() {
        loadData();
    }
}
