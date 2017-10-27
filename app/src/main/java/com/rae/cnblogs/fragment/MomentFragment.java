package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeViewCompat;
import com.rae.cnblogs.adapter.MomentAdapter;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IMomentContract;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 闪存列表
 * Created by ChenRui on 2017/10/27 0027 10:41.
 */
public class MomentFragment extends BaseFragment implements IMomentContract.View {


    /**
     * 实例化
     *
     * @param type 参考：{@link com.rae.cnblogs.sdk.api.IMomentApi#MOMENT_TYPE_ALL}
     * @return
     */
    public static MomentFragment newInstance(String type) {
        Bundle args = new Bundle();
        args.putString("type", type);
        MomentFragment fragment = new MomentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String mType;

    @BindView(R.id.recycler_view)
    RaeRecyclerView mRecyclerView;
    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholderView;
    @BindView(R.id.ptr_content)
    AppLayout mAppLayout;

    MomentAdapter mAdapter;
    IMomentContract.Presenter mPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_moment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = CnblogsPresenterFactory.getMomentPresenter(getContext(), this);
        if (getArguments() != null) {
            mType = getArguments().getString("type", IMomentApi.MOMENT_TYPE_ALL);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mPresenter.destroy();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MomentAdapter();
        mRecyclerView.setAdapter(mAdapter);

        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPresenter.start();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return mRecyclerView.isOnTop();
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mPresenter.loadMore();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter.start();
    }


    @Override
    public void onNoMoreData() {
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mAppLayout.refreshComplete();
        mPlaceholderView.empty(msg);
    }

    @Override
    public void onLoadData(List<MomentBean> data) {
        mRecyclerView.setNoMore(false);
        mPlaceholderView.dismiss();
        mAppLayout.refreshComplete();
        mRecyclerView.loadMoreComplete();
        mAdapter.invalidate(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoginExpired() {
        mPlaceholderView.empty(getString(R.string.login_expired));
    }

    @Override
    public String getType() {
        return mType;
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        RaeViewCompat.scrollToTop(mRecyclerView);
        if (mRecyclerView.isOnTop()) {
            mAppLayout.post(new Runnable() {
                @Override
                public void run() {
                    mAppLayout.autoRefresh();
                }
            });
        }
    }
}
