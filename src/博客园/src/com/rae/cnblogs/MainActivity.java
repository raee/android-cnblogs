package com.rae.cnblogs;

import android.os.Bundle;
import android.widget.ListView;

import com.rae.cnblogs.listener.CnBlogsListener;
import com.rae.cnblogs.view.RefreshListView;
import com.rae.core.view.pulltorefresh.ILoadingLayout;
import com.rae.core.view.pulltorefresh.PullToRefreshBase;
import com.rae.core.view.pulltorefresh.PullToRefreshBase.Mode;
import com.rae.core.view.pulltorefresh.PullToRefreshBase.Orientation;
import com.rae.core.view.pulltorefresh.internal.FlipLoadingLayout;

public class MainActivity extends BaseSlideActivity
{
	private RefreshListView	mListView;
	private CnBlogsListener	mListener;
	private int				mCurrentIndex	= 1;	// 当前页
													
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initRefreshView();
	}
	
	
	// 初始化顶部下拉刷新View
	private void initRefreshView()
	{
		// 下拉刷新
		mListView = (RefreshListView) findViewById(android.R.id.list);
		mListView.setMode(Mode.BOTH);
		
//		ILoadingLayout layout = mListView.getLoadingLayoutProxy(true, true);
//		layout = new FlipLoadingLayout(this, Mode.BOTH, Orientation.HORIZONTAL, null);
//		layout.setLastUpdatedLabel("");
//		layout.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_launcher));
//		layout.setPullLabel("");
//		layout.setRefreshingLabel("");
//		layout.setReleaseLabel("");
		
		mListener = new CnBlogsListener(this, mListView); // 初始化回调接口
		
		
		mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
		{
			
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				getCnBlogsApi().getHomeBlogs(mListener, 1, 20);
			}
			
			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
			{
				mListener.isAdded(true);
				mCurrentIndex++;
				getCnBlogsApi().getHomeBlogs(mListener, mCurrentIndex, 20);
			}
		});
	}
}
