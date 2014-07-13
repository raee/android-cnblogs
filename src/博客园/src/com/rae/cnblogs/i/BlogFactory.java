package com.rae.cnblogs.i;

/**
 * 博客工厂
 * 
 * @author Chenrui
 * 
 */
public class BlogFactory
{
	private static Blogs	blogs;
	
	private BlogFactory()
	{
	}
	
	public static Blogs getCnBlogsApi()
	{
		if (blogs == null) blogs = new CnBlogs();
		return blogs;
	}
}
