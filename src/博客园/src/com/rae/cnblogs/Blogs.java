package com.rae.cnblogs;

import java.util.List;

import com.rae.cnblogs.model.Blog;

/**
 * 博客接口
 * 
 * @author admin
 * 
 */
public abstract class Blogs
{
	
	/**
	 * 分页获取首页博客
	 * 
	 * @param index
	 * @param size
	 */
	public abstract void getHomeBlogs(int index, int size);
	
	/**
	 * 设置博客回调监听
	 * 
	 * @param l
	 */
	public abstract void setBlogListener(BlogListener l);
	
	/**
	 * 分页获取分类下的博客
	 * 
	 * @param categroyId
	 * @param index
	 * @param size
	 */
	public abstract List<Blog> getBlogsByCategroy(String categroyId, int index,
			int size);
	
	/**
	 * 获取博客正文
	 * 
	 * @param id
	 *            博客ID
	 */
	public abstract Blog getBlogContent(String id);
	
}
