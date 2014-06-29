package com.rae.cnblogs;

import java.util.List;

import com.rae.cnblogs.http.HttpRequest;
import com.rae.cnblogs.model.Blog;
import com.rae.cnblogs.parser.BlogParser;
import com.rae.cnblogs.parser.IBlogParser;

/**
 * 博客园
 * 
 * @author admin
 * 
 */
class CnBlogs extends Blogs
{
	private HttpRequest	mRequest;
	private IBlogParser	mBlogParser;
	
	public CnBlogs()
	{
		mRequest = new HttpRequest();
		mBlogParser = new BlogParser();
	}
	
	@Override
	public void getHomeBlogs(int index, int size)
	{
		mRequest.get(mBlogParser, BlogUrlApi.BLOG_SITE_HOME_PAGE, index, size);
	}
	
	@Override
	public void setBlogListener(BlogListener l)
	{
		mRequest.setBlogListener(l);
	}
	
	@Override
	public List<Blog> getBlogsByCategroy(String categroyId, int index, int size)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Blog getBlogContent(String id)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
}
