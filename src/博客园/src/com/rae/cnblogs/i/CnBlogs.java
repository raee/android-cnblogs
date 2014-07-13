package com.rae.cnblogs.i;

import java.util.List;

import com.rae.cnblogs.http.HttpRequest;
import com.rae.cnblogs.model.Blog;
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
	private IBlogParser<Blog>	mBlogParser;
	
	public CnBlogs()
	{
		this.mBlogRequest = new HttpRequest<Blog>();
		this.mBlogParser = new CnBlogsParser();
	}
	
	@Override
	public void getHomeBlogs(BlogListener<Blog> listener, int index, int size)
	{
		mBlogRequest.get(ApiUrls.BLOG_SITE_HOME_PAGE, mBlogParser, listener, index, size);
	}
	
	@Override
	public void getBlogContent(BlogListener<Blog> listener, String id)
	{
		mBlogRequest.get(ApiUrls.BLOG_CONTENT, mBlogParser, listener, id);
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
	
}
