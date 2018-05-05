package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.feedback.Comment;
import com.avos.avoscloud.feedback.FeedbackThread;
import com.kyleduo.switchbutton.SwitchButton;
import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.ThemeCompat;
import com.rae.cnblogs.message.UserInfoEvent;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.CnblogsReportException;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IUserApi;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;

/**
 * 我的
 * Created by ChenRui on 2017/1/19 00:13.
 */
public class MineFragment extends BasicFragment {

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
    @BindView(R.id.img_system_message_badge)
    View mSystemMessageBadgeView;
    @BindView(R.id.img_feedback_badge)
    View mFeedbackBadgeView;

    @BindView(R.id.sb_night_mode)
    SwitchButton mNightModeButton;


    @Override
    protected int getLayoutId() {
        return R.layout.fm_mine;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onCreateView(View view) {
        super.onCreateView(view);
        mNightModeButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ThemeCompat.switchNightMode();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // 加载用户信息
        loadUserInfo();

        // 意见反馈的回复
        checkFeedbackMessage();

        // 获取系统消息
        RxObservable.create(CnblogsApiFactory.getInstance(getContext()).getRaeServerApi().getMessageCount(), "MineFragment")
                .subscribe(new ApiDefaultObserver<Integer>() {
                    @Override
                    protected void onError(String message) {
                    }

                    @Override
                    protected void accept(Integer integer) {
                        if (integer == null || getContext() == null) return;
                        mSystemMessageBadgeView.setVisibility(config().getMessageCount() != integer ? View.VISIBLE : View.INVISIBLE);
                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 夜间模式处理
        mNightModeButton.setCheckedNoEvent(ThemeCompat.isNight());
    }

    private boolean isNotLogin() {
        return !UserProvider.getInstance().isLogin();
    }

    /**
     * 加载用户信息
     */
    private void loadUserInfo() {
        if (isNotLogin()) {
            loadNotLoginUI();
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
                        loadNotLoginUI();
                        AppUI.toastInCenter(getContext(), getString(R.string.login_expired));
                        AppRoute.routeToLogin(getContext());
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

    private void loadNotLoginUI() {
        mAvatarView.setImageResource(R.drawable.boy);
        mDisplayNameView.setVisibility(View.GONE);
        mNoLoginTextView.setVisibility(View.VISIBLE);
        mFansAndFollowLayout.setVisibility(View.GONE);
        mFansCountView.setText("0");
        mFollowCountView.setText("0");
    }

    private void onLoadUserInfo(UserInfoBean user) {
        RaeImageLoader.displayHeaderImage(user.getAvatar(), mAvatarView);
        mDisplayNameView.setText(user.getDisplayName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        RxObservable.dispose("MineFragment");
    }

    @OnClick(R.id.layout_account_fans)
    public void onFansClick() {
        if (isNotLogin()) {
            AppRoute.routeToLogin(getActivity());
            return;
        }
        AppRoute.routeToFans(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
    }

    @OnClick(R.id.layout_account_follow)
    public void onFollowClick() {
        if (isNotLogin()) {
            AppRoute.routeToLogin(getActivity());
            return;
        }
        AppRoute.routeToFollow(this.getContext(), getString(R.string.me), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
    }


    @OnClick({R.id.img_blog_avatar, R.id.tv_mine_name, R.id.tv_no_login})
    public void onLoginClick() {
        // 没有登录跳登录
        if (isNotLogin()) {
            AppRoute.routeToLogin(getActivity());
            return;
        }

        AppRoute.routeToBlogger(getContext(), UserProvider.getInstance().getLoginUserInfo().getBlogApp());
    }

    /**
     * 我的收藏
     */
    @OnClick(R.id.ll_favorites)
    public void onFavoritesClick() {
        AppMobclickAgent.onClickEvent(getContext(), "Favorites");
        // 没有登录跳登录
        if (isNotLogin()) {
            AppRoute.routeToLogin(getActivity());
            return;
        }
        AppRoute.jumpToFavorites(this.getActivity());
    }

    /**
     * 问题反馈
     */
    @OnClick(R.id.ll_feedback)
    public void onFeedbackClick() {
        mFeedbackBadgeView.setVisibility(View.INVISIBLE);
        AppMobclickAgent.onClickEvent(getContext(), "Feedback");
        AppRoute.routeToFeedback(getContext());
    }

    /**
     * 浏览记录
     */
    @OnClick(R.id.ll_history)
    public void onHistoryClick() {
        AppMobclickAgent.onClickEvent(getContext(), "History");
        AppRoute.routeToHistory(getContext());
    }


    /**
     * 系统消息
     */
    @OnClick(R.id.ll_system_message)
    public void onSystemMessageClick() {
        AppMobclickAgent.onClickEvent(getContext(), "SystemMessage");
        mSystemMessageBadgeView.setVisibility(View.INVISIBLE);
        AppRoute.routeToSystemMessage(this.getContext());
    }

    /**
     * 设置
     */
    @OnClick(R.id.ll_setting)
    public void onSettingClick() {
        AppRoute.jumpToSetting(this.getContext());
    }

    /**
     * 夜间模式
     */
    @OnClick(R.id.ll_night)
    public void onNightClick() {
        AppMobclickAgent.onClickEvent(getContext(), "NightMode");
        mNightModeButton.performClick();
    }


    /**
     * 检查是否有新的意见反馈回复消息
     */
    private void checkFeedbackMessage() {
        try {
            final int originalCount = FeedbackThread.getInstance().getCommentsList().size();
            FeedbackThread.getInstance().sync(new FeedbackThread.SyncCallback() {
                @Override
                public void onCommentsSend(List<Comment> list, AVException e) {

                }

                @Override
                public void onCommentsFetch(List<Comment> list, AVException e) {
                    mFeedbackBadgeView.setVisibility(list.size() > originalCount ? View.VISIBLE : View.INVISIBLE);
                }
            });
        } catch (Exception e) {
            CrashReport.postCatchedException(new CnblogsReportException("意见反馈发生异常！", e));
        }
    }

    @Subscribe
    public void onEvent(UserInfoEvent event) {
        loadUserInfo();
    }
}
