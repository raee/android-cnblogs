package com.rae.cnblogs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.rae.cnblogs.R;
import com.rae.cnblogs.event.LoginEventMessage;
import com.rae.cnblogs.widget.webclient.CnblogLoginWebViewClient;
import com.rae.cnblogs.widget.webclient.bridge.WebLoginListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * WEB 登录模块
 * Created by ChenRui on 2017/1/19 22:06.
 */
public class WebLoginFragment extends WebViewFragment {

    private CnblogLoginWebViewClient mWebViewClient;
    private WebLoginListener mLoginListener; // 登录回调
    private String mUrl = "https://passport.cnblogs.com/user/signin";

    public static WebLoginFragment newInstance() {
        return new WebLoginFragment();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }


    @Subscribe
    public void onLoginEvent(LoginEventMessage message) {
        load(message.userName, message.password);
        Toast.makeText(getContext(), "message = " + message.userName, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fm_web_login;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.setVisibility(View.GONE); // 后台运行
    }


    @Override
    public WebViewClient getWebViewClient() {
        if (!(getActivity() instanceof WebLoginListener)) {
            throw new ClassCastException("activity must implement LoginListener");
        }

        mLoginListener = (WebLoginListener) getActivity();
        mWebViewClient = new CnblogLoginWebViewClient(mWebView, mProgressBar, mLoginListener);
        return mWebViewClient;
    }

    private void load(String userName, String password) {
        mWebViewClient.setLoginParams(userName, password);
        loadUrl(mUrl);
    }
}
