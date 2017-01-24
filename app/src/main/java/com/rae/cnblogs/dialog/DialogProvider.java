package com.rae.cnblogs.dialog;

import android.content.Context;

import com.rae.cnblogs.dialog.impl.DefaultDialog;
import com.rae.cnblogs.dialog.impl.HintCardDialog;

/**
 * 对话框提供者
 * Created by ChenRui on 2017/1/24 0024 14:39.
 */
public final class DialogProvider {

    // 博客分享对话框
    public static final int TYPE_DEFAULT = 0;
    public static final int TYPE_HINT_CARD = 1;

    public static IAppDialog create(Context context) {
        return create(context, TYPE_DEFAULT);
    }

    public static IAppDialog create(Context context, int type) {
        switch (type) {
            case TYPE_DEFAULT:
                return new DefaultDialog(context);
            case TYPE_HINT_CARD:
                return new HintCardDialog(context);
            default:
                throw new NullPointerException("没有找到对话框类型！");
        }
    }

}
