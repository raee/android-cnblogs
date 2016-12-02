package com.rae.cnblogs.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.rae.cnblogs.R;
import com.rae.cnblogs.model.BlogItemViewHolder;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.core.Rae;

import java.util.List;

/**
 * Created by ChenRui on 2016/12/2 0002 19:43.
 */
public class BlogListItemAdapter extends RecyclerView.Adapter<BlogItemViewHolder> {

    private List<Blog> mBlogs;

    public BlogListItemAdapter() {
    }

    @Override
    public BlogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlogItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_list, parent, false));
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return Rae.getCount(mBlogs);
    }

    public void invalidate(List<Blog> data) {
        mBlogs = data;
    }
}
