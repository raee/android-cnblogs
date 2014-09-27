package com.rae.cnblogs.sdk.data;

import java.util.List;

import com.rae.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.sdk.model.Category;

/**
 * 博客园数据库接口
 * 
 * @author ChenRui
 * 
 */
public interface IDataProvider {
	/**
	 * 获取分类
	 * 
	 * @return
	 */
	List<Category> getCategories();

	boolean existBlog(String id);

	void addOrUpdateBlogs(List<Blog> blogs);

	List<Blog> GetBlogs(int page);

	List<Blog> GetBlogs(String cateId, int page);

	Blog getBlog(String blogId);

	void addBlog(Blog blog);

	void updateBlog(Blog blog);
}
