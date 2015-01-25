// package com.rae.cnblogs.adapter;
//
// import java.util.List;
//
// import android.app.Activity;
// import android.os.Bundle;
// import android.view.LayoutInflater;
// import android.view.View;
// import android.view.ViewGroup;
// import android.widget.ListView;
// import android.widget.TextView;
// import android.widget.Toast;
//
// import com.rae.cnblogs.R;
// import com.rae.cnblogs.R.drawable;
// import com.rae.cnblogs.R.id;
// import com.rae.cnblogs.R.layout;
// import com.rae.cnblogs.i.BlogException;
// import com.rae.cnblogs.i.BlogFactory;
// import com.rae.cnblogs.i.BlogListener;
// import com.rae.cnblogs.i.Blogs;
// import com.rae.cnblogs.model.Blog;
// import com.rae.core.view.pulltorefresh.ILoadingLayout;
// import com.rae.core.view.pulltorefresh.PullToRefreshBase;
// import com.rae.core.view.pulltorefresh.PullToRefreshBase.Mode;
// import com.rae.core.view.pulltorefresh.PullToRefreshBase.OnPullEventListener;
// import com.rae.core.view.pulltorefresh.PullToRefreshBase.OnRefreshListener;
// import com.rae.core.view.pulltorefresh.PullToRefreshBase.State;
// import com.rae.core.view.pulltorefresh.PullToRefreshListView;
//
// public class MainActivity extends Activity implements BlogListener
// {
//
// private PullToRefreshListView mListView;
//
// @Override
// protected void onCreate(Bundle savedInstanceState)
// {
// super.onCreate(savedInstanceState);
// setContentView(R.layout.activity_main);
//
// // 下拉刷新
// mListView = (PullToRefreshListView) findViewById(android.R.id.list);
// mListView.setMode(Mode.BOTH);
//
// ILoadingLayout layout = mListView.getLoadingLayoutProxy(true, true);
// layout.setLastUpdatedLabel("");
// layout.setLoadingDrawable(getResources().getDrawable(R.drawable.ic_launcher));
// layout.setPullLabel("");
// layout.setRefreshingLabel("");
// layout.setReleaseLabel("");
//
// mListView.setOnPullEventListener(new OnPullEventListener<ListView>()
// {
//
// @Override
// public void onPullEvent(PullToRefreshBase<ListView> refreshView,
// State state, Mode direction)
// {
//
// }
// });
//
// mListView
// .setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>()
// {
//
// @Override
// public void onPullDownToRefresh(
// PullToRefreshBase<ListView> refreshView)
// {
// Blogs blog = BlogFactory.getFactory();
// blog.setBlogListener(MainActivity.this);
// blog.getHomeBlogs(2, 20);
// }
//
// @Override
// public void onPullUpToRefresh(
// PullToRefreshBase<ListView> refreshView)
// {
// Blogs blog = BlogFactory.getFactory();
// blog.setBlogListener(MainActivity.this);
// blog.getHomeBlogs(1, 20);
// }
// });
// }
//
// @Override
// public void onBlogSuccess(List<Blog> result)
// {
// mListView.setAdapter(new BlogListAdapter(result));
// mListView.onRefreshComplete();
// }
//
// @Override
// public void onError(BlogException e)
// {
// Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
// mListView.onRefreshComplete();
// }
//
// private class BlogListAdapter extends BaseListViewAdapter<Blog>
// {
//
// public BlogListAdapter(List<Blog> datalist)
// {
// super(datalist);
// }
//
// @Override
// public View getView(int position, View convertView, ViewGroup parent)
// {
// ViewHolder holder = null;
// Blog m = getDataItem(position);
// if (convertView == null)
// {
// convertView = LayoutInflater.from(MainActivity.this).inflate(
// R.layout.item_blog, null);
// holder = new ViewHolder();
// holder.tvTitle = (TextView) convertView
// .findViewById(R.id.tv_blog_title);
// holder.tvSummary = (TextView) convertView
// .findViewById(R.id.tv_blog_summary);
// holder.tvDate = (TextView) convertView
// .findViewById(R.id.tv_blog_date);
// holder.tvUser = (TextView) convertView
// .findViewById(R.id.tv_blog_user);
// holder.tvComment = (TextView) convertView
// .findViewById(R.id.tv_blog_comment);
// holder.tvView = (TextView) convertView
// .findViewById(R.id.tv_blog_view);
// convertView.setTag(holder);
// }
//
// holder = (ViewHolder) convertView.getTag();
//
// holder.tvTitle.setText(m.getTitle());
// holder.tvSummary.setText(m.getSummary());
// holder.tvDate.setText(m.getPostDate());
// holder.tvUser.setText(m.getAutor());
// holder.tvComment.setText(m.getCommentCount() + "");
// holder.tvView.setText(m.getViewCount() + "");
//
// return convertView;
// }
//
// }
//
// class ViewHolder
// {
// public TextView tvTitle, tvSummary, tvDate, tvUser, tvComment, tvView;
// }
//
//}
