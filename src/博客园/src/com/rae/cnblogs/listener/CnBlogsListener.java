//package com.rae.cnblogs.listener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import android.content.Context;
//import android.widget.Toast;
//
//import com.rae.cnblogs.adapter.BlogAdapter;
//import com.rae.cnblogs.data.DataFactory;
//import com.rae.cnblogs.i.BlogException;
//import com.rae.cnblogs.i.BlogListener;
//import com.rae.cnblogs.model.Blog;
//import com.rae.core.view.pulltorefresh.PullToRefreshListView;
//
//public class CnBlogsListener implements BlogListener<Blog>
//{
//	private BlogAdapter				mBlogAdapter;
//	private PullToRefreshListView	mListView;
//	private boolean					mIsAdded	= false;
//	private List<Blog>				mDataList;
//	private Context					mContext;
//	
//	public CnBlogsListener(Context context, PullToRefreshListView lv)
//	{
//		this.mContext = context;
//		mDataList = new ArrayList<Blog>();
//		mBlogAdapter = new BlogAdapter(context, mDataList);
//		mListView = lv;
//		lv.setAdapter(mBlogAdapter);
//		lv.setOnItemClickListener(mBlogAdapter);
//	}
//	
//	@Override
//	public void onBlogSuccess(List<Blog> result)
//	{
//		if (mIsAdded)
//		{
//			mDataList.addAll(result);
//		}
//		else
//		{
//			mDataList = result;
//		}
//		mBlogAdapter.notifyChanged(mDataList);
//		DataFactory.getDataProvider(this.mContext).addBlogs(result);
//		// 完成
//		complete();
//	}
//	
//	@Override
//	public void onError(BlogException e)
//	{
//		complete();
//	}
//	
//	public void isAdded(boolean val)
//	{
//		mIsAdded = val;
//	}
//	
//	private void complete()
//	{
//		mListView.onRefreshComplete();
//		Toast.makeText(this.mContext, "数据加载成功！", Toast.LENGTH_SHORT).show();
//	}
//}
