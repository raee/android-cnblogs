package com.rae.cnblogs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.ContextWrapper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
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
        TextView msgView = toast.getView().findViewById(android.R.id.message);
        if (msgView != null) {
            int p = 12;
            msgView.setPadding(p, p, p, p);
            msgView.setTextSize(14);
            msgView.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
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
            if (loadingDialog.getWindow() != null && loadingDialog.getWindow().getDecorView() != null) {
                loadingDialog.dismiss();
            }
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
        Toast toast = makeToast(context, context.getString(resId));
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView msgView = toast.getView().findViewById(android.R.id.message);
        if (msgView != null) {
            int p = 20;
            msgView.setPadding(p, p, p, p);
            msgView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.toast_success, 0, 0);
            msgView.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        }
        toast.show();
    }

    public static Toast failed(Context context, String msg) {
        Toast toast = makeToast(context, msg);
        toast.setGravity(Gravity.CENTER, 0, 0);
        TextView msgView = toast.getView().findViewById(android.R.id.message);
        if (msgView != null) {
            int p = 20;
            msgView.setPadding(p, p, p, p);
//            msgView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.toast_failed, 0, 0);
//            msgView.setCompoundDrawablePadding((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, context.getResources().getDisplayMetrics()));
        }
        toast.show();
        return toast;
    }

    public static void dismiss() {
        try {
            if (dialogWeakReference == null || dialogWeakReference.get() == null) {
                return;
            }
            IAppDialog dialog = dialogWeakReference.get();
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            dialogWeakReference.clear();
            dialogWeakReference = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
