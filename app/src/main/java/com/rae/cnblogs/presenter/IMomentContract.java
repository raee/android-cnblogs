package com.rae.cnblogs.presenter;

import com.rae.cnblogs.IPageView;
import com.rae.cnblogs.sdk.bean.MomentBean;

/**
 * 闪存
 * Created by ChenRui on 2017/10/27 0027 10:54.
 */
public interface IMomentContract {

    interface Presenter extends IAppPresenter {
        void loadMore();

//        void delete(String ingId);
    }

    interface View extends IPageView<MomentBean> {

        /**
         * 参考 {@link com.rae.cnblogs.sdk.api.IMomentApi#MOMENT_TYPE_ALL}
         */
        String getType();

//        /**
//         * 删除失败
//         */
//        void onDeleteMomentFailed(String msg);
//
//        /**
//         * 删除成功
//         */
//        void onDeleteMomentSuccess();

        /**
         * 有回复我的消息数量
         */
        void onReplyCountChanged(int number);

        /**
         * 有提到我的数量
         */
        void onAtMeCountChanged(int number);
    }
}
