package com.rae.cnblogs.presenter.impl;

import android.content.Context;

import com.rae.cnblogs.presenter.ISearchContract;
import com.rae.cnblogs.sdk.ApiDefaultObserver;
import com.rae.cnblogs.sdk.api.ISearchApi;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * 搜索
 * Created by ChenRui on 2017/8/29 0029 9:39.
 */
public class SearchPresenterImpl extends BasePresenter<ISearchContract.View> implements ISearchContract.Presenter {

    ISearchApi mSearchApi;
    private Disposable mSuggestionSubscriber;

    public SearchPresenterImpl(Context context, ISearchContract.View view) {
        super(context, view);
        mSearchApi = getApiProvider().getSearchApi();
    }

    @Override
    public void destroy() {
        super.destroy();
        cancelSuggest();
    }

    @Override
    public void suggest() {
        cancelSuggest();
        createObservable(mSearchApi.getSuggestion(mView.getSearchText()))
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        mSuggestionSubscriber = disposable;
                    }
                })
                // 延迟，避免响应过快 fix bug #717
                .delay(300, TimeUnit.MILLISECONDS)
                // 切换回到主线程
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiDefaultObserver<List<String>>() {
                    @Override
                    protected void onError(String message) {

                    }

                    @Override
                    protected void accept(List<String> data) {

                        mView.onSuggestionSuccess(data);
                    }
                });
    }

    /**
     * 取消即时搜索
     */
    private void cancelSuggest() {
        if (mSuggestionSubscriber != null && !mSuggestionSubscriber.isDisposed()) {
            mSuggestionSubscriber.dispose();
        }
    }
}
