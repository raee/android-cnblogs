//package com.rae.cnblogs.i;
//
//import java.util.List;
//
//import com.rae.cnblogs.model.Blog;
//
///**
// * 博客接口,通过调用setBlogListener(listener);来实现数据回调。
// * 
// * @author admin
// * 
// */
//public abstract class AbsBlogOpenAPI
//{
//	
//	/**
//	 * 分页获取首页博客 | 官方接口
//	 * 
//	 * @param index
//	 * @param size
//	 */
//	public abstract void getHomeBlogs(BlogListener listener, int index, int size);
//	
//	/**
//	 * 获取博客正文 | 官方接口
//	 * 
//	 * @param id
//	 *            博客ID
//	 */
//	public abstract void getBlogContent(BlogListener listener, String id);
//	
//	/**
//	 * 获取48小时阅读排行 | 官方接口
//	 * 
//	 * @return
//	 */
//	public abstract List<Blog> get48HoursBlogs(BlogListener listener);
//	
//	/**
//	 * 获取10天内推荐排行 | 官方接口
//	 */
//	public abstract void get10DaysBlogs(BlogListener listener);
//	
//	/**
//	 * 分页获取推荐博客 | 官方接口
//	 * 
//	 * @param index
//	 * @param size
//	 * @return
//	 */
//	public abstract List<Blog> getRecommend(BlogListener listener, int index, int size);
//	
//	/**
//	 * 搜索博主 | 官方接口
//	 * 
//	 * @param name
//	 *            博主名称
//	 */
//	public abstract void searchUser(String name);
//	
//	/**
//	 * 分页获取博客评论 | 官方接口
//	 * 
//	 * @param blogId
//	 *            博客ID
//	 * @param index
//	 *            页码
//	 * @param size
//	 *            分页数
//	 */
//	public abstract void getComment(String blogId, int index, int size);
//	
//	/**
//	 * 分页获取博主的博客 | 官方接口
//	 * 
//	 * @param userId
//	 *            博主ID
//	 * @param index
//	 *            页码
//	 * @param size
//	 *            分页数
//	 */
//	public abstract void getUserBlogs(BlogListener listener, String userId, int index, int size);
//	
//	/**
//	 * 分页获取分类下的博客
//	 * 
//	 * @param categroyId
//	 * @param index
//	 * @param size
//	 */
//	public abstract List<Blog> getBlogsByCategroy(BlogListener listener, String categroyId, int index, int size);
//	
//}
