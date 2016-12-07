package com.rae.cnblogs;

import android.content.Context;
import android.widget.Toast;

/**
 * UI 规范
 * Created by ChenRui on 2016/12/8 00:22.
 */
public final class AppUI {

    public static void toast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public static void loading(Context context, String msg) {
        toast(context, msg);
    }

    public static void loading(Context context) {
        loading(context, R.string.tips_loading);
    }

    public static void loading(Context context, int resId) {
        loading(context, context.getString(resId));
    }

    public static void success(Context context, int resId) {
        toast(context, context.getString(resId));
    }
}
