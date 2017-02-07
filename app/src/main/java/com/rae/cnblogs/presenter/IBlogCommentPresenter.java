package com.rae.cnblogs.presenter;

import android.support.annotation.Nullable;

import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogCommentBean;

import java.util.List;

/**
 * 评论
 * Created by ChenRui on 2016/12/26 0026 8:48.
 */
public interface IBlogCommentPresenter extends IRaePresenter {

    void loadMore();

    /**
     * 发布评论
     *
     * @param parent 要回复的评论，可以为空。为空则发表新评论
     */
    void post(@Nullable BlogCommentBean parent);

    void delete(BlogCommentBean item);

    interface IBlogCommentView {
        void onLoadCommentSuccess(List<BlogCommentBean> data);

        BlogBean getBlog();

        void onLoadCommentEmpty();

        void onLoadMoreCommentEmpty();

        /**
         * 获取评论内容
         *
         * @return
         */
        String getCommentContent();

        /**
         * 发表评论失败
         *
         * @param msg
         */
        void onPostCommentFailed(String msg);

        /**
         * 发表评论成功
         */
        void onPostCommentSuccess();

        /**
         * 是否引用评论
         *
         * @return
         */
        boolean enableReferenceComment();

        void onDeleteCommentSuccess(BlogCommentBean item);

        void onDeleteCommentFailed(String msg);
    }
}
