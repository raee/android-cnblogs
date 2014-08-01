package com.rae.cnblogs.sdk.data;

import java.util.List;

import com.rae.cnblogs.sdk.model.Blog;

public interface IDataProvider
{
	boolean existBlog(String id);
	
	void addOrUpdateBlogs(List<Blog> blogs);
	
	List<Blog> GetBlogs(int page);
	
	List<Blog> GetBlogs(String cateId, int page);
	
	void addBlog(Blog blog);
	
	void updateBlog(Blog blog);
}
