package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:52.
 */
public interface ILauncherPresenter extends IAppPresenter {

    void advertClick();

    interface ILauncherView {

        void onLoadImage(String url);

        void onJumpToWeb(String url);

        void onJumpToBlog(Blog blog);

        void onNormalImage();

    }
}
