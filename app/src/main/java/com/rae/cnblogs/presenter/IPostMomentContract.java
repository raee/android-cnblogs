package com.rae.cnblogs.presenter;

import java.util.List;

/**
 * 发布闪存
 * Created by ChenRui on 2017/10/27 0027 14:37.
 */
public interface IPostMomentContract {

    interface Presenter extends IAppPresenter {
        boolean post();

        boolean isBlogOpened();
    }

    interface View {

        String getContent();

        void onPostMomentFailed(String msg);

        void onPostMomentSuccess();

        List<String> getImageUrls();

        /**
         * 后台发送
         */
        void onPostMomentInProgress();

        /**
         * 博客开通状态
         */
        void onLoadBlogOpenStatus(Boolean value);
    }
}
