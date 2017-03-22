package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.presenter.IFeedPresenter;
import com.rae.cnblogs.sdk.bean.UserFeedBean;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiErrorCode;
import com.rae.core.sdk.exception.ApiException;

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
        getApiProvider().getFriendApi().getFeeds(mPage, mView.getBlogApp(), new ApiUiArrayListener<UserFeedBean>() {

            @Override
            public void onApiFailed(ApiException e, String msg) {
                if (mPage <= 1) {
                    mView.onLoadFeedFailed(msg);
                } else if (e.getErrorCode() == ApiErrorCode.ERROR_EMPTY_DATA) {
                    mView.onLoadMoreFinish();
                } else {
                    mView.onLoadMoreFeedFailed(msg);
                }
            }

            @Override
            public void onApiSuccess(List<UserFeedBean> userFeedBeen) {
                if (mPage <= 1) {
                    mDataList.clear();
                } else {
                    if (Rae.isEmpty(userFeedBeen)) {
                        mView.onLoadMoreFinish();
                        return;
                    }
                }
                mDataList.addAll(userFeedBeen);
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
