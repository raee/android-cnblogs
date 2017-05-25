package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.R;
import com.rae.cnblogs.presenter.IBloggerPresenter;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

/**
 * 博主
 * Created by ChenRui on 2017/2/24 0024 16:25.
 */
public class BloggerPresenterImpl extends BasePresenter<IBloggerPresenter.IBloggerView> implements IBloggerPresenter, ApiUiListener<FriendsInfoBean> {

    private final IFriendsApi mFriendApi;
    private FriendsInfoBean mBloggerInfo;
    private ApiUiListener<Void> mFollowListener = new ApiUiListener<Void>() {
        @Override
        public void onApiFailed(ApiException e, String msg) {
            mView.onFollowFailed(msg);
        }

        @Override
        public void onApiSuccess(Void aVoid) {
            if (mBloggerInfo != null) {
                mBloggerInfo.setFollowed(!mBloggerInfo.isFollowed());
            }
            mView.onFollowSuccess();
        }
    };

    public BloggerPresenterImpl(Context context, IBloggerView view) {
        super(context, view);
        mFriendApi = getApiProvider().getFriendApi();
    }

    @Override
    public void start() {
        mFriendApi.getFriendsInfo(mView.getBlogApp(), this);
    }

    @Override
    public void onApiFailed(ApiException e, String msg) {
        mView.onLoadBloggerInfoFailed(msg);
    }

    @Override
    public void onApiSuccess(FriendsInfoBean friendsInfoBean) {
        mBloggerInfo = friendsInfoBean;
        mView.onLoadBloggerInfo(friendsInfoBean);
    }

    @Override
    public void doFollow() {
        if (mBloggerInfo == null) {
            mView.onFollowFailed(getString(R.string.tips_blogger_follow_not_info));
            return;
        }
        if (mBloggerInfo.isFollowed()) {
            mFriendApi.unFollow(mBloggerInfo.getUserId(), mFollowListener);
        } else {
            mFriendApi.follow(mBloggerInfo.getUserId(), mFollowListener);
        }
    }

    @Override
    public boolean isFollowed() {
        return mBloggerInfo != null && mBloggerInfo.isFollowed();
    }

    @Override
    public void destroy() {
        super.destroy();
        mFollowListener = null;
    }
}
