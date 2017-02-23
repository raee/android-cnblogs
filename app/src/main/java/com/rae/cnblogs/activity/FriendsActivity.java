package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.FriendsAdapter;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.IFriendsApi;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        showHomeAsUp(mToolbar);
        String title = getIntent().getStringExtra("title");
        if (TextUtils.isEmpty(title)) {
            title = getString(R.string.title_fans);
        }

        setTitle(title);
        mFriendApi = CnblogsApiFactory.getInstance(this).getFriendApi();

        mAdapter = new FriendsAdapter();
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<UserInfoBean>() {
            @Override
            public void onItemClick(UserInfoBean item) {
                AppRoute.jumpToBlogger(getContext(), item.getBlogApp());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPage++;
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
        if (UserProvider.getInstance().getLoginUserInfo() == null) {
            mPlaceholderView.empty(getString(R.string.not_login));
            return;
        }

        // 粉丝
        if (TextUtils.equals(mTitleView.getText(), getString(R.string.title_fans))) {
            mFriendApi.getFansList(UserProvider.getInstance().getLoginUserInfo().getUserId(), mPage, this);
        } else {
            mFriendApi.getFollowList(UserProvider.getInstance().getLoginUserInfo().getUserId(), mPage, this);
        }
    }

    @Override
    public void onApiFailed(ApiException ex, String msg) {
        mPlaceholderView.empty(msg);
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
        } else {
            mRecyclerView.setNoMore(true);
        }
        mAdapter.invalidate(mDataList);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();
        mPage++;
    }
}
