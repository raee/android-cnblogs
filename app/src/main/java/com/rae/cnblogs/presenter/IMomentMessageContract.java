package com.rae.cnblogs.presenter;

import com.rae.cnblogs.IPageView;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:54.
 */
public interface IMomentMessageContract {

    interface Presenter extends IAppPresenter {
        void loadMore();

        void postComment(String ingId, String userId, String commentId, String content);
    }

    interface View extends IPageView<MomentCommentBean> {

        /**
         * 评论失败
         */
        void onPostCommentFailed(String message);

        /**
         * 评论成功
         */
        void onPostCommentSuccess();
    }
}
