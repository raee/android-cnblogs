package com.rae.cnblogs.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.rae.cnblogs.R;
import com.rae.cnblogs.adapter.BlogAdapter;
import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.CnBlogsOpenAPI;
import com.rae.cnblogs.sdk.model.Blog;

/**
 * 博客视图
 * 
 * @author ChenRui
 * 
 */
public class BlogListView extends ListView implements CnBlogsCallbackListener<Blog>, OnScrollListener
{
	private CnBlogsOpenAPI					sdk;
	private BlogAdapter						mAdapter;
	private List<Blog>						datalist;
	private CnBlogsCallbackListener<Blog>	mListener;
	private int								mCurrentIndex		= 1;		// 当前页码
	private String							mCurrentCategory	= "";		// 当前分类
	private int								mLastItemIndex;				//当前ListView中最后一个Item的索引  
	private View							mLoadingFooterView;
	private ImageView						mImageRefresh;
	private boolean							mIsLoading			= false;
	
	public BlogListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		
		// 添加底部加载更多视图
		mLoadingFooterView = LayoutInflater.from(context).inflate(R.layout.item_blog_footer_view, null);
		mImageRefresh = (ImageView) mLoadingFooterView.findViewById(R.id.img_item_load_more);
		addFooterView(mLoadingFooterView);
		showLoadMoreView();
		
		// 初始化字段
		sdk = CnBlogsOpenAPI.getInstance(context);
		datalist = new ArrayList<Blog>();
		mAdapter = new BlogAdapter(this, context, datalist);
		
		this.setOnScrollListener(this);
		mAdapter.init();
		load(""); // 加载数据
		
	}
	
	@Override
	public void onLoadError(CnBlogsException e)
	{
		mIsLoading = false;
		Toast.makeText(getContext(), "加载数据错误，" + e.getMessage(), Toast.LENGTH_SHORT).show();
		if (mListener != null) mListener.onLoadError(e);
		hideLoadMoreView();
	}
	
	@Override
	public void onLoadBlogs(List<Blog> result)
	{
		mIsLoading = false;
		mCurrentIndex++;
		mAdapter.notifyChanged(result);
		hideLoadMoreView();
		if (mListener != null) mListener.onLoadBlogs(result);
	}
	
	/**
	 * 返回顶部
	 */
	public void goTop()
	{
		Log.i("cnblogs", "返回顶部！");
		setSelection(0);
	}
	
	public void load(String cateId)
	{
		if (mIsLoading) // 防止操作过快
		{
			Log.w("cnblogs", "已经加载.." + mCurrentIndex);
			return;
		}
		if (!mCurrentCategory.equals(cateId))
		{
			mCurrentIndex = 1;
		}
		mCurrentIndex = mCurrentIndex <= 0 ? 1 : mCurrentIndex; // 保证页码不为负数
		mCurrentCategory = cateId;
		sdk.getCnblogs(this, cateId, null, mCurrentIndex);
		mIsLoading = true;
	}
	
	public void setOnCnBlogsCallbackListener(CnBlogsCallbackListener<Blog> l)
	{
		mListener = l;
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		mLastItemIndex = firstVisibleItem + visibleItemCount - 1;  //ListView 的FooterView也会算到visibleItemCount中去，所以要再减去一  
	}
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		int itemCount = view.getAdapter().getCount() - 1; // 触发的总数
		if (scrollState == SCROLL_STATE_IDLE && mLastItemIndex == itemCount)
		{
			load(mCurrentCategory);
			showLoadMoreView();
		}
	}
	
	private void showLoadMoreView()
	{
		mLoadingFooterView.setVisibility(View.VISIBLE);
		mImageRefresh.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.rotate_refresh));
		
	}
	
	private void hideLoadMoreView()
	{
		mLoadingFooterView.setVisibility(View.GONE);
		mImageRefresh.clearAnimation();
	}
}
