package com.rae.cnblogs;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * RX 接口回调处理
 * Created by ChenRui on 2017/5/5 0005 16:52.
 */
public final class RxObservable {

    private final static Map<String, List<Disposable>> sObservableDisposableList = new WeakHashMap<>();


    public static <T> Observable<T> create(Observable<T> observable, final String tag) {
        return observable
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(@NonNull Disposable disposable) throws Exception {
                        List<Disposable> disposables = sObservableDisposableList.get(tag);
                        if (disposables == null) {
                            disposables = new ArrayList<>();
                            sObservableDisposableList.put(tag, disposables);
                        }
                        sObservableDisposableList.get(tag).add(disposable);
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 释放当前所有HTTP请求
     */
    public static void dispose() {
        try {
            for (List<Disposable> disposables : sObservableDisposableList.values()) {
                for (Disposable disposable : disposables) {
                    if (disposable != null && !disposable.isDisposed())
                        disposable.dispose();
                }
                disposables.clear();
            }
        } catch (Exception e) {
            Log.e("rae", "释放HTTP请求失败！", e);
        } finally {
            sObservableDisposableList.clear();
        }
    }

    /**
     * 释放指定TAG的请求
     *
     * @param tag 标签
     */
    public static void dispose(String tag) {
        try {
            if (TextUtils.isEmpty(tag) || !sObservableDisposableList.containsKey(tag)) {
                return;
            }
            List<Disposable> disposables = sObservableDisposableList.get(tag);
            for (Disposable disposable : disposables) {
                if (disposable != null && !disposable.isDisposed())
                    disposable.dispose();
            }
            disposables.clear();
            sObservableDisposableList.remove(tag);
        } catch (Exception e) {
            Log.e("rae", "释放HTTP请求失败！", e);
        } finally {
            sObservableDisposableList.remove(tag);
        }
    }

}
