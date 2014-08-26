package com.rae.cnblogs;

import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.rae.cnblogs.view.SlideMenuView;

/**
 * 支持滑动的基类
 * 
 * @author Chenrui
 * 
 */
public abstract class BaseSlideActivity extends SlidingFragmentActivity
{
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void setContentView(int id)
	{
		super.setContentView(id);
		this.initMenu();
	}
	
	// 初始菜单
	protected void initMenu()
	{
		SlidingMenu menu = getSlidingMenu();
		int width = AndroidUtils.getDisplayWidth(this);
		menu.setBehindOffset((int) (width * 0.2)); //设置滑动与屏幕边界的距离
		menu.setFadeDegree(1f);//设置渐入
		menu.setShadowDrawable(R.drawable.menu_shade);
		menu.setShadowWidth(60);
		
		menu.setMode(SlidingMenu.LEFT); // 设置滑动类型
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 菜单视图全屏
		setBehindContentView(new SlideMenuView(this));
	}
}
