package com.rae.cnblogs.view;

import com.rae.cnblogs.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ActionBarView extends LinearLayout {

	private TextView	mTitleTextView;

	public ActionBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		LayoutInflater.from(context).inflate(R.layout.view_actionbar, this);
		mTitleTextView = (TextView) findViewById(R.id.tv_ab_title);
	}

	public void setTitle(CharSequence title) {
		mTitleTextView.setText(title);
	}

	public void setTitle(int resId) {
		mTitleTextView.setText(resId);
	}

	public void setOnClickListener(int id, View.OnClickListener l) {
		findViewById(id).setOnClickListener(l);
	}

}
