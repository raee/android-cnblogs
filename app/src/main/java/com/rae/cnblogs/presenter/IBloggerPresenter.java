package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.FriendsInfoBean;

/**
 * 博主逻辑处理
 * Created by ChenRui on 2017/2/24 0024 16:22.
 */
public interface IBloggerPresenter extends IAppPresenter {

    /**
     * 关注/取消关注
     */
    void doFollow();

    boolean isFollowed();

    interface IBloggerView {
        void onLoadBloggerInfo(FriendsInfoBean userInfo);

        String getBlogApp();

        void onLoadBloggerInfoFailed(String msg);

        void onFollowFailed(String msg);

        void onFollowSuccess();
    }
}
