package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.sdk.config.CnblogSdkConfig;

import butterknife.ButterKnife;

/**
 * Created by ChenRui on 2016/12/1 23:51.
 */
public abstract class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        ButterKnife.bind(this, view);
        onCreateView(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLoadData();
    }

    protected void onCreateView(View view) {
    }

    protected void onLoadData() {

    }

    protected abstract int getLayoutId();

    protected CnblogSdkConfig config() {
        return CnblogSdkConfig.getsInstance(getContext().getApplicationContext());
    }

    protected int parseInt(String text) {
        if (TextUtils.isEmpty(text)) return 0;
        try {
            return Integer.valueOf(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

}
