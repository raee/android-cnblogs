package com.rae.cnblogs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.model.BookmarksViewHolder;
import com.rae.cnblogs.sdk.bean.BookmarksBean;

/**
 * Created by ChenRui on 2017/7/16 0016 13:47.
 */
public class BookmarksAdapter extends BaseItemAdapter<BookmarksBean, BookmarksViewHolder> {


    private onItemClickListener<BookmarksBean> mOnItemDeleteClickListener;

    public void setOnItemDeleteClickListener(onItemClickListener<BookmarksBean> onItemDeleteClickListener) {
        mOnItemDeleteClickListener = onItemDeleteClickListener;
    }

    @Override
    public BookmarksViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BookmarksViewHolder(inflateView(parent, R.layout.item_bookmark_list));
    }

    @Override
    public void onBindViewHolder(BookmarksViewHolder holder, int position, final BookmarksBean m) {
        holder.mBlogTitle.setText(m.getTitle());
        holder.mBlogSummary.setText(m.getSummary());
        holder.mBlogDate.setText(m.getDateAdded());
        if (mOnItemDeleteClickListener != null) {
            holder.mBlogDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemDeleteClickListener.onItemClick(m);
                }
            });
        }
    }
}
