package com.rae.cnblogs;

import android.app.Activity;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.rae.cnblogs.view.BlogListView;
import com.rae.cnblogs.view.SlideMenuView;

/**
 * 支持滑动的基类
 * 
 * @author Chenrui
 * 
 */
public abstract class BaseSlideActivity extends Activity {

	private SlidingMenu mSlideMenu;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSlideMenu = new SlidingMenu(this);
	}

	@Override
	public void setContentView(int id) {
		super.setContentView(id);
		this.initMenu();
	}

	// 初始菜单
	protected void initMenu() {
		SlidingMenu menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT_RIGHT);
		int width = AndroidUtils.getDisplayWidth(this);
		menu.setBehindOffset((int) (width * 0.35)); // 设置滑动与屏幕边界的距离
		menu.setFadeDegree(1f);// 设置渐入
		menu.setBehindScrollScale(0.5f); // 设置缩放
		menu.setShadowDrawable(R.drawable.menu_shade);
		menu.setShadowWidth(60);

		menu.setAboveOffset((int) (width * 0.35));

		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN); // 菜单视图全屏

		SlideMenuView menuView = new SlideMenuView(this, getBlogListView());
		// menu.setContent(menuViegw);
		menu.attachToActivity(this, SlidingMenu.SLIDING_WINDOW);
		menu.setMenu(menuView);
		menu.setSecondaryMenu(new SlideMenuView(this, getBlogListView()));
		// setBehindContentView();

	}

	private SlidingMenu getSlidingMenu() {

		return mSlideMenu;
	}

	protected abstract BlogListView getBlogListView();

	public void toggleMenu() {
		getSlidingMenu().toggle(true);
	}

	@Override
	public void setTitle(CharSequence title) {
		getActionBar().setTitle(title);
	}
}
