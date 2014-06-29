package com.rae.cnblogs;

import java.util.List;

import com.rae.cnblogs.model.Blog;

public interface BlogListener
{
	/**
	 * 获取博客成功
	 * 
	 * @param result
	 */
	void onBlogSuccess(List<Blog> result);
	
	void onError(BlogException e);
}
