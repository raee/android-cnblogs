package com.rae.cnblogs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import io.reactivex.Observable;
import io.reactivex.functions.Action;

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


    @BindView(R.id.ptr_content)
    AppLayout mAppLayout;

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
                AppRoute.jumpToBlogger(FriendsActivity.this, item.getBlogApp());
            }
        });
        mRecyclerView.setLoadingMoreEnabled(true);
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                loadData();
            }
        });


        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout ptrFrameLayout) {
                start();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mRecyclerView.isOnTop();
            }
        });

        start();
    }

    private void start() {
        mPage = 1;
        loadData();
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitleView.setText(title);
    }

    // 获取数据
    private void loadData() {
        Observable<List<UserInfoBean>> observable = mFriendApi.getFollowAndFansList(mUserId, mPage, !isFansType());
        RxObservable.create(observable, "FriendsActivity")
                .doFinally(new Action() {
                    @Override
                    public void run() throws Exception {
                        mAppLayout.refreshComplete();
                    }
                })
                .subscribe(new ApiDefaultObserver<List<UserInfoBean>>() {
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

        if (Rx.isEmpty(data)) {
            if (mPage <= 1) {
                mPlaceholderView.empty();
            } else {
                mRecyclerView.loadMoreComplete();
                mRecyclerView.setNoMore(true);
            }
            return;
        }
        if (mPage <= 1) {
            mDataList.clear();
        }
        mDataList.addAll(data);

        mAdapter.invalidate(mDataList);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.loadMoreComplete();
        mPage++;

        if (mPage <= 1) {
            mRecyclerView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mRecyclerView.scrollToPosition(0);
                }
            }, 1000);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppRoute.REQ_CODE_BLOGGER) {
            start();
        }
    }

    /**
     * 是否为粉丝类型
     */
    public boolean isFansType() {
        return mFromType == AppRoute.ACTIVITY_FRIENDS_TYPE_FANS;
    }

}
