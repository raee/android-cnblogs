package com.rae.cnblogs.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.BlogCommentViewHolder;
import com.rae.cnblogs.sdk.bean.BlogComment;


/**
 * 评论列表ITEM
 * Created by ChenRui on 2016/12/15 22:48.
 */
public class BlogCommentItemAdapter extends BaseItemAdapter<BlogComment, BlogCommentViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private OnBlogCommentItemClick mOnBlogCommentItemLongClick;

    @Override
    public void onClick(View v) {
        BlogComment m = (BlogComment) v.getTag();
        if (mOnBlogCommentItemClick == null || m == null) return;
        mOnBlogCommentItemClick.onItemClick(m);
    }

    @Override
    public boolean onLongClick(View v) {
        BlogComment m = (BlogComment) v.getTag();
        if (mOnBlogCommentItemLongClick == null || m == null) return false;
        mOnBlogCommentItemLongClick.onItemClick(m);
        return true;
    }


    public interface OnBlogCommentItemClick {
        void onItemClick(BlogComment comment);
    }

    private OnBlogCommentItemClick mOnBlogCommentItemClick;

    public void setOnBlogCommentItemClick(OnBlogCommentItemClick onBlogCommentItemClick) {
        mOnBlogCommentItemClick = onBlogCommentItemClick;
    }

    public void setOnBlogCommentItemLongClick(OnBlogCommentItemClick onBlogCommentItemClick) {
        mOnBlogCommentItemLongClick = onBlogCommentItemClick;
    }

    @Override
    public BlogCommentViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BlogCommentViewHolder(inflateView(parent, R.layout.item_blog_comment));
    }

    @Override
    public void onBindViewHolder(BlogCommentViewHolder holder, int position, BlogComment m) {
        holder.authorTitleView.setText(m.getAuthorName());
        holder.dateView.setText(m.getDate());
        holder.contentView.setText(m.getBody());
        holder.itemView.setTag(m);
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);

        if (!TextUtils.isEmpty(m.getAvatar())) {
            ImageLoader.getInstance().displayImage(m.getAvatar(), holder.avatarView, RaeImageLoader.headerOption());
        }
    }
}
