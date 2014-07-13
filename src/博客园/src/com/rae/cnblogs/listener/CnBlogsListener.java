package com.rae.cnblogs.listener;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.ListView;

import com.rae.cnblogs.adapter.BlogAdapter;
import com.rae.cnblogs.i.BlogException;
import com.rae.cnblogs.i.BlogListener;
import com.rae.cnblogs.model.Blog;
import com.rae.core.view.pulltorefresh.PullToRefreshBase;
import com.rae.core.view.pulltorefresh.PullToRefreshListView;

public class CnBlogsListener implements BlogListener<Blog>
{
	private BlogAdapter				mBlogAdapter;
	private PullToRefreshListView	mListView;
	private boolean					mIsAdded	= false;
	private List<Blog>				mDataList;
	
	public CnBlogsListener(Context context, PullToRefreshListView lv)
	{
		mDataList = new ArrayList<Blog>();
		mBlogAdapter = new BlogAdapter(context, mDataList);
		mListView = lv;
		lv.setAdapter(mBlogAdapter);
	}
	
	@Override
	public void onBlogSuccess(List<Blog> result)
	{
		if (mIsAdded)
		{
			mDataList.addAll(result);
		}
		else
		{
			mDataList = result;
		}
		mBlogAdapter.notifyChanged(mDataList);
		// 完成
		complete();
	}
	
	@Override
	public void onError(BlogException e)
	{
		complete();
	}
	
	public void isAdded(boolean val)
	{
		mIsAdded = val;
	}
	
	private void complete()
	{
		mListView.onRefreshComplete();
	}
	
}
