package com.rae.cnblogs;

/**
 * 
 * @author 睿
 * 
 */
public class BlogFactory
{
	private BlogFactory()
	{
	}
	
	public static Blogs getFactory()
	{
		return new CnBlogs();
	}
}
