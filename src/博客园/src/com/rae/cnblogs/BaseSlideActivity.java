//package com.rae.cnblogs;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
//import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
//import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
//import com.rae.cnblogs.i.BlogFactory;
//import com.rae.cnblogs.i.AbsBlogOpenAPI;
//
///**
// * 支持滑动的基类
// * 
// * @author Chenrui
// * 
// */
//public class BaseSlideActivity extends SlidingFragmentActivity
//{
//	private AbsBlogOpenAPI	mBlogs; // 博客接口
//							
//	@Override
//	public void onCreate(Bundle savedInstanceState)
//	{
//		super.onCreate(savedInstanceState);
//	}
//	
//	@Override
//	public void setContentView(int id)
//	{
//		super.setContentView(id);
//		this.initMenu();
//	}
//	
//	// 初始菜单
//	protected void initMenu()
//	{
//		SlidingMenu menu = getSlidingMenu();
//		//				menu.setShadowDrawable(R.drawable.actionbar_bg); // 设置与主Activity之间的阴影
//		menu.setBehindOffset(160); //设置滑动与屏幕边界的距离
//		menu.setFadeDegree(0.35f);//设置渐入
//		menu.setAboveOffset(60);
//		menu.setMode(SlidingMenu.LEFT); // 设置滑动类型
//		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 菜单视图全屏
//		setBehindContentView(inflaterMenuView());
//	}
//	
//	// 初始化菜单布局文件
//	private View inflaterMenuView()
//	{
//		View menuView = getLayoutInflater().inflate(R.layout.view_slide_menu, null);
//		return menuView;
//	}
//	
//	/**
//	 * 获取博客园接口
//	 * 
//	 * @return
//	 */
//	public AbsBlogOpenAPI getCnBlogsApi()
//	{
//		if (mBlogs == null)
//		{
//			mBlogs = BlogFactory.getCnBlogsApi();
//		}
//		return mBlogs;
//	}
//	
//}
