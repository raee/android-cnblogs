package com.rae.cnblogs.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.rae.cnblogs.R;
import com.rae.cnblogs.message.PostMomentEvent;
import com.rae.cnblogs.message.UserInfoEvent;
import com.rae.cnblogs.presenter.IMomentDetailContract;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.swift.Rx;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.Observable;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 16:59.
 */
public class MomentDetailPresenterImpl extends BasePresenter<IMomentDetailContract.View> implements IMomentDetailContract.Presenter {

    private IMomentApi mMomentApi;
    private final IFriendsApi mFriendApi;
    private FriendsInfoBean mBloggerInfo;
    private boolean mRefresh;

    public MomentDetailPresenterImpl(Context context, IMomentDetailContract.View view) {
        super(context, view);
        mMomentApi = CnblogsApiFactory.getInstance(context).getMomentApi();
        mFriendApi = getApiProvider().getFriendApi();
    }

    @Override
    public void start() {
        super.start();
        MomentBean momentInfo = mView.getMomentInfo();

        // 如果自带有评论就不用再发起请求了
        if (Rx.isEmpty(momentInfo.getCommentList()) || mRefresh) {
            loaMomentDetail(momentInfo);
        } else {
            mView.onLoadComments(momentInfo.getCommentList(), false);
        }

        if (isLogin()) {
            // 加载博主信息
            createObservable(mFriendApi.getFriendsInfo(mView.getBlogApp())).subscribe(new ApiDefaultObserver<FriendsInfoBean>() {
                @Override
                protected void onError(String msg) {
                    mView.onLoadBloggerInfoFailed(msg);
                }

                @Override
                protected void accept(FriendsInfoBean friendsInfoBean) {
                    mBloggerInfo = friendsInfoBean;
                    mView.onLoadBloggerInfo(friendsInfoBean);
                }
            });
        } else {
            mView.onLoadBloggerInfoFailed(getString(R.string.login_expired));
        }

    }

    private void loaMomentDetail(MomentBean momentInfo) {
        // 加载详情里面的
        createObservable(mMomentApi.getMomentDetail(momentInfo.getUserAlias(), momentInfo.getId(), System.currentTimeMillis()))
                .subscribe(new ApiDefaultObserver<MomentBean>() {
                    @Override
                    protected void onError(String message) {
                        // 加载失败后，默认加载
                        loadComments(mView.getMomentInfo());
                    }

                    @Override
                    protected void accept(MomentBean momentBean) {
                        if (Rx.isEmpty(momentBean.getCommentList())) {
                            loadComments(mView.getMomentInfo());
                        } else {
                            mView.onLoadComments(momentBean.getCommentList(), false);
                        }
                    }
                });
    }

    private void loadComments(MomentBean momentInfo) {
        createObservable(mMomentApi.getMomentSingleComments(momentInfo.getId(), momentInfo.getUserAlias(), System.currentTimeMillis()))
                .subscribe(new ApiDefaultObserver<List<MomentCommentBean>>() {
                    @Override
                    protected void onError(String message) {
                        mView.onEmptyComment(message);
                    }

                    @Override
                    protected void accept(List<MomentCommentBean> momentCommentBeans) {
                        if (Rx.isEmpty(momentCommentBeans)) {
                            mView.onEmptyComment(getString(R.string.empty_comment));
                            return;
                        }

                        // 判断是否还有更多评论
                        MomentCommentBean commentBean = momentCommentBeans.get(momentCommentBeans.size() - 1);
                        boolean hasMore = "more".equals(commentBean.getId());
                        if (hasMore)
                            momentCommentBeans.remove(commentBean);

                        mView.onLoadComments(momentCommentBeans, hasMore);
                    }
                });
    }

    @Override
    public void refresh() {
        mRefresh = true;
        start();
    }

    @Override
    public void loadMore() {
        // 目前没有发现有多页的情况，就重新刷新当前页面
        refresh();
    }

    @Override
    public void postComment(String ingId, String userId, String commentId, String content) {
        createObservable(mMomentApi.postComment(ingId, userId, commentId, content))
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        mView.onPostCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        mView.onPostCommentSuccess();
                    }
                });
    }

    @Override
    public void follow() {
        if (mBloggerInfo == null) {
            mView.onFollowFailed(getString(R.string.tips_blogger_follow_not_info));
            return;
        }
        Observable<Empty> observable;
        if (mBloggerInfo.isFollowed()) {
            observable = mFriendApi.unFollow(mBloggerInfo.getUserId());
        } else {
            observable = mFriendApi.follow(mBloggerInfo.getUserId());
        }

        createObservable(observable).subscribe(new ApiDefaultObserver<Empty>() {
            @Override
            protected void onError(String message) {
                mView.onFollowFailed(message);
            }

            @Override
            protected void accept(Empty empty) {
                if (mBloggerInfo != null) {
                    mBloggerInfo.setFollowed(!mBloggerInfo.isFollowed());
                }
                mView.onFollowSuccess();
                // 通知更新用户信息
                EventBus.getDefault().post(new UserInfoEvent());
            }
        });
    }

    @Override
    public boolean isFollowed() {
        return mBloggerInfo != null && mBloggerInfo.isFollowed();
    }

    @Override
    public void deleteComment(String commentId) {
        createObservable(mMomentApi.deleteMomentComment(commentId))
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        mView.onDeleteCommentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        // 重新加载数据
                        loaMomentDetail(mView.getMomentInfo());
                    }
                });
    }

    @Override
    public void deleteMoment() {
        String ingId = mView.getMomentInfo().getId();
        if (isNotLogin()) {
            mView.onDeleteMomentFailed(getString(R.string.login_expired));
            return;
        }
        if (TextUtils.isEmpty(ingId)) {
            mView.onDeleteMomentFailed("闪存ID为空");
            return;
        }

        createObservable(mMomentApi.deleteMoment(ingId))
                .subscribe(new ApiDefaultObserver<Empty>() {
                    @Override
                    protected void onError(String message) {
                        mView.onDeleteMomentFailed(message);
                    }

                    @Override
                    protected void accept(Empty empty) {
                        PostMomentEvent postMomentEvent = new PostMomentEvent();
                        postMomentEvent.setDeleted(true);
                        EventBus.getDefault().post(postMomentEvent);
                        mView.onDeleteMomentSuccess();
                    }
                });
    }
}
