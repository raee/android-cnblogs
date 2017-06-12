package com.rae.cnblogs.fragment;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * 我的
 * Created by ChenRui on 2017/1/19 00:13.
 */
public class MineFragment extends BaseFragment {

    public static MineFragment newInstance() {
        return new MineFragment();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        loadUserInfo();
    }

    private boolean isNotLogin() {
        return UserProvider.getInstance().getLoginUserInfo() == null;
    }

    private void loadUserInfo() {
        if (isNotLogin()) {
            return;
        }

        UserInfoBean user = UserProvider.getInstance().getLoginUserInfo();
        ImageLoader.getInstance().displayImage(user.getAvatar(), mAvatarView, RaeImageLoader.headerOption());
        mDisplayNameView.setText(user.getDisplayName());
        Observable<FriendsInfoBean> observable = CnblogsApiFactory.getInstance(this.getContext()).getFriendApi().getFriendsInfo(user.getBlogApp());
        RxObservable.create(observable).subscribe(new ApiDefaultObserver<FriendsInfoBean>() {
            @Override
            protected void onError(String message) {

            }

            @Override
            protected void accept(FriendsInfoBean data) {
                mFollowCountView.setText(data.getFollows());
                mFansCountView.setText(data.getFans());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxObservable.dispose();
    }

    @OnClick(R.id.layout_account_fans)
    public void onFansClick() {
        if (isNotLogin()) {
            return;
        }
        AppRoute.jumpToFans(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getUserId());
    }

    @OnClick(R.id.layout_account_follow)
    public void onFollowClick() {
        if (isNotLogin()) {
            return;
        }
        AppRoute.jumpToFollow(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getUserId());
    }


}
