package com.rae.cnblogs;

import android.os.Bundle;
import android.view.MenuItem;

import com.rae.cnblogs.view.BlogListView;

public class MainActivity extends BaseSlideActivity {
	private BlogListView mBlogListView;

	// private RefreshListView mListView;
	// private CnBlogsListener mListener;
	// private int mCurrentIndex = 1; // 当前页
	//
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayShowTitleEnabled(true);
		// ImageView menuView = new ImageView(this);
		// menuView.setImageResource(R.drawable.ic_menu);
		// LayoutParams layoutParam = new
		// LayoutParams(LayoutParams.WRAP_CONTENT,
		// LayoutParams.WRAP_CONTENT, Gravity.LEFT | Gravity.CENTER);
		// getActionBar().setCustomView(menuView, layoutParam);
		// getActionBar().setDisplayShowCustomEnabled(true);
		// getActionBar().setDisplayShowHomeEnabled(false);
		// getActionBar().setDisplayShowTitleEnabled(true);

		// CnBlogsOpenAPI api = new HttpCnBlogsOpenAPI(this);
		// api.setOnCnBlogsLoadListener(new CnBlogsListener()
		// {
		//
		// @Override
		// public void onLoadError(CnBlogsException e)
		// {
		// Toast.makeText(MainActivity.this, e.getMessage(),
		// Toast.LENGTH_LONG).show();
		// }
		//
		// @Override
		// public <T> void onLoadBlogs(List<T> data)
		// {
		// List<Blog> result = (List<Blog>) data;
		// for (Blog blog : result)
		// {
		// Log.i("cnblogs", blog.getTitle());
		// }
		// }
		// });
		// api.getBlogs(1);

		// initRefreshView();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			toggleMenu(); // 切换菜单
		}

		return super.onOptionsItemSelected(item);
	}

	//
	//
	// // 初始化顶部下拉刷新View
	// private void initRefreshView()
	// {
	// // 下拉刷新
	// mListView = (RefreshListView) findViewById(android.R.id.list);
	// mListener = new CnBlogsListener(this, mListView); // 初始化回调接口
	// mListView.setOnRefreshListener(new
	// PullToRefreshBase.OnRefreshListener2<ListView>()
	// {
	//
	// @Override
	// public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView)
	// {
	// getCnBlogsApi().getHomeBlogs(mListener, 1, 20);
	// }
	//
	// @Override
	// public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView)
	// {
	// mListener.isAdded(true);
	// mCurrentIndex++;
	// getCnBlogsApi().getHomeBlogs(mListener, mCurrentIndex, 20);
	//
	//
	// }
	// });
	// }

	@Override
	public BlogListView getBlogListView() {
		if (mBlogListView == null) {
			mBlogListView = (BlogListView) findViewById(R.id.blog_list_view);
		}
		return mBlogListView;
	}
}
