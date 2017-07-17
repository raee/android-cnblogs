package com.rae.cnblogs.adapter;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;

/**
 * Created by ChenRui on 2017/7/16 0016 23:48.
 */
public class CategoriesOverallAdapter extends FlexibleAdapter<AbstractFlexibleItem> {

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

    @Override
    public boolean shouldMove(int fromPosition, int toPosition) {
        return !(toPosition == 0 || toPosition == 1) && super.shouldMove(fromPosition, toPosition);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        mHandler.removeMessages(0);
        mHandler.sendEmptyMessageDelayed(0, 500);
        return super.onItemMove(fromPosition, toPosition);
    }
}
