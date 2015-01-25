package com.rae.cnblogs;

import java.util.List;

import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.CnBlogsOpenAPI;
import com.rae.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.view.WebBlogView;
import com.rae.view.slideback.lib.SwipeBackLayout;
import com.rae.view.slideback.lib.app.SwipeBackActivity;

public class ViewBlogActivity extends SwipeBackActivity implements CnBlogsCallbackListener<Blog>
{
	private SwipeBackLayout	mSwipeBackLayout;
	private ImageView		mImgLoading;
	private View			mLoadingLayout;
	private CnBlogsOpenAPI	sdk;
	private WebBlogView		mWebView;
	private int				mWidth, mHeight;	// 屏幕的宽高
			
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_blog);
		mWebView = (WebBlogView) findViewById(R.id.blog_view);
		mImgLoading = (ImageView) findViewById(R.id.img_view_blog_loading);
		mLoadingLayout = findViewById(R.id.fl_loading_layout);
		showLoading();
		sdk = CnBlogsOpenAPI.getInstance(this);
		loadBlogContent();
		
		// 获取屏幕的宽高
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		this.mWidth = outMetrics.widthPixels;
		this.mHeight = outMetrics.heightPixels;
		Log.i("cnblogs", mWidth + "*" + mHeight);
		
		mSwipeBackLayout = getSwipeBackLayout();
		mSwipeBackLayout.setScrimColor(Color.TRANSPARENT);
		mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);
	}
	
	//加载博客
	private void loadBlogContent()
	{
		String blogId = getIntent().getStringExtra("id");
		sdk.getBlogContent(this, blogId);
	}
	
	private void showLoading()
	{
		mLoadingLayout.setVisibility(View.VISIBLE);
		((AnimationDrawable) mImgLoading.getDrawable()).start(); //开始动画
	}
	
	private void hideLoading()
	{
		mLoadingLayout.setVisibility(View.GONE);
		((AnimationDrawable) mImgLoading.getDrawable()).stop(); //停止动画
	}
	
	@Override
	public void onLoadError(CnBlogsException e)
	{
		hideLoading();
		Log.e("cnblogs", e.getMessage());
	}
	
	@Override
	public void onLoadBlogs(List<Blog> result)
	{
		if (result.size() <= 0)
		{
			onLoadError(new CnBlogsException("获取博客失败！"));
			return;
		}
		// 加载webView
		hideLoading();
		mWebView.load(result.get(0));
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		if (event.getAction() == MotionEvent.ACTION_DOWN)
		{
			
		}
		return super.onTouchEvent(event);
	}
	
	@Override
	public void onBackPressed()
	{
		scrollToFinishActivity();
	}
	
}
