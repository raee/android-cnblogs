package com.rae.cnblogs.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rae.cnblogs.AppRoute;
import com.rae.cnblogs.R;
import com.rae.cnblogs.RaeImageLoader;
import com.rae.cnblogs.model.ItemLoadingViewHolder;
import com.rae.cnblogs.model.MomentMessageHolder;
import com.rae.cnblogs.model.SimpleViewHolder;
import com.rae.cnblogs.sdk.bean.MomentCommentBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 回复我的
 * Created by ChenRui on 2017/10/27 0027 10:49.
 */
public class MomentMessageAdapter extends BaseItemAdapter<MomentCommentBean, SimpleViewHolder> {


    public MomentMessageAdapter() {
        int size = 5;
        List<MomentCommentBean> data = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            MomentCommentBean m = new MomentCommentBean();
            m.setId("loading");
            data.add(m);
        }
        invalidate(data);
    }

    @Override
    public int getItemViewType(int position) {
        MomentCommentBean dataItem = getDataItem(position);
        if (dataItem != null && "loading".equalsIgnoreCase(dataItem.getId())) {
            return VIEW_TYPE_LOADING;
        }
        return super.getItemViewType(position);
    }

    @Override
    protected void onBindItemClickListener(SimpleViewHolder holder, int position, MomentCommentBean dataItem) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) return;
        super.onBindItemClickListener(holder, position, dataItem);
    }


    @Override
    public SimpleViewHolder onCreateViewHolder(LayoutInflater inflater, ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOADING) {
            return new ItemLoadingViewHolder(inflateView(parent, R.layout.item_list_loading));
        }
        return new MomentMessageHolder(inflateView(parent, R.layout.item_moment_message));
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder viewHolder, int position, MomentCommentBean m) {
        int viewType = getItemViewType(position);
        if (viewType == VIEW_TYPE_LOADING) return;


        MomentMessageHolder holder = (MomentMessageHolder) viewHolder;

        OnBloggerClickListener onBloggerClickListener = new OnBloggerClickListener(m.getBlogApp());

        holder.authorView.setOnClickListener(onBloggerClickListener);
        holder.avatarView.setOnClickListener(onBloggerClickListener);
        holder.dateView.setOnClickListener(onBloggerClickListener);
        holder.sourceView.setOnClickListener(new OnSourceClickListener(m.getUserAlias(), m.getIngId()));

        RaeImageLoader.displayHeaderImage(m.getAvatar(), holder.avatarView);
        holder.authorView.setText(m.getAuthorName());
        holder.dateView.setText(m.getPostTime());
        holder.summaryView.setText(m.getContent());
        holder.sourceView.setText(m.getReferenceContent());
    }

    public static class OnSourceClickListener implements View.OnClickListener {

        private final String mIngId;
        private final String mUserAlias;

        public OnSourceClickListener(String userAlias, String ingId) {
            mUserAlias = userAlias;
            mIngId = ingId;
        }

        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(mIngId)) {
                AppRoute.jumpToMomentDetail(v.getContext(), mUserAlias, mIngId);
            }
        }
    }

    public static class OnBloggerClickListener implements View.OnClickListener {

        private final String mBlogApp;

        public OnBloggerClickListener(String blogApp) {
            mBlogApp = blogApp;
        }

        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(mBlogApp)) {
                AppRoute.jumpToBlogger(v.getContext(), mBlogApp);
            }
        }
    }

}
