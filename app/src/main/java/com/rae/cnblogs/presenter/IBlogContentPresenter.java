package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.db.model.UserBlogInfo;

/**
 * 博客查看
 * Created by ChenRui on 2016/12/7 22:02.
 */
public interface IBlogContentPresenter {

    /**
     * 加载博客内容
     */
    void loadContent();

    /**
     * 点赞
     *
     * @param isCancel TRUE = 取消点赞
     */
    void doLike(boolean isCancel);

    /**
     * 收藏
     *
     * @param isCancel TRUE = 取消收藏
     */
    void doBookmarks(boolean isCancel);

    interface IBlogContentView {

        Blog getBlog();

        void onLoadContentSuccess(Blog blog);

        void onLoadContentFailed(String msg);

        /**
         * 点赞失败回调
         *
         * @param isCancel 是否为取消点赞
         * @param msg      错误消息
         */
        void onLikeError(boolean isCancel, String msg);

        /**
         * 点赞成功
         *
         * @param isCancel 是否为取消点赞
         */
        void onLikeSuccess(boolean isCancel);

        /**
         * 收藏失败回调
         *
         * @param isCancel 是否为取消收藏
         * @param msg      错误消息
         */
        void onBookmarksError(boolean isCancel, String msg);

        /**
         * 收藏成功
         *
         * @param isCancel 是否为取消收藏
         */
        void onBookmarksSuccess(boolean isCancel);

        /**
         * 需要登录
         */
        void onNeedLogin();

        /**
         * 加载用户的博客信息成功
         *
         * @param infoModel 实体
         */
        void onLoadBlogInfoSuccess(UserBlogInfo infoModel);

        BlogType getBlogType();
    }
}
