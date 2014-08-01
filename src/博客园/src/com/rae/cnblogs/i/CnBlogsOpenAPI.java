//package com.rae.cnblogs.i;
//
//import java.util.List;
//
//import android.util.Log;
//
//import com.rae.cnblogs.http.HttpRequest;
//import com.rae.cnblogs.model.Blog;
//import com.rae.cnblogs.parser.CnBlogContentParser;
//import com.rae.cnblogs.parser.CnBlogsParser;
//import com.rae.cnblogs.parser.IBlogParser;
//
///**
// * 博客园
// * 
// * @author admin
// * 
// */
//class CnBlogsOpenAPI extends AbsBlogOpenAPI
//{
//	private HttpRequest<Blog>	mBlogRequest;
//	private IBlogParser<Blog>	mBlogParser;		// 博客解析
//	private IBlogParser<Blog>	mBlogContentParser; // 内容解析
//													
//	public CnBlogsOpenAPI()
//	{
//		this.mBlogRequest = new HttpRequest<Blog>();
//		this.mBlogParser = new CnBlogsParser();
//		this.mBlogContentParser = new CnBlogContentParser();
//	}
//	
//	@Override
//	public void getHomeBlogs(final BlogListener listener, int index, int size)
//	{
//		mBlogRequest.get(Constant.BLOG_SITE_HOME_PAGE, mBlogParser, new BlogListener()
//		{
//			
//			@Override
//			public void onError(BlogException e)
//			{
//				listener.onError(e);
//			}
//			
//			@Override
//			public void onBlogSuccess(List<Blog> result)
//			{
//				// 加载博客
//				//getBlogList(result, listener);
//				listener.onBlogSuccess(result);
//			}
//		}, index, size);
//	}
//	
//	@Override
//	public void getBlogContent(BlogListener listener, String id)
//	{
//		Log.i("blog", "获取博客内容：" + id);
//		mBlogRequest.get(Constant.BLOG_CONTENT, mBlogContentParser, listener, id);
//	}
//	
//	@Override
//	public List<Blog> get48HoursBlogs(BlogListener listener)
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public void get10DaysBlogs(BlogListener listener)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public List<Blog> getRecommend(BlogListener listener, int index, int size)
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	@Override
//	public void searchUser(String name)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public void getComment(String blogId, int index, int size)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public void getUserBlogs(BlogListener listener, String userId, int index, int size)
//	{
//		// TODO Auto-generated method stub
//		
//	}
//	
//	@Override
//	public List<Blog> getBlogsByCategroy(BlogListener listener, String categroyId, int index, int size)
//	{
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	// 获取博客内容
//	private void getBlogList(List<Blog> src, final BlogListener l)
//	{
//		BlogContentListener listener = new BlogContentListener(l);
//		
//		for (Blog blog : src)
//		{
//			listener.setBlog(blog);
//			getBlogContent(l, blog.getId());
//		}
//		l.onBlogSuccess(src);
//	}
//	
//	// 获取博客内容回调处理
//	private class BlogContentListener implements BlogListener
//	{
//		private BlogListener	listener;
//		private Blog				source;
//		
//		public BlogContentListener(BlogListener l)
//		{
//			this.listener = l;
//		}
//		
//		public void setBlog(Blog src)
//		{
//			this.source = src;
//		}
//		
//		@Override
//		public void onError(BlogException e)
//		{
//			listener.onError(e);
//		}
//		
//		@Override
//		public void onBlogSuccess(List<Blog> result)
//		{
//			if (result.size() > 0 && result.get(0) != null)
//			{
//				source.setContent(result.get(0).getContent());
//			}
//		}
//	}
//	
//}
