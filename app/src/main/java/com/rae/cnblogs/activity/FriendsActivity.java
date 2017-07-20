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
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.FriendsAdapter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IFriendsApi;
import com.rae.cnblogs.sdk.bean.UserInfoBean;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.Observable;

/**
 * 粉丝和关注
 * Created by ChenRui on 2017/2/23 00:41.
 */
public class FriendsActivity extends SwipeBackBaseActivity {

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
        Observable<List<UserInfoBean>> observable = mFriendApi.getFollowAndFansList(mUserId, mPage, !isFansType());
        RxObservable.create(observable, "FriendsActivity").subscribe(new ApiDefaultObserver<List<UserInfoBean>>() {
            @Override
            protected void onError(String message) {
                if (mPage > 1) {
                    mRecyclerView.setNoMore(true);
                } else {
                    mPlaceholderView.empty(message);
                }
            }

            @Override
            protected void accept(List<UserInfoBean> data) {
                onLoadFriends(data);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxObservable.dispose("FriendsActivity");
    }

    public void onLoadFriends(List<UserInfoBean> data) {
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
        } else if (Rx.isEmpty(mDataList) && mPage > 1) {
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
     */
    public boolean isFansType() {
        return mFromType == AppRoute.ACTIVITY_FRIENDS_TYPE_FANS;
    }

}
