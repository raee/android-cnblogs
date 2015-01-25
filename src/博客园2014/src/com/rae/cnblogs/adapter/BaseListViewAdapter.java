package com.rae.cnblogs.adapter;

import java.util.List;

import android.content.Context;
import android.widget.BaseAdapter;

public abstract class BaseListViewAdapter<T> extends BaseAdapter
{
	protected List<T>	mDataList;
	protected Context	mContext;
	
	public BaseListViewAdapter(Context context, List<T> datalist)
	{
		this.mContext = context;
		this.mDataList = datalist;
	}
	
	@Override
	public int getCount()
	{
		return mDataList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return mDataList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	public T getDataItem(int position)
	{
		return this.mDataList.get(position);
	}
	
}
