package com.rae.cnblogs.sdk;

/**
 * 博客园统一错误回调接口
 * 
 * @author ChenRui
 * 
 */
public interface CnBlogsErrorListener
{
	/**
	 * 博客园失败返回
	 * 
	 * @param e
	 */
	void onLoadError(CnBlogsException e);
	
}
