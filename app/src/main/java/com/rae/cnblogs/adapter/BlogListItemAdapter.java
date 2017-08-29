package com.rae.cnblogs.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.AppUI;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.BlogItemViewHolder;
import com.rae.cnblogs.model.ItemLoadingViewHolder;
import com.rae.cnblogs.sdk.UserProvider;
import com.rae.cnblogs.sdk.bean.BlogBean;
import com.rae.cnblogs.sdk.bean.BlogType;
import com.rae.cnblogs.widget.PlaceholderView;
import com.rae.swift.Rx;

import java.util.ArrayList;
import java.util.List;

/**
 * 博客列表ITEM
 * Created by ChenRui on 2016/12/2 0002 19:43.
 */
public class BlogListItemAdapter extends BaseItemAdapter<BlogBean, RecyclerView.ViewHolder> implements View.OnClickListener {

    private final BlogType mBlogType;
    private final PlaceholderView mPlaceholderView;


    public BlogListItemAdapter(Context context, BlogType type, PlaceholderView placeholderView) {
        mPlaceholderView = placeholderView;
        mBlogType = type;
        loading();
    }

    public void loading() {
        int size = 5;
        List<BlogBean> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            BlogBean m = new BlogBean();
            m.setTag("loading");
            data.add(m);
        }
        invalidate(data);
    }

    public void empty() {
        if (mDataList != null) {
            mDataList.clear();
        }
        List<BlogBean> data = new ArrayList<>();
        BlogBean m = new BlogBean();
        m.setTag("empty");
        invalidate(data);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 0) {
            return super.getItemViewType(position);
        }
        BlogBean blog = getDataItem(position);
        if (blog != null && TextUtils.equals("loading", blog.getTag())) {
            return VIEW_TYPE_LOADING;
        }
        if (blog != null && TextUtils.equals("empty", blog.getTag())) {
            return VIEW_TYPE_EMPTY;
        }
        if (mBlogType == BlogType.NEWS) {
            return VIEW_TYPE_NEWS;
        }
        return VIEW_TYPE_NORMAL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return new ItemLoadingViewHolder(inflateView(parent, R.layout.item_list_loading));
        }
        if (viewType == VIEW_TYPE_EMPTY) {
            return new ItemLoadingViewHolder(mPlaceholderView);
        }
        if (viewType == VIEW_TYPE_NEWS) {
            return new BlogItemViewHolder(inflateView(parent, R.layout.item_news_list));
        }

        return new BlogItemViewHolder(inflateView(parent, R.layout.item_blog_list));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder vh, int position, final BlogBean m) {

        if (getItemViewType(position) == VIEW_TYPE_LOADING || getItemViewType(position) == VIEW_TYPE_EMPTY) {
            return;
        }

        BlogItemViewHolder holder = (BlogItemViewHolder) vh;

        switch (mBlogType) {
            case BLOG:
                holder.authorLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 判断是否已经登录
                        if (UserProvider.getInstance().isLogin()) {
                            AppRoute.jumpToBlogger(v.getContext(), m.getBlogApp());
                        } else {
                            AppRoute.jumpToLogin(v.getContext());
                        }
                    }
                });
                RaeImageLoader.displayHeaderImage(m.getAvatar(), holder.avatarView);
                break;
            case BLOGGER: // 作者
                holder.authorLayout.setVisibility(View.GONE);
                break;
            case KB: // 知识库
                holder.authorLayout.setVisibility(View.GONE);
                holder.commentView.setVisibility(View.GONE);
                holder.summaryView.setTextSize(13);
                break;
            case NEWS:
                if (!TextUtils.isEmpty(m.getAvatar())) {
                    RaeImageLoader.displayImage(m.getAvatar(), holder.avatarView);
                    holder.avatarView.setVisibility(View.VISIBLE);
                } else {
                    holder.avatarView.setVisibility(View.GONE);
                }
                break;
            default:
                RaeImageLoader.displayHeaderImage(m.getAvatar(), holder.avatarView);
                break;
        }
        holder.authorView.setText(m.getAuthor());
        holder.titleView.setText(formatHtml(m.getTitle()));
        holder.summaryView.setText(formatHtml(m.getSummary()));
        holder.dateView.setText(m.getPostDate());
        holder.readerView.setText(m.getViews());
        holder.likeView.setText(m.getLikes());
        holder.commentView.setText(m.getComment());

        int titleColor = m.isReaded() ? R.color.ph4 : R.color.ph1;
        int summaryColor = m.isReaded() ? R.color.ph4 : R.color.ph2;

        holder.titleView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), titleColor));
        holder.summaryView.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), summaryColor));

        holder.itemView.setTag(m);
        holder.itemView.setOnClickListener(this);

        if (mBlogType != BlogType.NEWS) {
            showThumbImages(m, holder);
        }

    }

    private CharSequence formatHtml(String text) {
        if (TextUtils.isEmpty(text)) return text;
        // <strong>android</strong>
        text = text.replace("<strong>", "<strong><font color='red'>").replace("</strong>", "</font></strong>");
        return Html.fromHtml(text);
    }

    /**
     * 预览图处理
     *
     * @param m
     * @param holder
     */
    private void showThumbImages(BlogBean m, BlogItemViewHolder holder) {
        List<String> thumbs = m.getThumbs();
        if (Rx.isEmpty(thumbs)) {
            //  没有预览图
            holder.largeThumbView.setVisibility(View.GONE);
            holder.thumbLayout.setVisibility(View.GONE);
        } else if (thumbs.size() < 3 && thumbs.size() > 0) {
            // 一张预览图
            holder.largeThumbView.setVisibility(View.VISIBLE);
            holder.thumbLayout.setVisibility(View.GONE);
            RaeImageLoader.displayImage(thumbs.get(0), holder.largeThumbView);
        } else if (thumbs.size() >= 3) {
            holder.largeThumbView.setVisibility(View.GONE);
            holder.thumbLayout.setVisibility(View.VISIBLE);
            // 取三张预览图
            RaeImageLoader.displayImage(thumbs.get(0), holder.thumbOneView);
            RaeImageLoader.displayImage(thumbs.get(1), holder.thumbTwoView);
            RaeImageLoader.displayImage(thumbs.get(2), holder.thumbThreeView);
        } else {
            holder.largeThumbView.setVisibility(View.GONE);
            holder.thumbLayout.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View view) {
        BlogBean blog = (BlogBean) view.getTag();
        if (blog == null) {
            AppUI.failed(view.getContext(), "博客信息为空");
            return;
        }
        // 设置为已读
        blog.setReaded(true);
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
            }
        }, 1000);
        AppRoute.jumpToBlogContent(view.getContext(), blog, mBlogType);
    }

}
