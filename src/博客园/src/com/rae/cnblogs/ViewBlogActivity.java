//package com.rae.cnblogs;
//
//import android.app.Activity;
//import android.os.Bundle;
//
//import com.rae.cnblogs.view.BlogWebView;
//
//public class ViewBlogActivity extends Activity
//{
//	private BlogWebView	mBlogView;
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_view_blog);
//		mBlogView = (BlogWebView) findViewById(R.id.webview_blog);
//		
//		String blogId = getIntent().getExtras().getString("id");
//		
//		mBlogView.loadBlog(blogId);
//	}
//}
