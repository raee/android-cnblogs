package com.rae.cnblogs;

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
	
	//	/**
	//	 * 分页获取分类下的博客
	//	 * 
	//	 * @param categroyId
	//	 * @param index
	//	 * @param size
	//	 */
	//	public abstract List<Blog> getBlogsByCategroy(String categroyId, int index,
	//			int size);
	//	
	//	/**
	//	 * 获取博客
	//	 * 
	//	 * @param id
	//	 */
	//	public abstract Blog getBlog(String id);
	
}
