package com.rae.cnblogs.widget.webclient;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.io.InputStream;

/**
 * Created by ChenRui on 2016/12/27 23:10.
 */
public class RaeWebViewClient extends WebViewClient {

    private ProgressBar mProgressBar;
    private Context mContext;


    public RaeWebViewClient(ProgressBar progressBar) {
        mContext = progressBar.getContext();
        mProgressBar = progressBar;
    }

    private void dismissProgress() {
        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
        mProgressBar.startAnimation(animation);
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        dismissProgress();
        if (url.startsWith("http")) {
            Activity at = (Activity) view.getContext();
            at.setTitle(view.getTitle());
            injectJavascriptFromAssets(view, "js/rae.js");
        }
    }

    /**
     * 注入脚本
     *
     * @param view
     * @param scriptContent 脚本内容
     */
    public void injectJavascript(WebView view, String scriptContent) {
        String js = "javascript:(function(){" + scriptContent + "})();";
        view.loadUrl(js);
    }


    /**
     * 从资源文件中注入脚本
     *
     * @param view
     * @param filePath 文件在assets 的路径
     */
    public void injectJavascriptFromAssets(WebView view, String filePath) {
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
}
