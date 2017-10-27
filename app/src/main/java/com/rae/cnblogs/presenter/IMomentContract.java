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
    }

    interface View extends IPageView<MomentBean> {

        /**
         * 参考 {@link com.rae.cnblogs.sdk.api.IMomentApi#MOMENT_TYPE_ALL}
         */
        String getType();
    }
}
