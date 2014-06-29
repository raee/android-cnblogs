package com.rae.cnblogs;

import com.rae.cnblogs.http.HttpRequest;
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
	
}
