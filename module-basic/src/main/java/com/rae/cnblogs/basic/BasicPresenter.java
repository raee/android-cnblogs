package com.rae.cnblogs.basic;

import android.arch.lifecycle.Lifecycle;

import com.trello.lifecycle2.android.lifecycle.AndroidLifecycle;
import com.trello.rxlifecycle2.LifecycleProvider;

/**
 * MVP Presenter
 * Created by ChenRui on 2018/4/18.
 */
public abstract class BasicPresenter<V extends IPresenterView> implements IPresenter {

    // 生命周期绑定
    private final LifecycleProvider<Lifecycle.Event> mLifecycleProvider;
    private final V mView;

    public BasicPresenter(V view) {
        this.mView = checkNotNull(view);
        mLifecycleProvider = AndroidLifecycle.createLifecycleProvider(view);
    }

    public V getView() {
        return mView;
    }

    @Override
    public void start() {
        onStart();
    }

    @Override
    public void destroy() {
        onDestroy();
    }

    private void onDestroy() {

    }

    /**
     * 加载数据
     */
    abstract void onStart();


    /**
     * 检查是否为空
     */
    private <T> T checkNotNull(T object) {
        if (object == null) {
            throw new NullPointerException("object is null ");
        }
        return object;
    }
}
