package com.rae.cnblogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.rae.cnblogs.dialog.DialogProvider;
import com.rae.cnblogs.dialog.IAppDialog;
import com.rae.cnblogs.dialog.impl.LoadingDialog;

import java.lang.ref.WeakReference;


/**
 * UI 规范
 * Created by ChenRui on 2016/12/8 00:22.
 */
public final class AppUI {

    private static WeakReference<IAppDialog> dialogWeakReference;

    public static Toast failed(Context context, String msg) {
        return toastInCenter(context, msg);
    }

    public static Toast toast(Context context, String msg) {
        Toast toast = makeToast(context, msg);
        toast.show();
        return toast;

    }

    public static Toast toastInCenter(Context context, String msg) {
        Toast toast = makeToast(context, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        return toast;
    }

    @NonNull
    @SuppressLint("ShowToast")
    private static Toast makeToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        if (context == null) {
            return toast;
        }
        toast.getView().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_toast));
        TextView msgView = (TextView) toast.getView().findViewById(android.R.id.message);
        msgView.setTextSize(14);
        msgView.setTextColor(ContextCompat.getColor(context, android.R.color.white));
        return toast;
    }

    public static IAppDialog loading(Context context, String msg) {
        IAppDialog dialog;
        if (dialogWeakReference == null || dialogWeakReference.get() == null) {
            dialog = DialogProvider.create(context, DialogProvider.TYPE_LOADING);
            dialogWeakReference = new WeakReference<>(dialog);
        } else {
            dialog = dialogWeakReference.get();
        }

        // 不是当前的
        LoadingDialog loadingDialog = (LoadingDialog) dialog;

        if (!((ContextWrapper) loadingDialog.getContext()).getBaseContext().equals(context)) {
            loadingDialog.dismiss();
            dialogWeakReference.clear();
            dialogWeakReference = null;
            dialog = DialogProvider.create(context, DialogProvider.TYPE_LOADING);
            dialogWeakReference = new WeakReference<>(dialog);
        }

        dialog.setMessage(msg);
        dialog.show();
        return dialog;
    }

    public static IAppDialog loading(Context context) {
        return loading(context, R.string.tips_loading);
    }

    public static IAppDialog loading(Context context, int resId) {
        return loading(context, context.getString(resId));
    }

    public static void success(Context context, int resId) {
        toast(context, context.getString(resId));
    }

    public static void dismiss() {
        if (dialogWeakReference == null || dialogWeakReference.get() == null) {
            return;
        }
        IAppDialog dialog = dialogWeakReference.get();
        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        dialogWeakReference.clear();
        dialogWeakReference = null;
    }



}
