package com.rae.cnblogs.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Fragment Slide dialog
 * Created by ChenRui on 2016/12/16 00:12.
 */
public class SlideDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new SlideDialog(getContext());
    }
}
