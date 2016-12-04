package com.rae.cnblogs.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.BlogItemViewHolder;
import com.rae.cnblogs.sdk.bean.Blog;
import com.rae.core.Rae;

import java.util.List;

/**
 * Created by ChenRui on 2016/12/2 0002 19:43.
 */
public class BlogListItemAdapter extends RecyclerView.Adapter<BlogItemViewHolder> {

    private List<Blog> mBlogList;
    private DisplayImageOptions mAvatarOptions;

    public BlogListItemAdapter() {
        mAvatarOptions = RaeImageLoader.defaultOptions()
                .displayer(new FadeInBitmapDisplayer(1000))
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showImageOnLoading(R.mipmap.ic_launcher)
                .showImageOnFail(R.mipmap.ic_launcher)
                .build();
    }

    @Override
    public BlogItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BlogItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_blog_list, parent, false));
    }

    @Override
    public void onBindViewHolder(BlogItemViewHolder holder, int position) {
        Blog m = mBlogList.get(position);

        ImageLoader.getInstance().displayImage(m.getAvatar(), holder.avatarView, mAvatarOptions);
        holder.authorView.setText(m.getAuthor());
        holder.titleView.setText(m.getTitle());
        holder.summaryView.setText(m.getSummary());
        holder.dateView.setText(m.getPostDate());
        holder.readerView.setText(m.getViews());
        holder.likeView.setText(m.getLikes());
        holder.commentView.setText(m.getComment());
    }

    @Override
    public int getItemCount() {
        return Rae.getCount(mBlogList);
    }

    public void invalidate(List<Blog> data) {
        mBlogList = data;
    }
}
