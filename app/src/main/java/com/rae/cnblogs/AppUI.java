package com.rae.cnblogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
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

    public static void failed(Context context, String msg) {
        toastInCenter(context, msg);
    }

    public static void toast(Context context, String msg) {
        makeToast(context, msg).show();
    }

    public static void toastInCenter(Context context, String msg) {
        Toast toast = makeToast(context, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @NonNull
    @SuppressLint("ShowToast")
    private static Toast makeToast(Context context, String msg) {
        Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.bg_toast));
        TextView msgView = (TextView) toast.getView().findViewById(android.R.id.message);
        msgView.setTextSize(14);
        return toast;
    }

    public static void loading(Context context, String msg) {
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
