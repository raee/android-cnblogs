package com.rae.cnblogs.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.rae.cnblogs.AppUI;
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

import butterknife.BindView;
import butterknife.OnClick;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.SmoothScrollStaggeredLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * 分类管理
 * Created by ChenRui on 2017/7/16 0016 22:58.
 */
public class CategoriesFragment extends BaseFragment implements CategoriesOverallAdapter.CategoryDragListener {

    private Disposable mDisposable;

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

//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(getLayoutId(), container, false);
//        ButterKnife.bind(this, view);
//        return view;
//    }


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
        // 加载分类
        RxObservable.create(CnblogsApiFactory.getInstance(getContext()).getCategoriesApi().getCategories(), "Category").subscribe(new ApiDefaultObserver<List<CategoryBean>>() {
            @Override
            protected void onError(String message) {
                AppUI.failed(getContext(), "分类加载错误！");
                getActivity().finish();
            }

            @Override
            protected void accept(List<CategoryBean> data) {
                // 初始化分类
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
        });
    }

    /**
     * 初始化
     */
    private void initializeRecyclerView() {
        mCategoryAdapter = new CategoriesOverallAdapter(mCategoryItems);
        mCategoryAdapter.setOnlyEntryAnimation(true)
                .setAnimationInterpolator(new DecelerateInterpolator())
                .setAnimationInitialDelay(500L)
                .setAnimationDelay(70L);
        mCategoryRecyclerView = (RecyclerView) getView().findViewById(R.id.recycler_view);
        mCategoryRecyclerView.setItemViewCacheSize(0); //Setting ViewCache to 0 (default=2) will animate mCategoryItems better while scrolling down+up with LinearLayout
        mCategoryRecyclerView.setLayoutManager(new SmoothScrollStaggeredLayoutManager(getActivity(), 3));
        mCategoryRecyclerView.setAdapter(mCategoryAdapter);
        mCategoryRecyclerView.setHasFixedSize(true); //Size of RV will not change
        mCategoryAdapter.setLongPressDragEnabled(true) //Enable long press to drag items
                .setHandleDragEnabled(true) //Enable drag using handle view
                .setSwipeEnabled(true); //Enable swipe items

        mCategoryAdapter.setCategoryDragListener(this);
        mCategoryAdapter.setOnDataSetFinishListener(new CategoriesOverallAdapter.OnDataSetFinishListener() {
            @Override
            public void onDataSetFinish(final View lastView) {
                lastView.post(new Runnable() {
                    @Override
                    public void run() {
                        onViewLayoutFinish(lastView);
                    }
                });
            }

            private void onViewLayoutFinish(View lastView) {
                int[] location = new int[2];
                lastView.getLocationInWindow(location);
                int y = location[1];
                Log.i("rae", "高度：" + mCategoryRecyclerView.getLayoutManager().getHeight() + ";实际：" + mCategoryRecyclerView.getHeight());
            }
        });

        mCategoryAdapter.addListener(new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(int position) {

                // 删除模式
                if (mCategoryAdapter.isRemoveMode()) {

                    AbstractFlexibleItem item = mCategoryAdapter.getItem(position);
                    // 保留首页跟推荐
                    if (item == null || position <= 1) {
                        return false;
                    }

                    // 添加到隐藏的分类中
                    mUnusedAdapter.addItem(0, item);

                    // 删除显示的分类
                    mCategoryAdapter.removeItem(position);

                    saveSort(true);
                } else {
                    getActivity().getIntent().putExtra("position", position);
                    getActivity().setResult(Activity.RESULT_OK, getActivity().getIntent());
                    getActivity().finish();
                }

                return false;
            }
        });

        mCategoryAdapter.addListener(new FlexibleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int i) {
                CategoriesOverallItem item = (CategoriesOverallItem) mCategoryAdapter.getItem(i);
                if (item == null) return;
                AppUI.toastInCenter(getContext(), item.getCategory().getName());
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
        mUnusedRecyclerView.setLayoutManager(new SmoothScrollStaggeredLayoutManager(getActivity(), 3));
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

                saveSort(true);
                return true;
            }
        });

        mUnusedAdapter.addListener(new FlexibleAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int i) {
                CategoriesOverallItem item = (CategoriesOverallItem) mUnusedAdapter.getItem(i);
                if (item == null) return;
                AppUI.toastInCenter(getContext(), item.getCategory().getName());
            }
        });
    }


    @Override
    public void onItemDrag() {
        // 当前的集合替换
        mCategoryItems = mCategoryAdapter.getCurrentItems();
        saveSort(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveSort(false);
    }

    /**
     * 保存分类
     */
    private void saveSort(final boolean enableNotify) {
        if (mCategoryAdapter == null || mUnusedAdapter == null) return;

        List<CategoryBean> result = new ArrayList<>();
        mCategoryItems = new ArrayList<>(mCategoryAdapter.getCurrentItems());
        mUnusedItems = new ArrayList<>(mUnusedAdapter.getCurrentItems());

        // 不能没有分类
        if (mCategoryItems.size() <= 0) {
            return;
        }

        final String tag = "updateCategories";

        mDisposable = Observable.just(result)
                .subscribeOn(Schedulers.io())
                .map(new Function<List<CategoryBean>, List<CategoryBean>>() {
                    @Override
                    public List<CategoryBean> apply(List<CategoryBean> data) throws Exception {
                        int index = 0;
                        for (AbstractFlexibleItem item : mCategoryItems) {
                            CategoryBean category = ((CategoriesOverallItem) item).getCategory();
                            category.setHide(false);
                            category.setOrderNo(index);
                            data.add(category);
                            index++;
                        }

                        index = 0;
                        for (AbstractFlexibleItem item : mUnusedItems) {
                            CategoryBean category = ((CategoriesOverallItem) item).getCategory();
                            category.setHide(true);
                            category.setOrderNo(index);
                            data.add(category);
                            index++;
                        }

                        return data;
                    }
                })
                .flatMap(new Function<List<CategoryBean>, ObservableSource<Empty>>() {
                    @Override
                    public ObservableSource<Empty> apply(List<CategoryBean> categoryBeans) throws Exception {
                        return RxObservable.create(mCategoryApi.updateCategories(categoryBeans), tag);
                    }
                })
                .subscribe(new Consumer<Empty>() {
                    @Override
                    public void accept(@NonNull Empty empty) throws Exception {
                        // fix bug #812
                        if (enableNotify && mHandler!=null) {
                            mHandler.removeMessages(0);
                            mHandler.sendEmptyMessageDelayed(0, 200);
                        }
                    }
                });

        // fix bug #512
        if (getActivity() != null) {
            getActivity().setResult(Activity.RESULT_OK);
        }

    }

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            notifyDataSetChanged();
            return false;
        }
    });

    /**
     * 通知分类数据改变
     */
    private void notifyDataSetChanged() {
        mCategoryAdapter.updateDataSet(mCategoryItems);
        mUnusedAdapter.updateDataSet(mUnusedItems);

        // bug fix: 兼容华为文字白屏问题
        mCategoryRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mCategoryAdapter.notifyDataSetChanged();
                mUnusedAdapter.notifyDataSetChanged();
            }
        }, 300);
    }

    @Override
    public void onDestroy() {

        if (mCategoryAdapter != null) {
            mCategoryAdapter.setOnDataSetFinishListener(null);
        }
        // fix bug #385
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }

        mHandler.removeMessages(0);
        mHandler = null;
        super.onDestroy();
    }
}
