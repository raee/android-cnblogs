package com.rae.cnblogs.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.core.sdk.ApiUiListener;
import com.rae.core.sdk.exception.ApiException;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 我的
 * Created by ChenRui on 2017/1/19 00:13.
 */
public class MineFragment extends BaseFragment {

    public static MineFragment newInstance() {
        MineFragment fragment = new MineFragment();
        return fragment;
    }

    @BindView(R.id.img_blog_avatar)
    ImageView mAvatarView;

    @BindView(R.id.tv_mine_name)
    TextView mDisplayNameView;
    @BindView(R.id.tv_follow_count)
    TextView mFollowCountView;
    @BindView(R.id.tv_fans_count)
    TextView mFansCountView;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_mine;
    }

    @Override
    protected void onCreateView(View view) {
        super.onCreateView(view);
        UserInfoBean user = UserProvider.getInstance().getLoginUserInfo();
        if (user != null) {
            ImageLoader.getInstance().displayImage(user.getAvatar(), mAvatarView, RaeImageLoader.headerOption());
            mDisplayNameView.setText(user.getDisplayName());
            CnblogsApiFactory.getInstance(this.getContext()).getFriendApi().getFriendsInfo(user.getBlogApp(), new ApiUiListener<FriendsInfoBean>() {
                @Override
                public void onApiFailed(ApiException ex, String msg) {

                }

                @Override
                public void onApiSuccess(FriendsInfoBean data) {
                    mFollowCountView.setText(data.getFollows());
                    mFansCountView.setText(data.getFans());
                }
            });
        }


    }

    @OnClick(R.id.layout_account_fans)
    public void onFansClick() {
        AppRoute.jumpToFans(this.getContext());
    }

    @OnClick(R.id.layout_account_follow)
    public void onFollowClick() {
        AppRoute.jumpToFollow(this.getContext());
    }


}
