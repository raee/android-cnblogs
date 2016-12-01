package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.presenter.IHomePresenter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.List;

/**
 * Created by ChenRui on 2016/12/2 00:25.
 */
public class HomePresenterImpl extends BasePresenter<IHomePresenter.IHomeView> implements IHomePresenter, ApiUiArrayListener<Category> {

    public HomePresenterImpl(Context context, IHomeView view) {
        super(context, view);
    }

    @Override
    public void start() {
        // 加载分类
        CnblogsApiFactory.getCategoryApi(mContext).getCategory(this);
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mView.onLoadFailed(msg);
    }

    @Override
    public void onApiSuccess(List<Category> data) {
        mView.onLoadCategory(data);
    }
}
