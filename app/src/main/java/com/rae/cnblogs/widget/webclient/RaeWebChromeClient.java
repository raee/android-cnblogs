package com.rae.cnblogs.widget.webclient;

import android.content.Context;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * Created by ChenRui on 2016/12/27 23:12.
 */
public class RaeWebChromeClient extends WebChromeClient {

    private ProgressBar mProgressBar;
    private Context mContext;

    public RaeWebChromeClient(ProgressBar progressBar) {
        mContext = progressBar.getContext();
        mProgressBar = progressBar;
    }


    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        super.onProgressChanged(view, newProgress);
        showProgress(newProgress);
    }

    private void showProgress(int progress) {
        mProgressBar.setProgress(progress);
    }
}
