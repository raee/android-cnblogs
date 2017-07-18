package com.rae.cnblogs.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.adapter.CategoriesOverallAdapter;
import com.rae.cnblogs.dialog.CategoryDialog;
import com.rae.cnblogs.model.CategoriesOverallItem;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.ICategoryApi;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

/**
 * 分类管理
 * Created by ChenRui on 2017/7/16 0016 22:58.
 */
public class CategoriesFragment extends DialogFragment implements CategoriesOverallAdapter.CategoryDragListener {

    public static CategoriesFragment newInstance(List<CategoryBean> data) {
        Bundle args = new Bundle();
        args.putParcelableArrayList("data", (ArrayList<CategoryBean>) data);
        CategoriesFragment fragment = new CategoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private List<AbstractFlexibleItem> mCategoryItems = new ArrayList<>();
    private List<AbstractFlexibleItem> mUnusedItems = new ArrayList<>();

    private RecyclerView mCategoryRecyclerView;
    private CategoriesOverallAdapter mCategoryAdapter;


    private RecyclerView mUnusedRecyclerView;
    private CategoriesOverallAdapter mUnusedAdapter;
    private ICategoryApi mCategoryApi;

    @BindView(R.id.ll_category_unused)
    View mUnusedLayout;

    @BindView(R.id.tv_remove_category)
    TextView mEditView;

    protected int getLayoutId() {
        return R.layout.fm_categories;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCategoryApi = CnblogsApiFactory.getInstance(getContext()).getCategoriesApi();


    }

    @OnClick(R.id.tv_remove_category)
    public void onRemoveClick() {
        mCategoryAdapter.switchMode();
        mEditView.setText(mCategoryAdapter.isRemoveMode() ? R.string.finish : R.string.edit);
        mCategoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        List<CategoryBean> data = getArguments().getParcelableArrayList("data");
        if (data != null) {
            for (CategoryBean item : data) {
                CategoriesOverallItem overallItem = new CategoriesOverallItem(item);
                if (item.isHide()) {
                    mUnusedItems.add(overallItem);
                } else {
                    mCategoryItems.add(overallItem);
                }
            }
            mUnusedLayout.setVisibility(mUnusedItems.size() > 0 ? View.VISIBLE : View.INVISIBLE);
            initializeRecyclerView();
            initializeUnusedRecyclerView();
        }
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

        mCategoryAdapter.addListener(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {


                if (mCategoryAdapter.isRemoveMode()) {

                    AbstractFlexibleItem item = mCategoryAdapter.getItem(position);
                    if (item == null) {

                        return false;
                    }

                    // 添加到隐藏的分类中
                    mUnusedAdapter.addItem(0, item);

                    // 删除显示的分类
                    mCategoryAdapter.removeItem(position);

                    saveSort();
                    notifyDataSetChanged();
                } else {

                }

                return false;
            }
        });
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

        mUnusedAdapter.addListener(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int i) {
                // 添加到显示的分类中
                AbstractFlexibleItem item = mUnusedAdapter.getItem(i);
                if (item == null) return false;

                mCategoryAdapter.addItem(item);

                // 从隐藏的分类中删除
                mUnusedAdapter.removeItem(i);

                saveSort();
                notifyDataSetChanged();
                return true;
            }
        });
    }

    /**
     * 通知分类数据改变
     */
    private void notifyDataSetChanged() {
        mCategoryAdapter.updateDataSet(mCategoryItems);
        mUnusedAdapter.updateDataSet(mUnusedItems);
    }

    @Override
    public void onItemDrag() {
        // 当前的集合替换
        mCategoryItems = mCategoryAdapter.getCurrentItems();
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

        mCategoryItems = new ArrayList<>(mCategoryAdapter.getCurrentItems());
        mUnusedItems = new ArrayList<>(mUnusedAdapter.getCurrentItems());

        for (AbstractFlexibleItem item : mCategoryItems) {
            CategoryBean category = ((CategoriesOverallItem) item).getCategory();
            category.setHide(false);
            category.setOrderNo(index);
            result.add(category);

            Log.i("rae", category.getName());

            index++;

        }

        index = 0;
        for (AbstractFlexibleItem item : mUnusedItems) {
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

    @android.support.annotation.NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        CategoryDialog dialog = new CategoryDialog(getContext());
        dialog.setTopMargin(getArguments().getInt("margin"));
        return dialog;
    }
}
