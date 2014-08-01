package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.download.Downloader;
import com.rae.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.sdk.model.Comment;

public abstract class CnBlogsOpenAPI
{
	protected Context	mContext;
	protected int		mPageSize	= 20;
	
	public CnBlogsOpenAPI(Context context)
	{
		this.mContext = context;
	}
	
	/**
	 * 获取博客下载器
	 * 
	 * @return
	 */
	protected abstract Downloader<Blog> getBlogDownloader();
	
	/**
	 * 获取博客评论载器
	 * 
	 * @return
	 */
	protected abstract Downloader<Comment> getCommentDownloader();
	
	/**
	 * 获取博客
	 * 
	 * @param l
	 *            回调监听
	 * @param index
	 *            页码
	 */
	public abstract void getCnblogs(CnBlogsCallbackListener<Blog> l, int index);
	
	/**
	 * 获取首页文章列表
	 * 
	 * @param l
	 *            回调监听
	 * @param index
	 *            页码
	 */
	public abstract void getRecentCnblogs(CnBlogsCallbackListener<Blog> l, int index);
	
	/**
	 * 10天内推荐排行
	 * 
	 * @param l
	 * @param size
	 *            获取条数
	 */
	public abstract void getTenDaysTopDiggPosts(CnBlogsCallbackListener<Blog> l, int size);
	
	/**
	 * 分页获取推荐博客列表
	 * 
	 * @param l
	 * @param index
	 *            页码
	 */
	public abstract void getRecommend(CnBlogsCallbackListener<Blog> l, int index);
	
	/**
	 * 48小时阅读排行
	 * 
	 * @param size
	 *            获取条数
	 */
	public abstract void get48HoursTopViewPosts(CnBlogsCallbackListener<Blog> l, int size);
	
	/**
	 * 获取文章评论
	 * 
	 * @param blogId
	 *            博客ID
	 * @param index
	 *            页码
	 */
	public abstract void getComments(CnBlogsCallbackListener<Comment> l, String blogId, int index);
	
	/**
	 * 获取博客
	 * 
	 * @param l
	 *            回调监听
	 * @param url
	 *            请求地址
	 * @param index
	 *            页码
	 */
	protected void getCnblogs(CnBlogsCallbackListener<Blog> l, String url, int index)
	{
		getBlogDownloader().setOnCnBlogCallbackListener(l);
		getBlogDownloader().download(url, index, mPageSize);
	}
	
}
