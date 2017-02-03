package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.presenter.IHomePresenter;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.Category;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页处理
 * Created by ChenRui on 2016/12/2 00:25.
 */
public class HomePresenterImpl extends BasePresenter<IHomePresenter.IHomeView> implements IHomePresenter, ApiUiArrayListener<Category> {

    private List<Category> mCategoryData;

    public HomePresenterImpl(Context context, IHomeView view) {
        super(context, view);
    }

    @Override
    public void start() {

        UserProvider userProvider = UserProvider.getInstance();
        if (userProvider.isLogin()) {
            mView.onLoadUserInfo(userProvider.getLoginUserInfo());
        } else {
            mView.onLoadNormal();
        }

        // 加载分类
        getApiProvider().getCategoryApi().getCategory(this);
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        // 发生错误至少加载首页这个分类
        List<Category> data = new ArrayList<>();

        Category home = new Category();
        home.setCategoryId("808");
        home.setParentId("0");
        home.setName("首页");
        home.setType("SiteHome");

        Category recommend = new Category();
        recommend.setCategoryId("-2");
        recommend.setParentId("0");
        recommend.setName("推荐");
        recommend.setType("Picked");

        data.add(home);
        data.add(recommend);
    }

    @Override
    public void onApiSuccess(List<Category> data) {
        // 集合发生改变才回调
        if (mCategoryData == null || mCategoryData.size() != data.size()) {
            mView.onLoadCategory(data);
            mCategoryData = data;
        }
    }
}
