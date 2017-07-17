package com.rae.cnblogs.model;

import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.bean.CategoryBean;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by ChenRui on 2017/7/16 0016 23:52.
 */
public class CategoriesOverallItem extends AbstractFlexibleItem<CategoriesOverallItem.CategoriesViewHolder> {

    private CategoryBean mCategory;

    public CategoriesOverallItem(CategoryBean category) {
        mCategory = category;
    }

    public CategoryBean getCategory() {
        return mCategory;
    }

    @Override
    public boolean equals(Object o) {
        if (mCategory == o) return true;
        if (o == null || mCategory.getClass() != o.getClass()) return false;
        CategoryBean that = (CategoryBean) o;
        return mCategory.getCategoryId().equalsIgnoreCase(that.getCategoryId());
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_category;
    }

    @Override
    public CategoriesViewHolder createViewHolder(View view, FlexibleAdapter flexibleAdapter) {
        return new CategoriesViewHolder(view, flexibleAdapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter flexibleAdapter, CategoriesViewHolder holder, int i, List list) {
        holder.setTitle(mCategory.getName());
        holder.itemView.setBackgroundResource(isDraggable() ? R.drawable.bg_category : R.drawable.bg_category_lock);
        holder.setTitleColor(isDraggable() ? R.color.ph2 : R.color.ph4);
    }

    @Override
    public boolean isDraggable() {
        // 首页、推荐不能拖动
        return !"SiteHome".equalsIgnoreCase(mCategory.getType()) && !"Picked".equalsIgnoreCase(mCategory.getType());
    }

    static class CategoriesViewHolder extends FlexibleViewHolder {

        TextView mTitleView;

        CategoriesViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mTitleView = (TextView) view.findViewById(R.id.tv_name);
        }

        public void setTitle(String text) {
            mTitleView.setText(text);
        }

        public void setTitleColor(int color) {
            mTitleView.setTextColor(ContextCompat.getColor(itemView.getContext(), color));
        }
    }
}
