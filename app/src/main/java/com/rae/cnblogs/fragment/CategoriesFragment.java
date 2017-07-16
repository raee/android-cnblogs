package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.animation.DecelerateInterpolator;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.adapter.CategoriesOverallAdapter;
import com.rae.cnblogs.model.CategoriesOverallItem;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * 分类管理
 * Created by ChenRui on 2017/7/16 0016 22:58.
 */
public class CategoriesFragment extends BaseFragment {
    private final List<AbstractFlexibleItem> mItems = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private CategoriesOverallAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fm_categories;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 加载分类
        RxObservable.create(CnblogsApiFactory.getInstance(getContext()).getCategoriesApi().getCategories(), "categories").subscribe(new ApiDefaultObserver<List<CategoryBean>>() {
            @Override
            protected void onError(String message) {
                // 发生错误至少加载首页这个分类
                List<CategoryBean> data = new ArrayList<>();

                CategoryBean home = new CategoryBean();
                home.setCategoryId("808");
                home.setParentId("0");
                home.setName("首页");
                home.setType("SiteHome");

                CategoryBean recommend = new CategoryBean();
                recommend.setCategoryId("-2");
                recommend.setParentId("0");
                recommend.setName("推荐");
                recommend.setType("Picked");

                data.add(home);
                data.add(recommend);

                accept(data);
            }

            @Override
            protected void accept(List<CategoryBean> data) {
                for (CategoryBean item : data) {
                    mItems.add(new CategoriesOverallItem(item));
                }
                initializeRecyclerView();
            }
        });

    }

    private void initializeRecyclerView() {

        mAdapter = new CategoriesOverallAdapter(mItems);
        mAdapter.setOnlyEntryAnimation(true)
                .setAnimationInterpolator(new DecelerateInterpolator())
                .setAnimationInitialDelay(500L)
                .setAnimationDelay(70L);

        mRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mRecyclerView.setItemViewCacheSize(0); //Setting ViewCache to 0 (default=2) will animate mItems better while scrolling down+up with LinearLayout
        mRecyclerView.setLayoutManager(new SmoothScrollStaggeredLayoutManager(getActivity(), 4));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true); //Size of RV will not change
        mAdapter.setLongPressDragEnabled(true) //Enable long press to drag items
                .setHandleDragEnabled(true) //Enable drag using handle view
                .setSwipeEnabled(true); //Enable swipe items
    }
}
