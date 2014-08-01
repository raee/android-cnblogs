package com.rae.cnblogs.sdk;

import java.util.List;

import com.rae.cnblogs.sdk.model.Blog;

/**
 * 博客回调
 * 
 * @author ChenRui
 * 
 */
public interface CnBlogsCallbackListener<T> extends CnBlogsErrorListener
{
	/**
	 * 加载博客成功
	 * 
	 * @param result
	 */
	void onLoadBlogs(List<T> result);
	
}
