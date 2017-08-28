package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
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

    @BindView(R.id.tv_no_login)
    View mNoLoginTextView;

    @BindView(R.id.ll_follow_fans)
    View mFansAndFollowLayout;


    @Override
    protected int getLayoutId() {
        return R.layout.fm_mine;
    }

    @Override
    protected void onCreateView(View view) {
        super.onCreateView(view);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 模拟登录
    }

    @Override
    public void onResume() {
        super.onResume();
        loadUserInfo();
    }

    private boolean isNotLogin() {
        return !UserProvider.getInstance().isLogin();
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        if (isNotLogin()) {
            mAvatarView.setImageResource(R.drawable.boy);
            mDisplayNameView.setVisibility(View.GONE);
            mNoLoginTextView.setVisibility(View.VISIBLE);
            mFansAndFollowLayout.setVisibility(View.GONE);
            mFansCountView.setText("0");
            mFollowCountView.setText("0");
            return;
        }

        mDisplayNameView.setVisibility(View.VISIBLE);
        mNoLoginTextView.setVisibility(View.GONE);
        mFansAndFollowLayout.setVisibility(View.VISIBLE);

        UserInfoBean user = UserProvider.getInstance().getLoginUserInfo();
        onLoadUserInfo(user);
        Observable<FriendsInfoBean> observable = CnblogsApiFactory.getInstance(this.getContext()).getFriendApi().getFriendsInfo(user.getBlogApp());
        RxObservable.create(observable, "MineFragment").subscribe(new ApiDefaultObserver<FriendsInfoBean>() {
            @Override
            protected void onError(String message) {
                // AppUI.toastInCenter(getContext(), message);
                mFollowCountView.setText("--");
                mFansCountView.setText("--");
            }

            @Override
            protected void accept(FriendsInfoBean data) {
                mFollowCountView.setText(data.getFollows());
                mFansCountView.setText(data.getFans());
            }
        });

        // 重新刷新用户信息
        IUserApi userApi = CnblogsApiFactory.getInstance(getContext()).getUserApi();
        RxObservable.create(userApi.getUserInfo(user.getBlogApp()), "MineFragment")
                .subscribe(new ApiDefaultObserver<UserInfoBean>() {
                    @Override
                    protected void onError(String message) {
                        // 不做处理
                    }

                    @Override
                    protected void onLoginExpired() {
                        // 登录过期
                        AppUI.toastInCenter(getContext(), getString(R.string.login_expired));
                        AppRoute.jumpToLogin(getContext());
                    }

                    @Override
                    protected void accept(UserInfoBean userInfoBean) {
                        // 更新用户信息
                        if (!TextUtils.isEmpty(userInfoBean.getUserId())) {
                            UserProvider.getInstance().setLoginUserInfo(userInfoBean);
                        }
                    }
                });
    }

    private void onLoadUserInfo(UserInfoBean user) {
        RaeImageLoader.displayHeaderImage(user.getAvatar(), mAvatarView);
        mDisplayNameView.setText(user.getDisplayName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxObservable.dispose("MineFragment");
    }

    @OnClick(R.id.layout_account_fans)
    public void onFansClick() {
        if (isNotLogin()) {
            AppRoute.jumpToLogin(getActivity());
            return;
        }
        AppRoute.jumpToFans(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getUserId());
    }

    @OnClick(R.id.layout_account_follow)
    public void onFollowClick() {
        if (isNotLogin()) {
            AppRoute.jumpToLogin(getActivity());
            return;
        }
        AppRoute.jumpToFollow(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getUserId());
    }


    @OnClick({R.id.img_blog_avatar, R.id.tv_mine_name, R.id.tv_no_login})
    public void onLoginClick() {
        // 没有登录跳登录
        if (isNotLogin()) {
            AppRoute.jumpToLogin(getActivity());
            return;
        }

        AppRoute.jumpToBlogger(getContext(), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
    }

    /**
     * 我的收藏
     */
    @OnClick(R.id.ll_favorites)
    public void onFavoritesClick() {
        // 没有登录跳登录
        if (isNotLogin()) {
            AppRoute.jumpToLogin(getActivity());
            return;
        }
        AppRoute.jumpToFavorites(this.getActivity());
    }

    /**
     * 问题反馈
     */
    @OnClick(R.id.ll_feedback)
    public void onFeedbackClick() {
        AppRoute.jumpToFeedback(getContext());
    }


    /**
     * 设置
     */
    @OnClick(R.id.ll_setting)
    public void onSettingClick() {
        AppRoute.jumpToSetting(this.getContext());
    }

}
