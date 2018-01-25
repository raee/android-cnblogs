package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.SystemMessageAdapter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.api.IRaeServerApi;
import com.rae.cnblogs.sdk.bean.SystemMessageBean;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;

import java.util.List;

import butterknife.BindView;

/**
 * 系统消息
 * Created by ChenRui on 2017/9/5 0005 15:39.
 */
public class SystemMessageActivity extends SwipeBackBaseActivity {

    @BindView(R.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;

    IRaeServerApi mRaeServerApi;

    SystemMessageAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_message);
        showHomeAsUp();
        mRaeServerApi = CnblogsApiFactory.getInstance(this).getRaeServerApi();
        mAdapter = new SystemMessageAdapter();

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<SystemMessageBean>() {
            @Override
            public void onItemClick(SystemMessageBean item) {
                AppRoute.jumpToWeb(getContext(), item.getUrl());
            }
        });

        // 获取消息列表
        RxObservable.create(mRaeServerApi.getMessages())
                .subscribe(new ApiDefaultObserver<List<SystemMessageBean>>() {
                    @Override
                    protected void onError(String message) {
                        mPlaceholderView.empty();
                    }

                    @Override
                    protected void accept(List<SystemMessageBean> data) {
                        if (Rx.isEmpty(data)) {
                            mPlaceholderView.empty();
                            return;
                        }
                        mPlaceholderView.dismiss();
                        mAdapter.invalidate(data);
                        mAdapter.notifyDataSetChanged();
                    }
                });

        // 更新消息数量
        RxObservable.create(mRaeServerApi.getMessageCount())
                .subscribe(new ApiDefaultObserver<Integer>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(Integer count) {
                        config().setMessageCount(count);
                    }
                });

    }
}
