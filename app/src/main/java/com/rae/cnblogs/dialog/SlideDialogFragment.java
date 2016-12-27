package com.rae.cnblogs.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.rae.cnblogs.R;

import butterknife.ButterKnife;

/**
 * Fragment Slide dialog
 * Created by ChenRui on 2016/12/16 00:12.
 */
public class SlideDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new SlideDialog(getContext(), getDialogThemeId());
        View view = LayoutInflater.from(getContext()).inflate(getLayoutId(), null);
        dialog.setContentView(view);
        ButterKnife.bind(this, view);
        onDialogViewCreate(view);
        return dialog;
    }

    protected int getDialogThemeId() {
        return R.style.SlideDialog;
    }

    protected void onDialogViewCreate(View view) {
    }

    protected int getLayoutId() {
        return 0;
    }
}
