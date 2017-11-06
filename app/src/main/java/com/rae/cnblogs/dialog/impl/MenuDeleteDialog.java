package com.rae.cnblogs.dialog.impl;

import android.content.Context;
import android.view.Gravity;
import android.view.WindowManager;

import com.rae.cnblogs.R;
import com.rae.cnblogs.model.MenuDialogItem;

/**
 * 删除对话框
 * Created by ChenRui on 2017/11/6 0006 23:34.
 */
public class MenuDeleteDialog extends MenuDialog implements MenuDialog.OnMenuItemClickListener {

    public interface onDeleteClickListener {
        void onMenuDeleteClicked();
    }

    onDeleteClickListener mOnDeleteClickListener;

    public MenuDeleteDialog(Context context) {
        super(context);
        addDeleteItem(getContext().getString(R.string.delete));
        addItem(getContext().getString(R.string.cancel));

        setOnMenuItemClickListener(this);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }


    public void setOnDeleteClickListener(onDeleteClickListener onDeleteClickListener) {
        mOnDeleteClickListener = onDeleteClickListener;
    }

    @Override
    public void onMenuItemClick(MenuDialog dialog, MenuDialogItem item) {
        if (mOnDeleteClickListener != null && item.getName().equalsIgnoreCase(getContext().getString(R.string.delete))) {
            mOnDeleteClickListener.onMenuDeleteClicked();
        }
    }


}
