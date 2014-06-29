package cn.test.cnblogs;

import java.util.List;

import com.rae.cnblogs.BlogException;
import com.rae.cnblogs.BlogFactory;
import com.rae.cnblogs.BlogListener;
import com.rae.cnblogs.BlogUrlApi;
import com.rae.cnblogs.Blogs;
import com.rae.cnblogs.http.HttpRequest;
import com.rae.cnblogs.model.Blog;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements BlogListener
{
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		testGetBlogs();
	}
	
	public void testConvertUrl()
	{
		String resultString = new HttpRequest().convertUrl(
				BlogUrlApi.BLOG_COMMENETS_PAGE, "abc123", 1, 2);
		log(resultString);
	}
	
	public void testGetBlogs()
	{
		Blogs blogs = BlogFactory.getFactory();
		blogs.setBlogListener(this);
		blogs.getHomeBlogs(1, 10);
	}
	
	public void log(Object msg)
	{
		Log.i("cnblogs", (msg == null ? "日志空" : msg.toString()));
	}
	
	@Override
	public void onBlogSuccess(List<Blog> result)
	{
		for (Blog blog : result)
		{
			log("标题：" + blog.getTitle());
		}
	}
	
	@Override
	public void onError(BlogException e)
	{
		e.printStackTrace();
	}
	
}
