package com.rae.cnblogs.widget.webclient;

import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.dialog.DialogProvider;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.IAppDialogClickListener;

/**
 * Created by ChenRui on 2016/12/27 23:12.
 */
public class RaeWebChromeClient extends WebChromeClient {

    private ProgressBar mProgressBar;

    public RaeWebChromeClient(ProgressBar progressBar) {
        mProgressBar = progressBar;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        AppUI.toast(view.getContext(), message);
        result.cancel();
        return true;
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        IAppDialog dialog = DialogProvider.create(view.getContext());
        dialog.setMessage(message);
        dialog.setButtonVisibility(IAppDialog.BUTTON_NEGATIVE, View.GONE);
        dialog.setOnClickListener(IAppDialog.BUTTON_POSITIVE, new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                result.confirm();
            }
        });
        return true;
    }

    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        IAppDialog dialog = DialogProvider.create(view.getContext());
        dialog.setMessage(message);
        dialog.setOnClickListener(IAppDialog.BUTTON_POSITIVE, new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                result.confirm();
            }
        });

        dialog.setOnClickListener(IAppDialog.BUTTON_NEGATIVE, new IAppDialogClickListener() {
            @Override
            public void onClick(IAppDialog dialog, int buttonType) {
                result.cancel();
            }
        });

        return true;
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
