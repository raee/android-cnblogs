package com.rae.cnblogs.presenter;

import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;

import java.util.List;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 16:59.
 */
public interface IMomentDetailContract {

    interface Presenter extends IAppPresenter {

        void loadMore();

    }

    interface View {

        MomentBean getMomentInfo();

        /**
         * 没有评论信息
         */
        void onEmptyComment(String message);

        /**
         * 加载评论
         */
        void onLoadComments(List<MomentCommentBean> data);
    }
}
