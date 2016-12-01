package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.Category;

import java.util.List;

/**
 * 首页
 * Created by ChenRui on 2016/12/2 00:21.
 */
public interface IHomePresenter extends IRaePresenter {

    interface IHomeView {
        void onLoadCategory(List<Category> data);

        void onLoadFailed(String msg);
    }
}
