package com.rae.cnblogs;

import java.util.List;

import com.cnblogs.sdk.CnblogJsonFactory;
import com.cnblogs.sdk.CnblogUiError;
import com.cnblogs.sdk.CnblogUiListener;
import com.cnblogs.sdk.Cnblogs;
import com.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.adapter.BlogAdapter;
import com.rae.cnblogs.view.ActionBarView;
import com.rae.cnblogs.view.DragLayout;
import com.rae.cnblogs.view.DragLayout.DragListener;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;

public class HomeActivity extends BaseActivity implements OnClickListener {
	private ActionBarView	mActionView;
	private DragLayout		mDragLayout;
	protected String		tag	= "HomeActivity";
	private ListView		mBlogListView;
	private BlogAdapter		mBlogAdapter;
	private Cnblogs			sdk;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		this.mActionView = (ActionBarView) findViewById(R.id.actionbar_viiew);
		this.mBlogListView = (ListView) findViewById(R.id.list_blog);
		this.mBlogAdapter = new BlogAdapter(this);
		this.mBlogListView.setAdapter(mBlogAdapter);

		setTitle("首页");

		mDragLayout = (DragLayout) findViewById(R.id.dl);
		mDragLayout.setDragListener(new DragListener() {

			@Override
			public void onOpen() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onDrag(float percent) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClose() {
				// TODO Auto-generated method stub

			}
		});

		// 按钮
		mActionView.setOnClickListener(R.id.logo, this);

		sdk = new Cnblogs(this);

		sdk.setCnblogUiListener(new CnblogUiListener() {

			@Override
			public void onSuccess(String json) {
				List<Blog> list = CnblogJsonFactory.parserBlogs(json);
				mBlogAdapter.setDataList(list);
			}

			@Override
			public void onLoadding(int progress, int totalSize) {
				Log.i(tag, "正在加载博客.." + progress);
			}

			@Override
			public void onError(CnblogUiError error) {
				Log.e(tag, error.toString());
			}
		});

		initBlogList();
	}

	@Override
	public void setTitle(CharSequence title) {
		mActionView.setTitle(title);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.logo:
				mDragLayout.open(true);
				break;

			default:
				break;
		}
	}

	/**
	 * 初始化博客列表
	 */
	private void initBlogList() {
		sdk.getBlogs(1);
	}

}
