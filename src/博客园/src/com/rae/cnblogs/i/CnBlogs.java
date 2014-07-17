package com.rae.cnblogs.i;

import java.util.List;

import android.util.Log;

import com.rae.cnblogs.data.DataFactory;
import com.rae.cnblogs.http.HttpRequest;
import com.rae.cnblogs.listener.CnBlogsListener;
import com.rae.cnblogs.model.Blog;
import com.rae.cnblogs.parser.CnBlogContentParser;
import com.rae.cnblogs.parser.CnBlogsParser;
import com.rae.cnblogs.parser.IBlogParser;

/**
 * 博客园
 * 
 * @author admin
 * 
 */
class CnBlogs extends Blogs
{
	private HttpRequest<Blog>	mBlogRequest;
	private IBlogParser<Blog>	mBlogParser;		// 博客解析
	private IBlogParser<Blog>	mBlogContentParser; // 内容解析
													
	public CnBlogs()
	{
		this.mBlogRequest = new HttpRequest<Blog>();
		this.mBlogParser = new CnBlogsParser();
		this.mBlogContentParser = new CnBlogContentParser();
	}
	
	@Override
	public void getHomeBlogs(final BlogListener<Blog> listener, int index, int size)
	{
		mBlogRequest.get(ApiUrls.BLOG_SITE_HOME_PAGE, mBlogParser, new BlogListener<Blog>()
		{
			
			@Override
			public void onError(BlogException e)
			{
				listener.onError(e);
			}
			
			@Override
			public void onBlogSuccess(List<Blog> result)
			{
				// 加载博客
				//getBlogList(result, listener);
				listener.onBlogSuccess(result);
			}
		}, index, size);
	}
	
	@Override
	public void getBlogContent(BlogListener<Blog> listener, String id)
	{
		Log.i("blog", "获取博客内容：" + id);
		mBlogRequest.get(ApiUrls.BLOG_CONTENT, mBlogContentParser, listener, id);
	}
	
	@Override
	public List<Blog> get48HoursBlogs(BlogListener<Blog> listener)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void get10DaysBlogs(BlogListener<Blog> listener)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Blog> getRecommend(BlogListener<Blog> listener, int index, int size)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void searchUser(String name)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getComment(String blogId, int index, int size)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void getUserBlogs(BlogListener<Blog> listener, String userId, int index, int size)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public List<Blog> getBlogsByCategroy(BlogListener<Blog> listener, String categroyId, int index, int size)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	// 获取博客内容
	private void getBlogList(List<Blog> src, final BlogListener<Blog> l)
	{
		BlogContentListener listener = new BlogContentListener(l);
		
		for (Blog blog : src)
		{
			listener.setBlog(blog);
			getBlogContent(l, blog.getId());
		}
		l.onBlogSuccess(src);
	}
	
	// 获取博客内容回调处理
	private class BlogContentListener implements BlogListener<Blog>
	{
		private BlogListener<Blog>	listener;
		private Blog				source;
		
		public BlogContentListener(BlogListener<Blog> l)
		{
			this.listener = l;
		}
		
		public void setBlog(Blog src)
		{
			this.source = src;
		}
		
		@Override
		public void onError(BlogException e)
		{
			listener.onError(e);
		}
		
		@Override
		public void onBlogSuccess(List<Blog> result)
		{
			if (result.size() > 0 && result.get(0) != null)
			{
				source.setContent(result.get(0).getContent());
			}
		}
	}
	
}
