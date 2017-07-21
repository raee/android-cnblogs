package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.dialog.impl.HintCardDialog;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.umeng.analytics.MobclickAgent;

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

    @BindView(R.id.ll_logout)
    View mLogoutButton;

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
        // TODO:调试登录
//        UserProvider.getInstance().debugLogin();

        return !UserProvider.getInstance().isLogin();
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        if (isNotLogin()) {
            mAvatarView.setImageResource(R.drawable.ic_default_user_avatar);
            mDisplayNameView.setVisibility(View.GONE);
            mNoLoginTextView.setVisibility(View.VISIBLE);
            mFansAndFollowLayout.setVisibility(View.GONE);
            mFansCountView.setText("0");
            mFollowCountView.setText("0");
            mLogoutButton.setVisibility(View.GONE);
            return;
        }

        mDisplayNameView.setVisibility(View.VISIBLE);
        mNoLoginTextView.setVisibility(View.GONE);
        mFansAndFollowLayout.setVisibility(View.VISIBLE);
        // 打印 登录信息

        Log.i("rae", "登录信息：" + android.webkit.CookieManager.getInstance().getCookie("www.cnblogs.com"));

        mLogoutButton.setVisibility(View.VISIBLE);

        UserInfoBean user = UserProvider.getInstance().getLoginUserInfo();
        onLoadUserInfo(user);
        Observable<FriendsInfoBean> observable = CnblogsApiFactory.getInstance(this.getContext()).getFriendApi().getFriendsInfo(user.getBlogApp());
        RxObservable.create(observable, "MineFragment").subscribe(new ApiDefaultObserver<FriendsInfoBean>() {
            @Override
            protected void onError(String message) {
                AppUI.toastInCenter(getContext(), message);
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
                        UserProvider.getInstance().setLoginUserInfo(userInfoBean);
                        // onLoadUserInfo(userInfoBean);
                    }
                });
    }

    private void onLoadUserInfo(UserInfoBean user) {
        ImageLoader.getInstance().displayImage(user.getAvatar(), mAvatarView, RaeImageLoader.headerWithoutFadeInOption());
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
    @OnClick(R.id.ll_issue)
    public void onIssueClick() {
        AppRoute.jumpToWeb(this.getContext(), getString(R.string.github_issue_url));
    }

    /**
     * 开源项目
     */
    @OnClick(R.id.ll_github)
    public void onOpenSourceClick() {
        AppRoute.jumpToWeb(this.getContext(), getString(R.string.github_url));
    }

    /**
     * 退出登录
     */
    @OnClick(R.id.btn_logout)
    public void onLogoutClick() {

        HintCardDialog dialog = new HintCardDialog(getContext());
        dialog.setMessage(getString(R.string.tips_logout));
        dialog.setOnEnSureListener(new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                dialog.dismiss();
                MobclickAgent.onProfileSignOff();
                UserProvider.getInstance().logout();
                loadUserInfo();
            }
        });
        dialog.showCloseButton();
        dialog.setEnSureText(getString(R.string.logout));
        dialog.show();

    }
}
