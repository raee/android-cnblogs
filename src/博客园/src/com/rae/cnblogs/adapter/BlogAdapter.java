package com.rae.cnblogs.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.rae.cnblogs.R;
import com.rae.cnblogs.model.Blog;

public class BlogAdapter extends BaseListViewAdapter<Blog> implements OnItemClickListener
{
	public BlogAdapter(Context context, List<Blog> datalist)
	{
		super(context, datalist);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		Blog m = getDataItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_blog, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_blog_title);
			holder.tvSummary = (TextView) convertView.findViewById(R.id.tv_blog_summary);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tv_blog_date);
			holder.tvUser = (TextView) convertView.findViewById(R.id.tv_blog_user);
			holder.tvComment = (TextView) convertView.findViewById(R.id.tv_blog_comment);
			holder.tvView = (TextView) convertView.findViewById(R.id.tv_blog_view);
			convertView.setTag(holder);
		}
		
		holder = (ViewHolder) convertView.getTag();
		
		holder.tvTitle.setText(m.getTitle());
		holder.tvSummary.setText(m.getSummary());
		holder.tvDate.setText(m.getPostDate());
		holder.tvUser.setText(m.getAutor());
		holder.tvComment.setText(m.getCommentCount() + "");
		holder.tvView.setText(m.getViewCount() + "");
		
		return convertView;
	}
	
	/*
	 * 通知数据已经发生改变
	 */
	public void notifyChanged(List<Blog> datas)
	{
		this.mDataList = datas;
		this.notifyDataSetInvalidated();
		this.notifyDataSetChanged();
	}
	
	class ViewHolder
	{
		public TextView	tvTitle, tvSummary, tvDate, tvUser, tvComment, tvView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long arg3)
	{
		// 跳转到博客查看页面
	}
}
