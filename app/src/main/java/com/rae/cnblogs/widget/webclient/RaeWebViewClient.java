package com.rae.cnblogs.widget.webclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.rae.cnblogs.widget.AppLayout;
import com.rae.cnblogs.widget.PlaceholderView;

import java.io.InputStream;
import java.lang.ref.WeakReference;

public class RaeWebViewClient extends WebViewClient {

    private final WeakReference<AppLayout> mAppLayout;
    private WeakReference<ProgressBar> mProgressBar;
    private WeakReference<PlaceholderView> mPlaceholderViewWeakReference;
    private Context mContext;


    public RaeWebViewClient(ProgressBar progressBar, AppLayout appLayout) {
        mContext = progressBar.getContext();
        mProgressBar = new WeakReference<>(progressBar);
        mAppLayout = new WeakReference<>(appLayout);
    }

    private void dismissProgress() {
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
        if (mProgressBar.get() != null) {
            mProgressBar.get().startAnimation(animation);
            mProgressBar.get().setVisibility(View.GONE);
        }

        if (mAppLayout.get() != null) {
            mAppLayout.get().refreshComplete();
        }

    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        if (mProgressBar.get() != null) {
            mProgressBar.get().setVisibility(View.VISIBLE);
        }
        dismissPlaceHolder();
    }


    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        dismissProgress();
        // 忽略博文的网页
        if (!url.contains("view.html")) {
            Activity at = (Activity) mContext;
            at.setTitle(view.getTitle());
            injectJavascriptFromAssets(view, "js/rae.js");
        }
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed(); // 忽略证书错误
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        super.onReceivedError(view, errorCode, description, failingUrl);
        showEmpty(view, failingUrl);
        view.stopLoading();
    }

    private void showEmpty(WebView view, String url) {
        if (!view.getUrl().equalsIgnoreCase(url)) return;
        if (mPlaceholderViewWeakReference != null && mPlaceholderViewWeakReference.get() != null) {
            mPlaceholderViewWeakReference.get().networkError();
            mPlaceholderViewWeakReference.get().setEmptyMessage("网络连接错误，请重试");
        }
    }


    /**
     * 注入脚本
     *
     * @param view
     * @param scriptContent 脚本内容
     */
    private void injectJavascript(WebView view, String scriptContent) {
        String js = "javascript:(function(){" + scriptContent + "})();";
        view.loadUrl(js);
    }

    public void destroy() {
        mProgressBar.clear();
        mAppLayout.clear();
        mProgressBar = null;
        mContext = null;
    }


    /**
     * 从资源文件中注入脚本
     *
     * @param view
     * @param filePath 文件在assets 的路径
     */
    private void injectJavascriptFromAssets(WebView view, String filePath) {
        try {
            InputStream in = view.getContext().getResources().getAssets().open(filePath);
            byte[] data = new byte[in.available()];
            in.read(data);
            in.close();
            String content = new String(data);
            injectJavascript(view, content);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPlaceHolderView(PlaceholderView view) {
        if (mPlaceholderViewWeakReference != null) {
            mPlaceholderViewWeakReference.clear();
        }

        mPlaceholderViewWeakReference = new WeakReference<>(view);
    }

    private void dismissPlaceHolder() {
        if (mPlaceholderViewWeakReference != null && mPlaceholderViewWeakReference.get() != null) {
            mPlaceholderViewWeakReference.get().dismiss();
        }
    }

}
