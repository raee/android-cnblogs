package com.rae.cnblogs.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.rae.cnblogs.model.CategoriesOverallItem;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by ChenRui on 2017/7/16 0016 23:48.
 */
public class CategoriesOverallAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

    private boolean mIsRemoveMode;

    public interface CategoryDragListener {


        void onItemDrag();
    }

    private CategoryDragListener mCategoryDragListener;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            // 更新分类
            if (mCategoryDragListener != null) {
                mCategoryDragListener.onItemDrag();
            }
            return false;
        }
    });


    public CategoriesOverallAdapter(@Nullable List<AbstractFlexibleItem> items) {
        super(items);
    }

    public void setCategoryDragListener(CategoryDragListener categoryDragListener) {
        mCategoryDragListener = categoryDragListener;
    }

    /**
     * 移除模式切换
     */
    public void switchMode() {
        mIsRemoveMode = !mIsRemoveMode;
    }

    public boolean isRemoveMode() {
        return mIsRemoveMode;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
        // 切换模型
        CategoriesOverallItem.CategoriesViewHolder viewHolder = (CategoriesOverallItem.CategoriesViewHolder) holder;
        viewHolder.onRemoveMode(mIsRemoveMode, getItem(position).isDraggable());
    }

    @Override
    public boolean shouldMove(int fromPosition, int toPosition) {
        return !(toPosition == 0 || toPosition == 1) && super.shouldMove(fromPosition, toPosition);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 1000);
        return super.onItemMove(fromPosition, toPosition);
    }
}
