package com.rae.cnblogs.i;

import java.util.List;

/**
 * 博客回调接口
 * 
 * @author Chenrui
 * 
 * @param <T>
 */
public interface BlogListener<T>
{
	/**
	 * 获取博客成功
	 * 
	 * @param result
	 */
	void onBlogSuccess(List<T> result);
	
	/**
	 * 获取博客时发生的错误
	 * 
	 * @param e
	 */
	void onError(BlogException e);
}
