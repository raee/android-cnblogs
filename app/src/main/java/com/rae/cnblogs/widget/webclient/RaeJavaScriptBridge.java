package com.rae.cnblogs.widget.webclient;

import android.util.Log;
import android.webkit.JavascriptInterface;

/**
 * JS 接口
 * Created by ChenRui on 2016/12/27 23:14.
 */
public class RaeJavaScriptBridge {

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
        Log.d("Rae", "图片点击：" + url + ";\n 图片组：" + images);
    }

}
