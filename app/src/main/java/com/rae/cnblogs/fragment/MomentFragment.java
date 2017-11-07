package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppMobclickAgent;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeViewCompat;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.MomentAdapter;
import com.rae.cnblogs.presenter.CnblogsPresenterFactory;
import com.rae.cnblogs.presenter.IMomentContract;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.api.IMomentApi;
import com.rae.cnblogs.sdk.bean.MomentBean;
import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.cnblogs.widget.ToolbarToastView;

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

    // 当前正在删除的索引
    private int mCurrentDeletePosition;

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
        mAdapter.setOnItemClickListener(null);
        mAdapter.setOnBloggerClickListener(null);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MomentAdapter();
        mAdapter.setOnBloggerClickListener(new MomentAdapter.OnBloggerClickListener() {
            @Override
            public void onBloggerClick(String blogApp) {
                AppRoute.jumpToBlogger(getContext(), blogApp);
            }
        });
        mAdapter.setOnDeleteClickListener(new MomentAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(String ingId, int position) {
                // 删除
                mCurrentDeletePosition = position;
                AppUI.loading(getContext(), "正在删除");
                mPresenter.delete(ingId);
            }
        });
        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<MomentBean>() {
            @Override
            public void onItemClick(MomentBean item) {
                if (item != null)
                    AppRoute.jumpToMomentDetail(getContext(), item);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPlaceholderView.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start();
            }
        });


        // 判断是否登录
        boolean isLogin = UserProvider.getInstance().isLogin();
        if (!isLogin && TextUtils.equals(IMomentApi.MOMENT_TYPE_ALL, mType)) {
            // 没有登录
            mPlaceholderView.showLogin();
        } else {
            mPlaceholderView.dismiss();
        }

        mAppLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                start();
                // 统计闪存
                AppMobclickAgent.onClickEvent(frame.getContext(), "Moment_" + mType);

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

    /**
     * 开始加载数据
     */
    protected void start() {
        mPresenter.start();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        start();
    }

    @Override
    public void onNoMoreData() {
        mRecyclerView.setNoMore(true);
    }

    @Override
    public void onEmptyData(String msg) {
        mAppLayout.refreshComplete();
        mPlaceholderView.retry(msg);
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
        mAppLayout.refreshComplete();
        mPlaceholderView.showLogin();
    }

    @Override
    public String getType() {
        return mType;
    }

    @Override
    public void onDeleteMomentFailed(String msg) {
        AppUI.dismiss();
        AppUI.failed(getContext(), msg);
    }

    @Override
    public void onDeleteMomentSuccess() {
        AppUI.dismiss();
        MomentBean item = mAdapter.getDataItem(mCurrentDeletePosition);
        mAdapter.remove(item);
        mAdapter.notifyDataSetChanged();
        AppUI.success(getContext(), R.string.tips_del_moment_success);
    }

    @Override
    public void onReplyContChanged(int number) {
        Log.i("rae", "有回复我的；" + number);
        showToast(ToolbarToastView.TYPE_REPLY_ME, number + "条回复我的消息");
    }

    private void showToast(int type, String msg) {
        Fragment fragment = getParentFragment();
        if (fragment != null && fragment.isAdded() && fragment.isVisible()) {
            SNSFragment snsFragment = (SNSFragment) fragment;
            snsFragment.showToast(type, msg);
        }
    }

    /**
     * 滚动到顶部
     */
    public void scrollToTop() {
        RaeViewCompat.scrollToTop(mRecyclerView);
        if (mRecyclerView != null && mRecyclerView.isOnTop()) {
            mAppLayout.post(new Runnable() {
                @Override
                public void run() {
                    mAppLayout.autoRefresh();
                }
            });
        }
    }

}
