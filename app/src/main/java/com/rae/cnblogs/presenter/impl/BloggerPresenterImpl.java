package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.IBloggerPresenter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;

import io.reactivex.Observable;

/**
 * 博主
 * Created by ChenRui on 2017/2/24 0024 16:25.
 */
public class BloggerPresenterImpl extends BasePresenter<IBloggerPresenter.IBloggerView> implements IBloggerPresenter {

    private final IFriendsApi mFriendApi;
    private FriendsInfoBean mBloggerInfo;

    public BloggerPresenterImpl(Context context, IBloggerView view) {
        super(context, view);
        mFriendApi = getApiProvider().getFriendApi();
    }

    @Override
    public void start() {
        if (isNotLogin()) {
            mView.onNotLogin();
            return;
        }

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
    }

    @Override
    public void doFollow() {
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
