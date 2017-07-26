package com.rae.cnblogs.widget.webclient;

import android.content.Context;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import com.google.gson.reflect.TypeToken;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.sdk.AppGson;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * JS 接口
 * Created by ChenRui on 2016/12/27 23:14.
 */
public class RaeJavaScriptBridge {

    private WeakReference<Context> mReference;

    public RaeJavaScriptBridge(Context context) {
        mReference = new WeakReference<>(context);
    }

    private String html;

    @JavascriptInterface
    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    @JavascriptInterface
    public void onImageClick(String url, String images) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(images)) {
            return;
        }
        ArrayList<String> imageList = AppGson.get().fromJson(images, new TypeToken<ArrayList<String>>() {
        }.getType());
        int index = Math.max(0, imageList.indexOf(url));
        if (mReference.get() != null) {
            AppRoute.jumpToImagePreview(mReference.get(), imageList, index);
        }
    }

}
