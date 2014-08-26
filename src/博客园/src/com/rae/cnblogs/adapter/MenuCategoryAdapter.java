package com.rae.cnblogs.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rae.cnblogs.R;
import com.rae.cnblogs.sdk.model.Category;
import com.rae.cnblogs.view.BlogListView;

/**
 * 菜单分类
 * 
 * @author ChenRui
 * 
 */
public class MenuCategoryAdapter extends BaseListViewAdapter<Category> implements OnItemClickListener
{
	private BlogListView	mBlogListView;
	
	public MenuCategoryAdapter(Context context, List<Category> datalist)
	{
		super(context, datalist);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder = null;
		Category model = getDataItem(position);
		if (convertView == null)
		{
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_menu_category, null);
			holder = new ViewHolder();
			holder.tvName = (TextView) convertView.findViewById(R.id.tv_category_name);
			holder.tvName.setTag(model);
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.tvName.setText(model.getName());
		return convertView;
	}
	
	class ViewHolder
	{
		public TextView	tvName;
	}
	
	public void setOnItemClickListener(SlidingMenu menu, ListView lv, BlogListView blogView)
	{
		if (blogView == null) return;
		menu.toggle(true);
		lv.setOnItemClickListener(this);
		this.mBlogListView = blogView;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3)
	{
		Category model = getDataItem(position);
		String cateId = "cate/" + model.getId();
		mBlogListView.load(cateId);	// 获取数据，刷新ListView
	}
	
}
