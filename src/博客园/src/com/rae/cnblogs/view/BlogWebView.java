//package com.rae.cnblogs.view;
//
//import android.annotation.SuppressLint;
//import android.app.ActionBar;
//import android.app.Activity;
//import android.content.Context;
//import android.util.AttributeSet;
//import android.view.MotionEvent;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//
//import com.rae.cnblogs.i.BlogWebInterface;
//
//@SuppressLint("SetJavaScriptEnabled")
//public class BlogWebView extends WebView
//{
//	private WebSettings			mSettings;
//	private BlogWebInterface	mWebInterface;
//	
//	public BlogWebView(Context context, AttributeSet attrs)
//	{
//		super(context, attrs);
//		mSettings = getSettings();
//		setDefaultSetting();
//		//TODO:触屏翻页
//	}
//	
//	// 设置默认设置
//	private void setDefaultSetting()
//	{
//		mWebInterface = new BlogWebInterface(getContext()); // JavaScript交互脚本接口
//		mSettings.setJavaScriptEnabled(true); // 启用脚本
//		setWebChromeClient(new WebChromeClient());
//		setWebViewClient(new WebViewClient());
//		mSettings.setJavaScriptCanOpenWindowsAutomatically(true);
//		super.addJavascriptInterface(mWebInterface, "android");
//	}
//	
//	public void loadBlog(String blogId)
//	{
//		mWebInterface.setBlogID(blogId);
//		String url = "file:///android_asset/view.html";		// 加载博客页面:android_asset/view.html
//		super.loadUrl(url);
//	}
//	
//	private float	mStartX, mStartY;
//	
//	@Override
//	public boolean onTouchEvent(MotionEvent event)
//	{
//		int action = event.getAction();
//		Activity at = (Activity) getContext();
//		ActionBar bar = at.getActionBar();
//		switch (action)
//		{
//			case MotionEvent.ACTION_DOWN:
//				this.mStartX = event.getRawX();
//				this.mStartY = event.getRawY();
//				break;
//			case MotionEvent.ACTION_MOVE:
//				float y = event.getRawY();
//				int span = (int) (y - mStartY); // 间隔，
//				if (span > 0 && Math.abs(span) > 80)
//				{
//					// 隐藏导航条
//					bar.show();
//				}
//				else if (Math.abs(span) > 80)
//				{
//					at.getActionBar().hide();
//				}
//				break;
//			case MotionEvent.ACTION_UP:
//				float x = event.getRawX();
//				
//				int spanX = (int) (x - mStartX); // 间隔，
//				if (spanX > 300)
//				{
//					at.finish();
//				}
//				
//				break;
//			
//			default:
//				break;
//		}
//		
//		return super.onTouchEvent(event);
//	}
//	
//}
