package com.rae.cnblogs.dialog.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.rae.cnblogs.R;
import com.rae.cnblogs.model.MenuDialogItem;
import com.rae.cnblogs.model.MenuDialogViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 菜单弹窗口
 * Created by ChenRui on 2017/2/4 0004 17:06.
 */
public class MenuDialog extends SlideDialog {

    public interface OnMenuItemClickListener {
        void onMenuItemClick(MenuDialog dialog, MenuDialogItem item);
    }

    private final MenuDialogAdapter mAdapter;
    @BindView(R.id.rec_dialog_menu)
    RecyclerView mRecyclerView;

    private Object mTag;

    public MenuDialog(Context context) {
        super(context);
        setContentView(R.layout.dialog_blog_menu);

        WindowManager.LayoutParams attr = getWindow().getAttributes();
        attr.width = WindowManager.LayoutParams.MATCH_PARENT;
        attr.height = WindowManager.LayoutParams.WRAP_CONTENT;
        attr.gravity = Gravity.START | Gravity.END;
        getWindow().setAttributes(attr);
        getWindow().setDimAmount(0.5f);


        int margin = (int) getContext().getResources().getDimension(R.dimen.default_dialog_margin);
        InsetDrawable drawable = new InsetDrawable(new ColorDrawable(Color.TRANSPARENT), margin, 0, margin, 0);
        getWindow().setBackgroundDrawable(drawable);


        ButterKnife.bind(this);
        mAdapter = new MenuDialogAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
    }


    public void addItem(String name) {
        mAdapter.addItem(name);
    }

    public void addDeleteItem(String name) {
        addItem(new MenuDialogItem(name, R.color.red));
    }

    public void addItem(MenuDialogItem item) {
        mAdapter.mDataList.add(item);
    }


    public void setOnMenuItemClickListener(OnMenuItemClickListener onMenuItemClickListener) {
        mAdapter.mOnMenuItemClickListener = onMenuItemClickListener;
    }

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        mTag = tag;
    }

    private class MenuDialogAdapter extends RecyclerView.Adapter<MenuDialogViewHolder> {

        private final List<MenuDialogItem> mDataList = new ArrayList<>();

        private OnMenuItemClickListener mOnMenuItemClickListener;


        @Override
        public MenuDialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MenuDialogViewHolder(getLayoutInflater().inflate(R.layout.item_dialog_menu, parent, false));
        }

        @Override
        public void onBindViewHolder(MenuDialogViewHolder holder, final int position) {
            final MenuDialogItem dataItem = mDataList.get(position);
            holder.titleView.setText(mDataList.get(position).getName());
            if (dataItem.getColorId() > 0) {
                holder.titleView.setTextColor(ContextCompat.getColor(getContext(), dataItem.getColorId()));
            } else {
                holder.titleView.setTextColor(ContextCompat.getColor(getContext(), R.color.ph2));
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnMenuItemClickListener.onMenuItemClick(MenuDialog.this, dataItem);
                    dismiss();
                }
            });
        }

        @Override
        public int getItemCount() {
            return mDataList.size();
        }

        public void addItem(String name) {
            mDataList.add(new MenuDialogItem(name));
        }

    }

}
