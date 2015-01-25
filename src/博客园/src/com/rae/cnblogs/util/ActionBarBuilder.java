package com.rae.cnblogs.util;

import com.rae.cnblogs.view.ActionBarView;

import android.app.ActionBar;
import android.content.Context;
import android.view.View;

/**
 * 导航栏帮助类
 * 
 * @author ChenRui
 * 
 */
public class ActionBarBuilder {
	private ActionBar		mActionBar;
	private ActionBarView	mActionView;

	public ActionBarBuilder(Context context, ActionBar actionBar) {
		this.mActionBar = actionBar;
		this.mActionView = new ActionBarView(context, null);
		setCustomerView(mActionView).hideTitle();

	}

	public ActionBarBuilder setCustomerView(View view) {
		this.mActionBar.setDisplayShowCustomEnabled(true);
		this.mActionBar.setCustomView(view);
		return this;
	}

	public ActionBarView getActionBarView() {
		return this.mActionView;
	}

	public ActionBarBuilder hideLogo() {
		mActionBar.setDisplayShowHomeEnabled(false);
		return this;
	}

	public ActionBarBuilder hideTitle() {
		mActionBar.setDisplayShowTitleEnabled(false);
		return this;
	}

	public ActionBarBuilder showUp() {
		mActionBar.setDisplayHomeAsUpEnabled(true);
		return this;
	}

	public void show() {
		mActionBar.show();
	}

	public void hide() {
		mActionBar.hide();
	}
}
