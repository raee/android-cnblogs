package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.UserFeedBean;

import java.util.List;

/**
 * Created by ChenRui on 2017/3/16 16:20.
 */
public interface IFeedPresenter extends  IAppPresenter {


    void loadMore();

    interface IFeedView {
        String getBlogApp();

        void onLoadFeedFailed(String msg);

        void onLoadMoreFeedFailed(String msg);

        void onLoadFeedSuccess(List<UserFeedBean> dataList);

        void onLoadMoreFinish();
    }
}
