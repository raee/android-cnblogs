package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogListAdapter;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IHomePresenter;
import com.rae.cnblogs.sdk.bean.CategoryBean;
import com.rae.cnblogs.sdk.bean.UserInfoBean;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


/**
 * 首页
 * Created by ChenRui on 2016/12/1 22:34.
 */
public class HomeFragment extends BaseFragment implements IHomePresenter.IHomeView {

    private BlogListAdapter mAdapter;
    private IHomePresenter mHomePresenter;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.tab_category)
    TabLayout mTabLayout;

    @BindView(R.id.vp_blog_list)
    ViewPager mViewPager;

//    @BindView(R.id.img_actionbar_avatar)
//    ImageView mAvatarView;
//    @BindView(R.id.img_actionbar_avatar_not_login)
//    ImageView mNotLoginAvatarView;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_home;
    }

    @Override
    public void onResume() {
        super.onResume();
        mHomePresenter.start();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHomePresenter = CnblogsPresenterFactory.getHomePresenter(getContext(), this);
    }

    @Override
    public void onLoadCategory(List<CategoryBean> data) {

        if (mAdapter == null) {
            mAdapter = new BlogListAdapter(getChildFragmentManager(), data);
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
        }

        mAdapter.notifyDataSetChanged();
    }

//    @OnClick(R.id.img_actionbar_avatar_not_login)
//    public void onNotLoginAvatarClick() {
//        // 登录
//        AppRoute.jumpToLogin(getContext());
//    }
//
//    @OnClick(R.id.img_actionbar_avatar)
//    public void onAvatarClick() {
//        // 跳到个人中心
//        AppRoute.jumpToUserCenter(getContext());
//    }

    @OnClick(R.id.img_actionbar_logo)
    public void onLogoClick() {
        // 返回顶部
        int currentItem = mViewPager.getCurrentItem();
        BlogListFragment fragment = (BlogListFragment) mAdapter.getItem(currentItem);
        fragment.scrollToTop();
    }


    @Override
    public void onLoadUserInfo(UserInfoBean userInfo) {
        // 加载头像
//        mAvatarView.setVisibility(View.VISIBLE);
//        mNotLoginAvatarView.setVisibility(View.GONE);
//        ImageLoader.getInstance().displayImage(userInfo.getAvatar(), mAvatarView);
    }

    @Override
    public void onLoadNormal() {
//        mAvatarView.setVisibility(View.GONE);
//        mNotLoginAvatarView.setVisibility(View.VISIBLE);
    }
}
