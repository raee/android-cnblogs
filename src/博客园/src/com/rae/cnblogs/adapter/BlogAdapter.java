package com.rae.cnblogs.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.R;

public class BlogAdapter extends ListViewAdapter<Blog> {

	public BlogAdapter(Context context) {
		super(context);
	}

	@Override
	public Object getViewHolder(View view) {
		BlogViewHolder holder = new BlogViewHolder();
		holder.headerImageView = (ImageView) view.findViewById(R.id.img_blog_header);
		holder.titleTextView = (TextView) view.findViewById(R.id.tv_blog_title);
		holder.summaryTextView = (TextView) view.findViewById(R.id.tv_blog_summry);
		return holder;
	}

	@Override
	public View getConvertView(LayoutInflater inflater) {
		return inflater.inflate(R.layout.item_blog, null);
	}

	@Override
	public void showView(Blog m, Object viewHolder) {
		BlogViewHolder holder = (BlogViewHolder) viewHolder;
		holder.titleTextView.setText(m.getTitle());
		holder.summaryTextView.setText(m.getSummary());
	}

	class BlogViewHolder {
		ImageView	headerImageView;
		TextView	titleTextView;
		TextView	summaryTextView;
	}

}
