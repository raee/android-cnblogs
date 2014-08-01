package com.rae.cnblogs.sdk.model;

/**
 * 博客接口地址参数说明：
 * 
 * @index 获取当前页的索引
 * @size 获取分页个数
 * 
 * @author admin
 * 
 */
public final class Constant
{
	public static final String	BLOG_SITE_HOME_PAGE		= "http://cnblogs.davismy.com/Handler.ashx?op=GetTimeLine&page={page}&since_id={sinceId}&max_id={maxId}&channelpath={cateId}&t={date}";
	
	/**
	 * 获取首页文章列表
	 * 
	 * @index
	 * @size
	 */
	public static final String	CNBLOGS_RECENT			= "http://wcf.open.cnblogs.com/blog/sitehome/recent/{size}";
	
	/**
	 * 分页获取首页文章列表
	 * 
	 * @index
	 * @size
	 */
	public static final String	CNBLOGS_PAGE			= "http://wcf.open.cnblogs.com/blog/sitehome/paged/{index}/{size}";
	
	/**
	 * 获取文章内容
	 * 
	 * @id
	 */
	public static final String	CNBLOGS_CONTENT			= "http://wcf.open.cnblogs.com/blog/post/body/{id}";
	
	/**
	 * 48小时阅读排行
	 * 
	 * @size
	 */
	public static final String	CNBLOGS_HOUR_READING	= "http://wcf.open.cnblogs.com/blog/48HoursTopViewPosts/{size}";
	
	/**
	 * 分页获取推荐博客列表
	 * 
	 * @index
	 * @size
	 */
	public static final String	CNBLOGS_RECOMMENT_PAGE	= "http://wcf.open.cnblogs.com/blog/bloggers/recommend/{index}/{size}";
	
	/**
	 * 根据作者名搜索博主
	 * 
	 * @name
	 */
	public static final String	CNBLOGS_SEARCH_USER		= "http://wcf.open.cnblogs.com/blog/bloggers/search?t={name}";
	
	/**
	 * 分页获取文章评论
	 * 
	 * @id 博客ID
	 * @index
	 * @size
	 */
	public static final String	CNBLOGS_COMMENETS_PAGE	= "http://wcf.open.cnblogs.com/blog/post/{id}/comments/{index}/{size}";
	
	/**
	 * 10天内推荐排行
	 * 
	 * @size
	 */
	public static final String	CNBLOGS_DAY_READING		= "http://wcf.open.cnblogs.com/blog/TenDaysTopDiggPosts/{size}";
	
	/**
	 * 分页获取个人博客文章列表
	 * 
	 * @blogapp
	 * @index
	 * @size
	 */
	public static final String	CNBLOGS_HOUR_STRING		= "http://wcf.open.cnblogs.com/blog/u/{blogapp}/posts/{index}/{size}";
}
