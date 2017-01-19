package com.rae.cnblogs.widget.webclient.bridge;

import android.graphics.Bitmap;

/**
 * Created by ChenRui on 2017/1/19 22:47.
 */
public interface LoginListener {
    void onLoginSuccess();

    void onLoginError(String msg);

    void onLoginCodeError(String msg);

    /**
     * 加载验证码
     *
     * @param bitmap
     */
    void onLoginCodeBitmap(Bitmap bitmap);
}
