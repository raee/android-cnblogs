package com.rae.cnblogs;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;
import com.rae.cnblogs.i.BlogFactory;
import com.rae.cnblogs.i.Blogs;

/**
 * 支持滑动的基类
 * 
 * @author Chenrui
 * 
 */
public class BaseSlideActivity extends Activity
{
	private Blogs	mBlogs; // 博客接口
							
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void setContentView(int id)
	{
		super.setContentView(id);
		initMenu();
	}
	
	// 初始菜单
	protected void initMenu()
	{
		//		SlidingMenu menu = getSlidingMenu();
		//		menu.setMode(SlidingMenu.LEFT); // 设置滑动类型
		//		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 菜单视图全屏
		//		//		menu.setShadowDrawable(R.drawabl.); // 设置与主Activity之间的阴影
		//		menu.setBehindOffset(160); //设置滑动与屏幕边界的距离
		//		menu.setFadeDegree(0.35f);//设置渐入
		//		menu.setMenu(inflaterMenuView());
	}
	
	// 初始化菜单布局文件
	private View inflaterMenuView()
	{
		View menuView = getLayoutInflater().inflate(R.layout.activity_main, null);
		return menuView;
	}
	
	/**
	 * 获取博客园接口
	 * 
	 * @return
	 */
	public Blogs getCnBlogsApi()
	{
		if (mBlogs == null)
		{
			mBlogs = BlogFactory.getCnBlogsApi();
		}
		return mBlogs;
	}
	
}
