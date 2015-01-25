package com.rae.cnblogs;

import com.rae.cnblogs.util.ActionBarBuilder;
import com.rae.cnblogs.view.ActionBarView;

import android.app.Activity;
import android.os.Bundle;

public class BaseActivity extends Activity {

	private ActionBarView	mActionView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initActionBar();
	}

	private void initActionBar() {
		if (getActionBar() == null) {
			return;
		}
		ActionBarBuilder bar = new ActionBarBuilder(this, getActionBar());
		mActionView = bar.getActionBarView();
		bar.hideLogo();
	}

	@Override
	public void setTitle(CharSequence title) {
		if (mActionView != null) {
			mActionView.setTitle(title);
		}
		else {
			super.setTitle(title);
		}
	}

	@Override
	public void setTitle(int titleId) {
		if (mActionView != null) {
			mActionView.setTitle(titleId);
		}
		else {
			super.setTitle(titleId);
		}
	}

}
