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
import butterknife.Unbinder;


public abstract class BaseFragment extends Fragment {

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), container, false);
        mUnbinder = ButterKnife.bind(this, view);
        onCreateView(view);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onLoadData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
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
