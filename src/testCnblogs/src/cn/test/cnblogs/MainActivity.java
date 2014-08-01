package cn.test.cnblogs;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.CnBlogsOpenAPI;
import com.rae.cnblogs.sdk.data.DataProvider;
import com.rae.cnblogs.sdk.http.HttpCnBlogsOpenAPI;
import com.rae.cnblogs.sdk.model.Blog;

public class MainActivity extends Activity
{
	DataProvider	db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		db = new DataProvider(MainActivity.this);
		test();
	}
	
	private void test()
	{
		final CnBlogsOpenAPI sdk = new HttpCnBlogsOpenAPI(this);
		sdk.setOnCnBlogsLoadListener(new CnBlogsCallbackListener<Blog>()
		{
			
			@Override
			public void onLoadError(CnBlogsException e)
			{
				error(e.getMessage());
			}
			
			@Override
			public void onLoadBlogs(List<Blog> result)
			{
				for (Blog blog : result)
				{
					if (TextUtils.isEmpty(blog.getContent()))
					{
						db.addBlog(blog);
						sdk.getBlogContent(blog);
					}
					else
					{
						db.updateBlog(blog);
					}
				}
			}
		});
		sdk.getBlogs("", 0);
	}
	
	void log(Object obj)
	{
		obj = obj == null ? "" : obj;
		Log.i("cnblogtest", obj.toString());
	}
	
	void error(Object obj)
	{
		obj = obj == null ? "" : obj;
		Log.e("cnblogtest", obj.toString());
	}
	
}
