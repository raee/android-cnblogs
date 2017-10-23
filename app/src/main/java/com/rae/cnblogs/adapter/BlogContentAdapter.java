package com.rae.cnblogs.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.R;

/**
 * Created by ChenRui on 2017/10/24 0024 1:16.
 */
public class BlogContentAdapter extends RecyclerView.Adapter {

    public static class FragmentViewHolder extends RecyclerView.ViewHolder {


        public FragmentViewHolder(View itemView) {
            super(itemView);

//            fm.beginTransaction().add

        }
    }

    public static class BlogCommentHolder extends RecyclerView.ViewHolder {
        public BlogCommentHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (viewType == 0) {
            return new FragmentViewHolder(inflater.inflate(R.layout.item_fragment_view, parent, false));
        } else {
            return new BlogCommentHolder(inflater.inflate(R.layout.item_fragment_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
