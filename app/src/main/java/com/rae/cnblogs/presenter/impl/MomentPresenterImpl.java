package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.PageObservable;
import com.rae.cnblogs.message.PostMomentEvent;
import com.rae.cnblogs.message.UserInfoEvent;
import com.rae.cnblogs.presenter.IMomentContract;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.swift.Rx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * moment
 * Created by ChenRui on 2017/10/27 0027 10:57.
 */
public class MomentPresenterImpl extends BasePresenter<IMomentContract.View> implements IMomentContract.Presenter {

    private IMomentApi mMomentApi;
    private int mReplyMeCount;
    private int mAtMeCount;

    private PageObservable<MomentBean> mPageObservable;


    public MomentPresenterImpl(Context context, IMomentContract.View view) {
        super(context, view);
        EventBus.getDefault().register(this);
        mMomentApi = CnblogsApiFactory.getInstance(context).getMomentApi();
        mPageObservable = new PageObservable<MomentBean>(view) {
            @Override
            protected Observable<List<MomentBean>> onCreateObserver(int page) {
                return createObservable(mMomentApi.getMoments(mView.getType(), page, System.currentTimeMillis()));
            }
        };
    }

    @Override
    public void start() {
        super.start();

        if (isLogin()) {

            // 查询回复我的数量
            createObservable(mMomentApi.queryReplyCount(System.currentTimeMillis()))
                    .flatMap(new Function<String, ObservableSource<String>>() {
                        @Override
                        public ObservableSource<String> apply(String s) throws Exception {
                            mReplyMeCount = Rx.parseInt(s);
                            return createObservable(mMomentApi.queryAtMeCount(System.currentTimeMillis()));
                        }
                    })
                    .subscribe(new ApiDefaultObserver<String>() {
                        @Override
                        protected void onError(String message) {
                            mView.onMessageCountChanged(mReplyMeCount, mAtMeCount);
                        }

                        @Override
                        protected void accept(String s) {
                            if (mView == null) return;
                            mAtMeCount = Rx.parseInt(s);
                            mView.onMessageCountChanged(mReplyMeCount, mAtMeCount);
                        }
                    });
        }

        if (!isLogin() && !TextUtils.equals(mView.getType(), IMomentApi.MOMENT_TYPE_ALL)) {
            mView.onLoginExpired();
        } else {
            mPageObservable.start();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
        EventBus.getDefault().unregister(this);
        mPageObservable.destroy();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(UserInfoEvent event) {
        // 重新加载数据
        start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(PostMomentEvent event) {
        // 重新加载数据
        if (event.isDeleted()) {
            start();
        }
    }


    @Override
    public void loadMore() {
        mPageObservable.loadMore();
    }

//    @Override
//    public void delete(String ingId) {
//        if (isNotLogin()) {
//            mView.onDeleteMomentFailed(getString(R.string.login_expired));
//            return;
//        }
//        if (TextUtils.isEmpty(ingId)) {
//            mView.onDeleteMomentFailed("闪存ID为空");
//            return;
//        }
//
//        createObservable(mMomentApi.deleteMoment(ingId))
//                .subscribe(new ApiDefaultObserver<Empty>() {
//                    @Override
//                    protected void onError(String message) {
//                        mView.onDeleteMomentFailed(message);
//                    }
//
//                    @Override
//                    protected void accept(Empty empty) {
//                        mView.onDeleteMomentSuccess();
//                    }
//                });
//    }
}
