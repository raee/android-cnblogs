package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.animation.DecelerateInterpolator;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.adapter.CategoriesOverallAdapter;
import com.rae.cnblogs.model.CategoriesOverallItem;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 分类管理
 * Created by ChenRui on 2017/7/16 0016 22:58.
 */
public class CategoriesFragment extends BaseFragment implements CategoriesOverallAdapter.CategoryDragListener {
    private final List<AbstractFlexibleItem> mCategoryItems = new ArrayList<>();
    private final List<AbstractFlexibleItem> mUnusedItems = new ArrayList<>();

    private RecyclerView mCategoryRecyclerView;
    private CategoriesOverallAdapter mCategoryAdapter;


    private RecyclerView mUnusedRecyclerView;
    private CategoriesOverallAdapter mUnusedAdapter;
    private ICategoryApi mCategoryApi;


    @Override
    protected int getLayoutId() {
        return R.layout.fm_categories;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryApi = CnblogsApiFactory.getInstance(getContext()).getCategoriesApi();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 加载分类
        RxObservable.create(mCategoryApi.getCategories(), "categories").subscribe(new ApiDefaultObserver<List<CategoryBean>>() {
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

                int i = 0;
                for (CategoryBean item : data) {
                    CategoriesOverallItem overallItem = new CategoriesOverallItem(item);
                    if (item.isHide()) {
                        mUnusedItems.add(overallItem);
                    } else {
                        mCategoryItems.add(overallItem);
                    }
                    i++;
                }

                initializeRecyclerView();
                initializeUnusedRecyclerView();
            }
        });

    }

    private void initializeRecyclerView() {

        mCategoryAdapter = new CategoriesOverallAdapter(mCategoryItems);
        mCategoryAdapter.setOnlyEntryAnimation(true)
                .setAnimationInterpolator(new DecelerateInterpolator())
                .setAnimationInitialDelay(500L)
                .setAnimationDelay(70L);


        mCategoryRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mCategoryRecyclerView.setItemViewCacheSize(0); //Setting ViewCache to 0 (default=2) will animate mCategoryItems better while scrolling down+up with LinearLayout
        mCategoryRecyclerView.setLayoutManager(new SmoothScrollStaggeredLayoutManager(getActivity(), 4));
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryRecyclerView.setHasFixedSize(true); //Size of RV will not change
        mCategoryAdapter.setLongPressDragEnabled(true) //Enable long press to drag items
                .setHandleDragEnabled(true) //Enable drag using handle view
                .setSwipeEnabled(true); //Enable swipe items

        mCategoryAdapter.setCategoryDragListener(this);
    }


    private void initializeUnusedRecyclerView() {

        mUnusedAdapter = new CategoriesOverallAdapter(mUnusedItems);
        mUnusedAdapter.setOnlyEntryAnimation(true)
                .setAnimationInterpolator(new DecelerateInterpolator())
                .setAnimationInitialDelay(500L)
                .setAnimationDelay(70L);

        mUnusedRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view_unused);
        mUnusedRecyclerView.setItemViewCacheSize(0); //Setting ViewCache to 0 (default=2) will animate mCategoryItems better while scrolling down+up with LinearLayout
        mUnusedRecyclerView.setLayoutManager(new SmoothScrollStaggeredLayoutManager(getActivity(), 4));
        mUnusedRecyclerView.setAdapter(mUnusedAdapter);
        mUnusedRecyclerView.setHasFixedSize(true); //Size of RV will not change
        mUnusedAdapter.setCategoryDragListener(this);
    }

    @Override
    public void onItemDrag() {
        saveSort();
    }

    @Override
    public void onStop() {
        super.onStop();
        saveSort();
    }

    /**
     * 保存分类
     */
    private void saveSort() {
        int index = 0;
        List<CategoryBean> result = new ArrayList<>();
        for (AbstractFlexibleItem item : mCategoryAdapter.getCurrentItems()) {
            CategoryBean category = ((CategoriesOverallItem) item).getCategory();
            category.setHide(false);
            category.setOrderNo(index);
            result.add(category);

            Log.i("rae", category.getName());

            index++;

        }

        index = 0;
        for (AbstractFlexibleItem item : mUnusedAdapter.getCurrentItems()) {
            CategoryBean category = ((CategoriesOverallItem) item).getCategory();
            category.setHide(true);
            category.setOrderNo(index);
            result.add(category);
            Log.i("rae", ((CategoriesOverallItem) item).getCategory().getName());
            index++;
        }

        RxObservable.create(mCategoryApi.updateCategories(result), "updateCategories").subscribe(new Consumer<Empty>() {
            @Override
            public void accept(@NonNull Empty empty) throws Exception {

            }
        });
    }
}
