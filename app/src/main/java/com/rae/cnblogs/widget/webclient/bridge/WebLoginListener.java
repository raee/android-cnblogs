package com.rae.cnblogs.widget.webclient.bridge;

import android.graphics.Bitmap;

/**
 * WEB 登录回调
 * Created by ChenRui on 2017/1/19 22:47.
 */
public interface WebLoginListener {

    void onWebLoginSuccess();

    void onWebLoginError(String msg);

    void onWebLoginCodeError(String msg);

    /**
     * 加载验证码
     *
     * @param bitmap
     */
    void onWebLoginCodeBitmap(Bitmap bitmap);
}
