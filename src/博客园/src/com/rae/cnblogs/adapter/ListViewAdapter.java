package com.rae.cnblogs.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class ListViewAdapter<T> extends BaseAdapter {
	private List<T>	mList;
	private Context	mContext;

	public ListViewAdapter(Context context) {
		this.mContext = context;
	}

	public List<T> getDataList() {
		return mList;
	}

	protected Context getContext() {
		return mContext;
	}

	public void setDataList(List<T> list) {
		mList = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		}
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = getConvertView(LayoutInflater.from(mContext));
			convertView.setTag(getViewHolder(convertView));
		}

		showView(mList.get(position), convertView.getTag());
		return convertView;
	}

	public abstract View getConvertView(LayoutInflater inflater);

	public abstract void showView(T m, Object viewHolder);

	public Object getViewHolder(View view) {
		// TODO Auto-generated method stub
		return null;
	}

}
