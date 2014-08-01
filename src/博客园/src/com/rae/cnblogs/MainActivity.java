package com.rae.cnblogs;

import java.util.List;

import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.CnBlogsListener;
import com.rae.cnblogs.sdk.CnBlogsOpenAPI;
import com.rae.cnblogs.sdk.http.HttpCnBlogsOpenAPI;
import com.rae.cnblogs.sdk.model.Blog;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends Activity
{
	//	private RefreshListView	mListView;
	//	private CnBlogsListener	mListener;
	//	private int				mCurrentIndex	= 1;	// 当前页
	//													
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		CnBlogsOpenAPI api = new HttpCnBlogsOpenAPI(this);
		api.setOnCnBlogsLoadListener(new CnBlogsListener()
		{
			
			@Override
			public void onLoadError(CnBlogsException e)
			{
				Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
			}
			
			@Override
			public <T> void onLoadBlogs(List<T> data)
			{
				List<Blog> result = (List<Blog>) data;
				for (Blog blog : result)
				{
					Log.i("cnblogs", blog.getTitle());
				}
			}
		});
		api.getBlogs(1);
		
		//		initRefreshView();
	}
	//	
	//	
	//	// 初始化顶部下拉刷新View
	//	private void initRefreshView()
	//	{
	//		// 下拉刷新
	//		mListView = (RefreshListView) findViewById(android.R.id.list);
	//		mListener = new CnBlogsListener(this, mListView); // 初始化回调接口
	//		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
	//		{
	//			
	//			@Override
	//			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
	//			{
	//				getCnBlogsApi().getHomeBlogs(mListener, 1, 20);
	//			}
	//			
	//			@Override
	//			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
	//			{
	//				mListener.isAdded(true);
	//				mCurrentIndex++;
	//				getCnBlogsApi().getHomeBlogs(mListener, mCurrentIndex, 20);
	//				
	//				
	//			}
	//		});
	//	}
}
