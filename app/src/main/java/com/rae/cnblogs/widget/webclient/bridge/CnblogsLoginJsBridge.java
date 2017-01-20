package com.rae.cnblogs.widget.webclient.bridge;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

/**
 * Created by ChenRui on 2017/1/19 22:44.
 */
public class CnblogsLoginJsBridge {

    private final WebLoginListener mListener;

    private String mUserName;
    private String mPassword;
    private String mCode;
    private Handler mHandler;


    public CnblogsLoginJsBridge(WebLoginListener listener) {
        mListener = listener;
        mHandler = new Handler(Looper.getMainLooper());
    }

    @JavascriptInterface
    public void onLoginError(final String msg) {
        if (mListener == null || TextUtils.isEmpty(msg) || msg.trim().isEmpty() || msg.contains("提交中")) {
            return;
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onWebLoginError(msg);
            }
        });
    }

    @JavascriptInterface
    public void onLoginCodeError(final String msg) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mListener.onWebLoginCodeError(msg);
            }
        });
    }

    @JavascriptInterface
    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    @JavascriptInterface
    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    @JavascriptInterface
    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }
}
