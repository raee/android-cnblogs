package com.rae.cnblogs.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.DesignTabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogListAdapter;
import com.rae.cnblogs.message.TabEvent;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IHomePresenter;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    private List<CategoryBean> mCategoryBeanList;
    private int mPosition;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.tab_category)
    DesignTabLayout mTabLayout;

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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mPosition = savedInstanceState.getInt("position");
        }
        mHomePresenter = CnblogsPresenterFactory.getHomePresenter(getContext(), this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHomePresenter.start();
    }


    @OnClick(R.id.img_edit_category)
    public void onCategoryClick(View view) {
        if (mCategoryBeanList == null || mCategoryBeanList.size() <= 0) {
            AppUI.failed(getContext(), "请等待分类加载完成");
            return;
        }
        AppRoute.jumpToCategoryForResult(getActivity());
    }

    @Override
    public void onLoadCategory(List<CategoryBean> data) {

        if (!isAdded()) {
            // 还没有加载
            return;
        }

        mCategoryBeanList = data;
        int count = mAdapter == null ? 0 : mAdapter.getCount();

        if (mAdapter == null) {
            mAdapter = new BlogListAdapter(getChildFragmentManager(), data);
            mViewPager.setAdapter(mAdapter);
            mTabLayout.setupWithViewPager(mViewPager);
            mTabLayout.addOnTabSelectedListener(new DesignTabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(DesignTabLayout.Tab tab) {

                }

                @Override
                public void onTabUnselected(DesignTabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(DesignTabLayout.Tab tab) {
                    goTop();
                }
            });
        } else {
            mAdapter.updateDataSet(data);
        }
        if (data.size() < count) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(mPosition);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 分类编辑返回
        if (requestCode == AppRoute.REQ_CODE_CATEGORY && resultCode == Activity.RESULT_OK) {
            mPosition = data != null ? data.getIntExtra("position", 0) : mViewPager.getCurrentItem();
            // 可能没有附加上去，fix bugly #352
            mViewPager.post(new Runnable() {
                @Override
                public void run() {
                    mHomePresenter.start();
                }
            });
        }
    }

    @OnClick(R.id.img_search)
    public void onSearchClick() {
        AppRoute.jumpToSearch(this.getContext());
    }

    @OnClick(R.id.img_actionbar_logo)
    public void onLogoClick() {
        onSearchClick();
    }

    private void goTop() {
        // 返回顶部
        int currentItem = mViewPager.getCurrentItem();
        BlogListFragment fragment = mAdapter.getFragment(currentItem);
        if (fragment != null)
            fragment.scrollToTop();
    }


//    @Override
//    public void onLoadUserInfo(UserInfoBean userInfo) {
//        //加载头像
//        mAvatarView.setVisibility(View.VISIBLE);
//        mNotLoginAvatarView.setVisibility(View.GONE);
//        ImageLoader.getInstance().displayImage(userInfo.getAvatar(), mAvatarView);
//    }

//    @Override
//    public void onLoadNormal() {
//        mAvatarView.setVisibility(View.GONE);
//        mNotLoginAvatarView.setVisibility(View.VISIBLE);
//    }

    @Override
    public void onStop() {
        super.onStop();
        mPosition = mViewPager.getCurrentItem();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", mPosition);
    }

    @Subscribe
    public void onTabEvent(TabEvent event) {
        if (event.getPosition() == 0) {
            mViewPager.post(new Runnable() {
                @Override
                public void run() {
                    goTop();
                }
            });
        }
    }
}
