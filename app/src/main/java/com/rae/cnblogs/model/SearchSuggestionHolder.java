package com.rae.cnblogs.model;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rae.cnblogs.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 搜索建议
 * Created by ChenRui on 2017/8/29 0029 9:57.
 */
public class SearchSuggestionHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.img_selected)
    ImageView mSelectedView;

    @BindView(R.id.tv_title)
    TextView mTitleView;

    public SearchSuggestionHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public ImageView getSelectedView() {
        return mSelectedView;
    }

    public TextView getTitleView() {
        return mTitleView;
    }
}
