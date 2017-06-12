package com.rae.cnblogs;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * RX API Observable
 * Created by ChenRui on 2017/5/5 0005 16:52.
 */
public final class RxObservable {

    private final static Map<String, List<Disposable>> sObservableDisposableList = new HashMap<>();


    public static <T> Observable<T> create(Observable<T> observable) {
        final String className = getInvokeMethodName();
//        Log.i("rae", "当前调用：" + className);

        List<Disposable> disposables = sObservableDisposableList.get(className);
        if (disposables == null) {
            disposables = new ArrayList<>();
            sObservableDisposableList.put(className, disposables);
        }
        return observable.doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(@NonNull Disposable disposable) throws Exception {
                sObservableDisposableList.get(className).add(disposable);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    @Nullable
    private static String getInvokeMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        boolean hasFind = false;
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
//            Log.d("rae", element.toString());
            if (className.contains("RxObservable")) {
                hasFind = true;
                continue;
            }
            if (hasFind) {
                return className;
            }
        }
        return "default";
    }

    /**
     * 释放当前所有HTTP请求
     */
    public static void dispose() {
        for (List<Disposable> disposables : sObservableDisposableList.values()) {
            for (Disposable disposable : disposables) {
                if (disposable != null && !disposable.isDisposed())
                    disposable.dispose();
            }
        }
        sObservableDisposableList.clear();
    }

    /**
     * 释放指定TAG的请求
     *
     * @param tag 标签
     */
    public static void dispose(String tag) {
//        Log.w("rae", "取消请求：" + tag);
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
    }


}
