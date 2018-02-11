package com.rae.cnblogs.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.DesignTabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.jcodecraeer.xrecyclerview.AppBarStateChangeListener;
import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.GlideApp;
import com.rae.cnblogs.R;
import com.rae.cnblogs.fragment.BlogListFragment;
import com.rae.cnblogs.message.UserInfoEvent;
import com.rae.cnblogs.model.FeedListFragment;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IBloggerPresenter;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.bean.FriendsInfoBean;
import com.rae.swift.app.RaeFragmentAdapter;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * blogger info
 * Created by ChenRui on 2017/2/9 0009 10:02.
 */
public class BloggerActivity extends SwipeBackBaseActivity implements IBloggerPresenter.IBloggerView, DesignTabLayout.OnTabSelectedListener {

    @BindView(R.id.img_background)
    ImageView mBackgroundView;

    @BindView(R.id.img_blog_avatar)
    ImageView mAvatarView;

    @BindView(R.id.tv_blogger_name)
    TextView mBloggerNameView;

    @BindView(R.id.tv_follow_count)
    TextView mFollowCountView;

    @BindView(R.id.tv_fans_count)
    TextView mFansCountView;

    @BindView(R.id.tv_age)
    TextView mSnsAgeView;

    @BindView(R.id.btn_blogger_follow)
    Button mFollowView;

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.vp_blogger)
    ViewPager mViewPager;

    @BindView(R.id.tab_category)
    DesignTabLayout mTabLayout;

    @BindView(R.id.layout_account_fans)
    View mFansLayout;

    @BindView(R.id.layout_account_follow)
    View mFollowLayout;

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @BindView(R.id.app_bar)
    AppBarLayout mAppBarLayout;

//    @BindView(R.id.view_bg_holder)
//    View mBloggerBackgroundView;

    @BindView(R.id.pb_blogger_follow)
    View mFollowProgressBar;

//    @BindView(R.id.img_alpha)
//    ImageView mAlphaImageView;

//    @BindView(R.id.layout_blogger)
//    BloggerLayout mBloggerLayout;

    String mBlogApp;

    private FriendsInfoBean mUserInfo;
    private IBloggerPresenter mBloggerPresenter;
    private FeedListFragment mFeedListFragment;
    private BlogListFragment mBlogListFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fm_blogger_info);
        ButterKnife.bind(this);
        mFollowView.setEnabled(false);

        showHomeAsUp(mToolbar);
        mBlogApp = getIntent().getStringExtra("blogApp");

        if (mBlogApp == null) {
            AppUI.failed(this, "BlogApp为空！");
            finish();
            return;
        }

        RaeFragmentAdapter adapter = new RaeFragmentAdapter(getSupportFragmentManager());
        CategoryBean category = new CategoryBean();
        category.setCategoryId(getBlogApp()); // 这里设置blogApp

        mFeedListFragment = FeedListFragment.newInstance(getBlogApp());
        mBlogListFragment = BlogListFragment.newInstance(-1, category, BlogType.BLOGGER);

        adapter.add(getString(R.string.feed), mFeedListFragment);
        adapter.add(getString(R.string.blog), mBlogListFragment);

        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.addOnTabSelectedListener(this);

        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            final Animation mAnimation = AnimationUtils.loadAnimation(getContext(), android.R.anim.fade_in);

            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state) {
//                Log.i("rae", "状态改变：" + state);
                mAnimation.setDuration(800);

                if (state == State.COLLAPSED) {
//                    ThemeCompat.refreshStatusColor(getContext(), true);
//                    setHomeAsUpIndicator(R.drawable.ic_back);
//                    mFollowView.setBackgroundResource(R.drawable.bg_btn_follow_drak);
//                    mFollowView.setTextColor(ContextCompat.getColor(getContext(), R.color.ph2));
                    mTitleView.setVisibility(View.VISIBLE);
                    mTitleView.clearAnimation();
                    mTitleView.startAnimation(mAnimation);
                } else {
                    mTitleView.clearAnimation();
                    mTitleView.setVisibility(View.GONE);
//                    ThemeCompat.refreshStatusColor(getContext(), false);
//                    setHomeAsUpIndicator(R.drawable.ic_back_white);
//                    mFollowView.setBackgroundResource(R.drawable.bg_btn_follow);
//                    mFollowView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                }
            }

            void setHomeAsUpIndicator(int homeAsUpIndicator) {
                if (getSupportActionBar() != null)
                    getSupportActionBar().setHomeAsUpIndicator(homeAsUpIndicator);
            }
        });

//        mBloggerLayout.setOnScrollPercentChangeListener(new BloggerLayout.ScrollPercentChangeListener() {
//            @Override
//            public void onScrollPercentChange(float percent) {
//                mBloggerBackgroundView.setAlpha(percent);
////                mFollowView.setAlpha(percent > 0 ? percent : 1);
//                mTitleView.setAlpha(percent);
//
//                if (percent > 0.5) {
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
//                    mFollowView.setBackgroundResource(R.drawable.bg_btn_follow_drak);
//                    mFollowView.setTextColor(ContextCompat.getColor(getContext(), R.color.ph2));
//                } else {
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back_white);
//                    mFollowView.setBackgroundResource(R.drawable.bg_btn_follow);
//                    mFollowView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//                }
//            }
//        });

//        mBloggerLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                mBloggerLayout.scrollTo(0, 0);
//            }
//        });

        // 获取博主信息
        mBloggerPresenter = CnblogsPresenterFactory.getBloggerPresenter(this, this);
        mBloggerPresenter.start();

    }

    @Override
    protected void onDestroy() {
        if (mBloggerPresenter != null)
            mBloggerPresenter.destroy();
        super.onDestroy();
        mTabLayout.removeOnTabSelectedListener(this);
    }

    @Override
    public void onLoadBloggerInfo(final FriendsInfoBean userInfo) {
        mUserInfo = userInfo;
        mFansLayout.setClickable(true);
        mFollowLayout.setClickable(true);
        mFollowView.setEnabled(true);

        if (!TextUtils.isEmpty(userInfo.getSnsAge())) {
            mSnsAgeView.setText(userInfo.getSnsAge());
        }

        // 如果是自己，则隐藏关注按钮
        if (UserProvider.getInstance().isLogin() && TextUtils.equals(mBlogApp, UserProvider.getInstance().getLoginUserInfo().getBlogApp())) {
            mFollowView.setVisibility(View.INVISIBLE);
        } else {
            mFollowView.setVisibility(View.VISIBLE);
        }

        GlideApp.with(this)
                .load(userInfo.getAvatar())
                .centerCrop()
                .placeholder(R.drawable.boy)
                .into(mAvatarView);

        showAvatar(userInfo.getBlogApp(), userInfo.getAvatar());

        mBloggerNameView.setText(userInfo.getDisplayName());
        mTitleView.setText(userInfo.getDisplayName());
        mFansCountView.setText(userInfo.getFans());
        mFollowCountView.setText(userInfo.getFollows());
        mFollowView.setText(userInfo.isFollowed() ? R.string.cancel_follow : R.string.following);
    }

    private void showAvatar(String blogApp, final String url) {
        if (TextUtils.isEmpty(url) || url.endsWith("simple_avatar.gif")) return;
        // 封面图
        final String coverUrl = String.format("https://files.cnblogs.com/files/%s/app-cover.bmp", blogApp);
        GlideApp.with(this)
                .load(coverUrl)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                        // 如果没有这张封面图就展示默认的
                        GlideApp.with(getContext())
                                .load(url)
                                .centerCrop()
                                .transition(DrawableTransitionOptions.withCrossFade())
                                .apply(RequestOptions.bitmapTransform(new BlurTransformation(12))) // 高斯模糊
                                .into(mBackgroundView);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        // 如果有封面图，则设置进去
                        mBackgroundView.setContentDescription(coverUrl);
                        // 统计
                        AppMobclickAgent.onClickEvent(getContext(), "BloggerCover");
                        return false;
                    }
                })
                .apply(RequestOptions.bitmapTransform(new BlurTransformation(12))) // 高斯模糊
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(mBackgroundView);
    }

    @Override
    public String getBlogApp() {
        return mBlogApp;
    }

    @Override
    public void onLoadBloggerInfoFailed(String msg) {
        AppUI.toast(this, msg);
    }

    @Override
    public void onFollowFailed(String msg) {
//        AppUI.dismiss();
        mFollowProgressBar.setVisibility(ViewPager.GONE);
        mFollowView.setVisibility(View.VISIBLE);
        AppUI.toast(this, msg);
    }

    @Override
    public void onFollowSuccess() {
//        AppUI.dismiss();

        mFollowProgressBar.setVisibility(ViewPager.GONE);
        mFollowView.setVisibility(View.VISIBLE);

        mFollowView.setText(mBloggerPresenter.isFollowed() ? R.string.cancel_follow : R.string.following);
        setResult(RESULT_OK);

        // 发送通知
        EventBus.getDefault().post(new UserInfoEvent());
    }

    @Override
    public void onNotLogin() {
        AppUI.toastInCenter(getContext(), getString(R.string.blogger_need_login));
        AppRoute.jumpToLogin(this);
        finish();
    }

    @Override
    protected void onStatusBarColorChanged() {
    }

    /**
     * 粉丝
     */
    @OnClick(R.id.layout_account_fans)
    public void onFansClick() {
        if (mUserInfo == null) return;
        AppRoute.jumpToFans(this.getContext(), mUserInfo.getDisplayName(), mUserInfo.getBlogApp());
    }


    /**
     * 关注
     */
    @OnClick(R.id.layout_account_follow)
    public void onFollowClick() {
        if (mUserInfo == null) return;
        AppRoute.jumpToFollow(this.getContext(), mUserInfo.getDisplayName(), mUserInfo.getBlogApp());
    }

    @OnClick(R.id.btn_blogger_follow)
    public void onFollowButtonClick() {
        if (mUserInfo == null) return;

//        AppUI.loading(this);
        mFollowProgressBar.setVisibility(ViewPager.VISIBLE);
        mFollowView.setVisibility(View.GONE);
        mBloggerPresenter.doFollow();
    }


    /**
     * 头像点击
     */
    @OnClick({R.id.img_background, R.id.img_blog_avatar})
    public void onAvatarClick(View view) {
        if (mUserInfo == null) return;
        ArrayList<String> images = new ArrayList<>();

        if (view.getId() == R.id.img_background && !TextUtils.isEmpty(view.getContentDescription())) {
            images.add(view.getContentDescription().toString());
        } else {
            images.add(mUserInfo.getAvatar());
        }
        AppRoute.jumpToImagePreview(this, images, 0);
    }

    @Override
    public void onTabSelected(DesignTabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(DesignTabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(DesignTabLayout.Tab tab) {
        takeScrollToTop(tab.getPosition());
    }

    @OnClick(R.id.tool_bar)
    public void onTitleClick() {
        takeScrollToTop(mViewPager.getCurrentItem());
    }

    /**
     * 返回顶部
     */
    private void takeScrollToTop(int position) {
        if (position == 0)
            mFeedListFragment.scrollToTop();
        if (position == 1)
            mBlogListFragment.scrollToTop();
    }
}
