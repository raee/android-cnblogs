package com.rae.cnblogs.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.rae.cnblogs.view.DragLayout.Status;

public class ContentRelativeLayout extends RelativeLayout {
	private DragLayout	dl;

	public ContentRelativeLayout(Context context) {
		super(context);
	}

	public ContentRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ContentRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setDragLayout(DragLayout dl) {
		this.dl = dl;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (dl.getStatus() != Status.Close) {
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (dl.getStatus() != Status.Close) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				dl.close();
			}
			return true;
		}
		return super.onTouchEvent(event);
	}

}
