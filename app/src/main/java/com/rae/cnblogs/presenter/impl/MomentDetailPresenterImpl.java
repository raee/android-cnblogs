package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.R;
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

    public MomentDetailPresenterImpl(Context context, IMomentDetailContract.View view) {
        super(context, view);
        mMomentApi = CnblogsApiFactory.getInstance(context).getMomentApi();
        mFriendApi = getApiProvider().getFriendApi();
    }

    @Override
    public void start() {
        super.start();
        MomentBean momentInfo = mView.getMomentInfo();
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

    @Override
    public void loadMore() {
        // TODO:闪存评论加载更多
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
            }
        });
    }

    @Override
    public boolean isFollowed() {
        return mBloggerInfo != null && mBloggerInfo.isFollowed();
    }
}
