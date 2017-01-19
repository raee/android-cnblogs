package com.rae.cnblogs.widget.webclient;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.rae.cnblogs.widget.webclient.bridge.CnblogsLoginJsBridge;
import com.rae.cnblogs.widget.webclient.bridge.LoginListener;

/**
 * WEB登录 处理
 * Created by ChenRui on 2017/1/19 22:04.
 */
public class CnblogLoginWebViewClient extends RaeWebViewClient {

    private LoginListener mLoginListener;

    private CnblogsLoginJsBridge mLoginJsBridge;


    @SuppressLint("JavascriptInterface")
    public CnblogLoginWebViewClient(WebView view, ProgressBar progressBar, LoginListener listener) {
        super(progressBar);
        mLoginListener = listener;
        mLoginJsBridge = new CnblogsLoginJsBridge(listener);
        view.addJavascriptInterface(mLoginJsBridge, "loginBridge");
    }

    public void setLoginParams(String userName, String password) {
        mLoginJsBridge.setUserName(userName);
        mLoginJsBridge.setPassword(password);
    }

    public void setCode(String code) {
        mLoginJsBridge.setCode(code);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.i("Rae", "onPageFinished = " + url);
        injectJavascriptFromAssets(view, "js/auto-login.js");
        view.buildDrawingCache();
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);

        // 检查COOKIE
        String cookie = CookieManager.getInstance().getCookie(url);
        Log.i("Rae", "onPageStarted = " + url);
        Log.w("Rae", "cookie = " + cookie);
        if (!TextUtils.isEmpty(cookie) && cookie.contains(".CNBlogsCookie") && mLoginListener != null) {
            mLoginListener.onLoginSuccess();
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        Log.w("Rae", "onLoadResource = " + url);
        if (url.contains("https://passport.cnblogs.com/BotDetectCaptcha.ashx?get=image") && mLoginListener != null) {
            // 加载验证码
        }
    }

}
