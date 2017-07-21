package com.rae.cnblogs.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RxObservable;
import com.rae.cnblogs.adapter.BaseItemAdapter;
import com.rae.cnblogs.adapter.BookmarksAdapter;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.CnblogsApiFactory;
import com.rae.cnblogs.sdk.Empty;
import com.rae.cnblogs.sdk.api.IBookmarksApi;
import com.rae.cnblogs.sdk.bean.BookmarksBean;
import com.rae.cnblogs.sdk.db.DbFactory;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.cnblogs.widget.RaeRecyclerView;
import com.rae.swift.Rx;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

/**
 * 我的收藏
 * Created by ChenRui on 2017/7/14 0014 14:58.
 */
public class FavoritesActivity extends SwipeBackBaseActivity {

    @BindView(R.id.placeholder)
    PlaceholderView mPlaceholder;
    @BindView(R.id.rec_friends_list)
    RaeRecyclerView mRecFriendsList;
    private IBookmarksApi mBookmarksApi;
    private int mPage = 1;
    private BookmarksAdapter mAdapter;
    private final List<BookmarksBean> mBookmarksDataList = new ArrayList<>();
    private String mTag = "FavoritesActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        showHomeAsUp();
        mBookmarksApi = CnblogsApiFactory.getInstance(this).getBookmarksApi();
        mAdapter = new BookmarksAdapter();
        mRecFriendsList.setAdapter(mAdapter);
        mRecFriendsList.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {

            }

            @Override
            public void onLoadMore() {
                mRecFriendsList.setNoMore(false);
                onLoadData();
            }
        });

        mAdapter.setOnItemClickListener(new BaseItemAdapter.onItemClickListener<BookmarksBean>() {
            @Override
            public void onItemClick(BookmarksBean item) {
                AppRoute.jumpToWeb(getContext(), item.getLinkUrl());
            }
        });

        mAdapter.setOnItemDeleteClickListener(new BaseItemAdapter.onItemClickListener<BookmarksBean>() {
            @Override
            public void onItemClick(final BookmarksBean item) {
                RxObservable.create(mBookmarksApi.delBookmarks(String.valueOf(item.getWzLinkId())), mTag)
                        .doOnSubscribe(new Consumer<Disposable>() {
                            @Override
                            public void accept(Disposable disposable) throws Exception {
                                AppUI.loading(getContext(), R.string.deleting);
                            }
                        })
                        .doFinally(new Action() {
                            @Override
                            public void run() throws Exception {
                                AppUI.dismiss();
                            }
                        })
                        .subscribe(new Consumer<Empty>() {
                            @Override
                            public void accept(Empty empty) throws Exception {
                                mAdapter.remove(item);
                                mAdapter.notifyDataSetChanged();

                                // 更新数据库
                                DbFactory.getInstance().getBlog().removeBookmarks(item.getLinkUrl());

                                setResult(RESULT_OK);

                            }
                        });
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        onLoadData();
    }

    /**
     * 加载数据
     */
    private void onLoadData() {
        RxObservable.create(mBookmarksApi.getBookmarks(mPage), mTag)
                .subscribe(new ApiDefaultObserver<List<BookmarksBean>>() {

                    @Override
                    protected void onError(String message) {
                        mPlaceholder.empty(message);
                    }

                    @Override
                    protected void accept(List<BookmarksBean> bookmarksBeans) {
                        mPlaceholder.dismiss();
                        if (Rx.isEmpty(bookmarksBeans)) {
                            if (mPage > 1) {
                                // 没有更多
                                mRecFriendsList.setNoMore(true);
                            } else {
                                mPlaceholder.empty();
                            }
                            return;
                        }

                        if (mPage <= 1) {
                            mBookmarksDataList.clear();
                        }
                        mBookmarksDataList.addAll(bookmarksBeans);
                        mAdapter.invalidate(mBookmarksDataList);
                        mAdapter.notifyDataSetChanged();
                        mPage++;
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxObservable.dispose(mTag);
        mBookmarksDataList.clear();
    }
}
