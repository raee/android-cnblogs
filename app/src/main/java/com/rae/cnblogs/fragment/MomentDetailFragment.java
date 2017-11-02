package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.MomentDetailAdapter;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IMomentDetailContract;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 闪存详情
 * Created by ChenRui on 2017/11/2 0002 15:35.
 */
public class MomentDetailFragment extends BaseFragment implements IMomentDetailContract.View {

    @BindView(R.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;
    @BindView(R.id.ptr_content)
    AppLayout mAppLayout;
    private MomentBean mData;
    private MomentDetailAdapter mAdapter;
    private IMomentDetailContract.Presenter mPresenter;


    public static MomentDetailFragment newInstance(MomentBean data) {
        Bundle args = new Bundle();
        args.putParcelable("data", data);
        MomentDetailFragment fragment = new MomentDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment_detail;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = CnblogsPresenterFactory.getMomentDetailPresenter(getContext(), this);

        if (getArguments() != null) {
            mData = getArguments().getParcelable("data");
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mData == null) {
            mPlaceholderView.empty("闪存数据为空");
            mAppLayout.setEnabled(false);
            return;
        }

        mPlaceholderView.dismiss();

        mAdapter = new MomentDetailAdapter(mData);
        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });

        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
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
        start();
    }

    public void start() {
        if (mData != null) {
            mPresenter.start();
        }
    }

    @Override
    public MomentBean getMomentInfo() {
        return mData;
    }

    @Override
    public void onEmptyComment(String message) {
        mRecyclerView.setNoMore(true);
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.empty(message);
    }

    @Override
    public void onLoadComments(List<MomentCommentBean> data) {
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
    }
}
