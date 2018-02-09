package com.rae.cnblogs.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeViewCompat;
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

import static android.app.Activity.RESULT_OK;

/**
 * 博主
 * Created by ChenRui on 2017/2/9 0009 10:31.
 */
public class BloggerFragment extends BaseFragment {

    @BindView(R.id.rec_friends_list)
    RaeRecyclerView mRecyclerView;

    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;

    @BindView(R.id.ptr_content)
    AppLayout mAppLayout;

    private IFriendsApi mFriendApi;

    protected int mPage = 1;
    private FriendsAdapter mAdapter;
    private final List<UserInfoBean> mDataList = new ArrayList<>();
    private String mBlogApp;
    private boolean mIsFansType;

    public static BloggerFragment newInstance(String blogApp, boolean isFans) {
        Bundle args = new Bundle();
        args.putString("blogApp", blogApp);
        args.putBoolean("isFans", isFans);
        BloggerFragment fragment = new BloggerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_blogger;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFriendApi = CnblogsApiFactory.getInstance(getContext()).getFriendApi();
        if (getArguments() != null) {
            mBlogApp = getArguments().getString("blogApp", null);
            mIsFansType = getArguments().getBoolean("isFans", false);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new FriendsAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<UserInfoBean>() {
            @Override
            public void onItemClick(UserInfoBean item) {
                AppRoute.jumpToBlogger(getActivity(), item.getBlogApp());
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

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (canAutoStart()) {
            start();
        }
    }

    protected boolean canAutoStart() {
        return true;
    }

    protected void start() {
        mPage = 1;
        loadData();
    }

    // 获取数据
    protected void loadData() {
        Observable<List<UserInfoBean>> observable = getFollowAndFansList();
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

    protected Observable<List<UserInfoBean>> getFollowAndFansList() {
        if (mIsFansType) {
            return mFriendApi.getFansList(mBlogApp, mPage);
        }
        return mFriendApi.getFollowList(mBlogApp, mPage);
    }

    @Override
    public void onDestroy() {
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == AppRoute.REQ_CODE_BLOGGER) {
            start();
        }
    }


    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        RaeViewCompat.scrollToTop(mRecyclerView);
    }
}
