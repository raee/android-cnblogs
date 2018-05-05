package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.sdk.config.CnblogAppConfig;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Fragment基础类，所有的Fragment都需要继承这个
 */
public abstract class BasicFragment extends Fragment {

    @Nullable
    private Unbinder mUnBinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mUnBinder = ButterKnife.bind(this, view);
        onCreateView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onLoadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnBinder != null) {
            mUnBinder.unbind();
            mUnBinder = null;
        }
    }

    protected void onCreateView(View view) {
    }

    /**
     * 实现类重写加载数据逻辑
     */
    protected void onLoadData() {
    }

    protected abstract int getLayoutId();

    /**
     * 获取配置文件
     */
    protected CnblogAppConfig config() {
        // fix bug #478
        return CnblogAppConfig.getsInstance(getContext());
    }
}
