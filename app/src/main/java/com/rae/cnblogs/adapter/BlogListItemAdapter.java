package com.rae.cnblogs.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.BlogItemViewHolder;
import com.rae.cnblogs.sdk.bean.Blog;

/**
 * 博客列表ITEM
 * Created by ChenRui on 2016/12/2 0002 19:43.
 */
public class BlogListItemAdapter extends BaseItemAdapter<Blog, BlogItemViewHolder> implements View.OnClickListener {

    public static final int VIEW_TYPE_NORMAL = 0;
    /**
     * 没有用户信息的类型
     */
    public static final int VIEW_TYPE_WITHOUT_AVATAR = 1;

    private int mViewType; // 显示类型

    private DisplayImageOptions mAvatarOptions;

    public BlogListItemAdapter() {
        mAvatarOptions = RaeImageLoader.headerOption();
    }

    public void setViewType(int viewType) {
        mViewType = viewType;
    }

    @Override
    public BlogItemViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return new BlogItemViewHolder(inflateView(parent, R.layout.item_blog_list));
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder holder, int position, Blog m) {

        switch (mViewType) {
            case VIEW_TYPE_WITHOUT_AVATAR:
                holder.authorLayout.setVisibility(View.GONE);
                break;
        }

        ImageLoader.getInstance().displayImage(m.getAvatar(), holder.avatarView, mAvatarOptions);

        holder.authorView.setText(m.getAuthor());
        holder.titleView.setText(m.getTitle());
        holder.summaryView.setText(m.getSummary());
        holder.dateView.setText(m.getPostDate());
        holder.readerView.setText(m.getViews());
        holder.likeView.setText(m.getLikes());
        holder.commentView.setText(m.getComment());


        holder.itemView.setTag(m);
        holder.itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Blog blog = (Blog) view.getTag();
        AppRoute.jumpToBlogContent(view.getContext(), blog);
    }
}
