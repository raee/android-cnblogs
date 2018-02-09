package com.rae.cnblogs.fragment;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.DesignTabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;
import com.rae.cnblogs.dialog.impl.DefaultDialog;
import com.rae.cnblogs.message.PostMomentEvent;
import com.rae.cnblogs.message.TabEvent;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.widget.RaeViewPager;
import com.rae.cnblogs.widget.ToolbarToastView;
import com.rae.swift.Rx;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 朋友圈（闪存）
 * Created by ChenRui on 2017/10/26 0026 23:31.
 */
public class SNSFragment extends BaseFragment {

    public static SNSFragment newInstance() {
        return new SNSFragment();
    }

    @BindView(R.id.tab_layout)
    DesignTabLayout mTabLayout;
    @BindView(R.id.view_pager)
    RaeViewPager mViewPager;

    @BindView(R.id.tool_bar_toast_view)
    ToolbarToastView mToastView;

    private SNSFragmentAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_sns;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new SNSFragmentAdapter(view.getContext(), getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        // 相互关联
        mViewPager.addOnPageChangeListener(new DesignTabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.addOnTabSelectedListener(new DesignTabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mTabLayout.addOnTabSelectedListener(new DefaultOnTabSelectedListener());
        mViewPager.setOffscreenPageLimit(3);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DesignTabLayout.Tab tab = mTabLayout.getTabAt(0);
        if (tab != null) {
            tab.select();
        }
    }

    @OnClick(R.id.tv_post)
    public void onPostClick() {
        // 统计闪存发布按钮点击
        AppMobclickAgent.onClickEvent(getContext(), "PostMoment_Enter");
        if (!UserProvider.getInstance().isLogin()) {
            AppRoute.jumpToLogin(getActivity(), 10256);
        } else {
            AppRoute.jumpToPostMoment(getActivity());
        }
    }


    @OnClick(R.id.img_mine)
    public void onMessageClick() {
        if (UserProvider.getInstance().isLogin()) {
            dismissToast();
            AppRoute.jumpToMomentMessage(this.getContext());
        } else {
            AppRoute.jumpToLogin(getContext());
        }
    }

    @OnClick(R.id.tool_bar_toast_view)
    public void onToastClick() {
        mToastView.dismiss();
        int type = mToastView.getType();
        if (type == ToolbarToastView.TYPE_REPLY_ME) {
            AppRoute.jumpToMomentMessage(this.getContext());
        }
        if (type == ToolbarToastView.TYPE_POST_SUCCESS && mAdapter != null && mViewPager.getCurrentItem() >= 0) {
            MomentFragment momentFragment = (MomentFragment) mAdapter.getItem(mViewPager.getCurrentItem());
            momentFragment.scrollToTop();
        }
        if (type == ToolbarToastView.TYPE_AT_ME) {
            AppRoute.jumpToMomentAtMe(getContext());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 10256) {
            AppRoute.jumpToPostMoment(getActivity());
        }
    }

    public void showToast(int type, String msg) {
        mToastView.setType(type);
        mToastView.show(msg);
    }

    public void dismissToast() {
        mToastView.dismiss();
    }

    public static class SNSFragmentAdapter extends FragmentStatePagerAdapter {

        private final List<MomentFragment> mFragments = new ArrayList<>();

        public SNSFragmentAdapter(Context context, FragmentManager fm) {
            super(fm);
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_ALL));
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_FOLLOWING));
            mFragments.add(MomentFragment.newInstance(IMomentApi.MOMENT_TYPE_MY));
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return Rx.getCount(mFragments);
        }
    }

    @Subscribe
    public void onTabEvent(TabEvent event) {
        if (event.getPosition() == 1) {
            performTabEvent();
        }
    }

    private void performTabEvent() {
        int position = mViewPager.getCurrentItem();
        MomentFragment fragment = (MomentFragment) mAdapter.getItem(position);
        if (fragment.isAdded() && !fragment.isDetached() && !fragment.isHidden()) {
            fragment.scrollToTop();
        }
    }

    class DefaultOnTabSelectedListener implements DesignTabLayout.OnTabSelectedListener {

        @Override
        public void onTabSelected(DesignTabLayout.Tab tab) {
            int count = mTabLayout.getTabCount();
            for (int i = 0; i < count; i++) {
                DesignTabLayout.Tab tabAt = mTabLayout.getTabAt(i);
                if (tabAt == null) continue;
                tabAt.setTextStyle(tab == tabAt ? 1 : 0);
            }
        }

        @Override
        public void onTabUnselected(DesignTabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(DesignTabLayout.Tab tab) {
            onTabSelected(tab);
            performTabEvent();
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(final PostMomentEvent event) {

        // 闪存事件
        if (event.isDeleted()) return;

        if (event.getIsSuccess()) {
            showToast(ToolbarToastView.TYPE_POST_SUCCESS, "发布成功");
            cancelPostMomentNotification(event);
        } else {
            DefaultDialog dialog = new DefaultDialog(getContext());
            dialog.setEnSureText("立即查看");
            dialog.setTitle("发布闪存失败");
            dialog.setMessage(event.getMessage());
            dialog.setOnEnSureListener(new IAppDialogClickListener() {
                @Override
                public void onClick(IAppDialog dialog, int buttonType) {
                    dialog.dismiss();
                    // 跳转到闪存发布
                    AppRoute.jumpToPostMoment(getActivity(), event.getMomentMetaData());
                }
            });
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    cancelPostMomentNotification(event);
                }
            });
            dialog.show();
        }

    }

    private void cancelPostMomentNotification(PostMomentEvent event) {
        // 清除通知
        NotificationManager nm = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (nm != null && event.getNotificationId() > 0) {
            nm.cancel(event.getNotificationId());
        }
    }
}
