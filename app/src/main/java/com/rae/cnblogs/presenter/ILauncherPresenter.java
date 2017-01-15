package com.rae.cnblogs.presenter;

/**
 * 启动页
 * Created by ChenRui on 2016/12/22 22:52.
 */
public interface ILauncherPresenter extends IAppPresenter {

    void advertClick();

    void stop();


    interface ILauncherView {

        void onLoadImage(String name, String url);

        void onJumpToWeb(String url);

        void onJumpToBlog(String id);

        void onNormalImage();

        void onJumpToMain();
    }
}
