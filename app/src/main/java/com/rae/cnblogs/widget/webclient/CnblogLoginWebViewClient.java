package com.rae.cnblogs.widget.webclient;

import android.annotation.SuppressLint;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.rae.cnblogs.widget.webclient.bridge.CnblogsLoginJsBridge;
import com.rae.cnblogs.widget.webclient.bridge.WebLoginListener;

import java.io.InputStream;
import java.net.URL;

/**
 * WEB登录 处理
 * Created by ChenRui on 2017/1/19 22:04.
 */
public class CnblogLoginWebViewClient extends RaeWebViewClient {

    private WebLoginListener mLoginListener;

    private CnblogsLoginJsBridge mLoginJsBridge;


    @SuppressLint("JavascriptInterface")
    public CnblogLoginWebViewClient(WebView view, ProgressBar progressBar, WebLoginListener listener) {
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
        // 检查COOKIE
        String cookie = CookieManager.getInstance().getCookie(url);
        Log.w("Rae", "cookie = " + cookie);
        if (!TextUtils.isEmpty(cookie) && cookie.contains(".CNBlogsCookie") && mLoginListener != null) {
            mLoginListener.onWebLoginSuccess();
            return;
        }

        // 注入自动登录脚本
        if (url.contains("https://passport.cnblogs.com/user/signin")) {
            injectJavascriptFromAssets(view, "js/auto-login.js");
        }
    }

    @Override
    public void onLoadResource(WebView view, String url) {
        super.onLoadResource(view, url);
        Log.w("Rae", "onLoadResource = " + url);
    }

    @Override
    public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

        // 加载验证码
        if (url.contains("https://passport.cnblogs.com/BotDetectCaptcha.ashx?get=image") && mLoginListener != null) {
            try {
                URL u = new URL(url);
                InputStream stream = u.openConnection().getInputStream();
                mLoginListener.onWebLoginCodeBitmap(BitmapFactory.decodeStream(stream));
                return new WebResourceResponse("image/jpeg", "UTF-8", stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return super.shouldInterceptRequest(view, url);
    }
}
