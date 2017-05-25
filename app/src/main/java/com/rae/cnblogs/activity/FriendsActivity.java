package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.FriendsAdapter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.core.Rae;
import com.rae.core.sdk.ApiUiArrayListener;
import com.rae.core.sdk.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 粉丝和关注
 * Created by ChenRui on 2017/2/23 00:41.
 */
public class FriendsActivity extends SwipeBackBaseActivity implements ApiUiArrayListener<UserInfoBean> {

    @BindView(R.id.title_tool_bar)
    Toolbar mToolbar;

    @BindView(R.id.tv_title)
    TextView mTitleView;

    @BindView(R.id.rec_friends_list)
    RaeRecyclerView mRecyclerView;

    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;

    private IFriendsApi mFriendApi;

    private int mPage = 1;
    private FriendsAdapter mAdapter;
    private final List<UserInfoBean> mDataList = new ArrayList<>();
    private String mUserId;
    private int mFromType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        showHomeAsUp(mToolbar);
        mUserId = getIntent().getStringExtra("userId");
        String bloggerName = getIntent().getStringExtra("bloggerName");
        mFromType = getIntent().getIntExtra("fromType", AppRoute.ACTIVITY_FRIENDS_TYPE_FANS);
        if (TextUtils.isEmpty(mUserId)) {
            AppUI.toast(this, "博主信息为空！");
            finish();
            return;
        }

        if (TextUtils.isEmpty(bloggerName)) {
            bloggerName = "我";
        }
        if (isFansType()) {
            setTitle(getString(R.string.title_fans, bloggerName));
        } else {
            setTitle(getString(R.string.title_follow, bloggerName));
        }

        mFriendApi = CnblogsApiFactory.getInstance(this).getFriendApi();
        mAdapter = new FriendsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<UserInfoBean>() {
            @Override
            public void onItemClick(UserInfoBean item) {
                AppRoute.jumpToBlogger(getContext(), item.getBlogApp());
            }
        });
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                start();
            }
        });

        start();

    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    // 获取数据
    private void start() {
        // 粉丝
        if (isFansType()) {
            mFriendApi.getFansList(mUserId, mPage, this);
        } else {
            mFriendApi.getFollowList(mUserId, mPage, this);
        }
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        if (mPage > 1) {
            mRecyclerView.setNoMore(true);
        } else {
            mPlaceholderView.empty(msg);
        }
    }

    @Override
    public void onApiSuccess(List<UserInfoBean> data) {
        mPlaceholderView.dismiss();

        if (mPage <= 1) {
            mDataList.clear();
            mDataList.addAll(data);
        } else {
            mDataList.removeAll(data);
            mDataList.addAll(data);
        }

        if (mDataList.size() <= 0 && mPage <= 1) {
            mPlaceholderView.empty(getString(R.string.empty_message));
            return;
        } else if (Rae.isEmpty(mDataList) && mPage > 1) {
            mRecyclerView.setNoMore(true);
            return;
        }

        mAdapter.invalidate(mDataList);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();
        mPage++;
    }

    /**
     * 是否为粉丝类型
     *
     * @return
     */
    public boolean isFansType() {
        return mFromType == AppRoute.ACTIVITY_FRIENDS_TYPE_FANS;
    }

}
