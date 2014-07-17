package com.rae.cnblogs.data;

import java.util.List;

import com.rae.cnblogs.model.Blog;
import com.rae.cnblogs.model.BlogCategory;

/**
 * 本地SQLITE数据库访问接口
 * 
 * @author ChenRui
 * 
 */
public interface IDataProvider
{
	/**
	 * 博客是否已经存在数据中
	 * 
	 * @param blogId
	 *            博客ID
	 * @return
	 */
	boolean exitsBlog(String blogId);
	
	/**
	 * 添加一条博客
	 * 
	 * @param model
	 */
	void addBlogs(List<Blog> model);
	
	/**
	 * 更新一条博客
	 * 
	 * @param model
	 */
	void updateBlog(Blog model);
	
	/**
	 * 获取博客列表
	 * 
	 * @param page
	 *            分页码
	 * @return
	 */
	List<Blog> getBlogList(int page);
	
	/**
	 * 获取分类列表，已经排序
	 * 
	 * @param status
	 *            0：可订阅；<br>
	 *            1：正在显示；<br>
	 *            -1：所有；
	 * @return
	 */
	List<BlogCategory> getCategroy(int status);
	
	/**
	 * 更新分类
	 * 
	 * @param cateId
	 *            分类ID
	 * @param status
	 *            状态，100为不更新状态
	 * @param order
	 *            排序
	 */
	void updateCategroy(String cateId, int status, int order);
}
