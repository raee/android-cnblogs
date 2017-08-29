package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

/**
 * 首页
 * Created by ChenRui on 2016/12/2 00:21.
 */
public interface IHomePresenter extends IRaePresenter {

    interface IHomeView {
        void onLoadCategory(List<CategoryBean> data);

//        /**
//         * 加载用户信息
//         */
//        void onLoadUserInfo(UserInfoBean userInfo);
//
//        /**
//         * 正常加载
//         */
//        void onLoadNormal();
    }
}
