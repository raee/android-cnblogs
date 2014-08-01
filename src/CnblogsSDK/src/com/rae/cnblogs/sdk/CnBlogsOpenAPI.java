package com.rae.cnblogs.sdk;

import android.content.Context;

import com.rae.cnblogs.sdk.download.JsonBlogDownloader;
import com.rae.cnblogs.sdk.http.Downloader;
import com.rae.cnblogs.sdk.model.Blog;

public abstract class CnBlogsOpenAPI
{
	protected Downloader<Blog>	mBlogDownload;
	protected Context			mContext;
	
	public CnBlogsOpenAPI(Context context)
	{
		this.mContext = context;
	}
	
	public void setOnCnBlogsLoadListener(CnBlogsCallbackListener<Blog> listener)
	{
		if (mBlogDownload == null)
		{
			mBlogDownload = new JsonBlogDownloader(mContext);
		}
		mBlogDownload.setOnCnBlogCallbackListener(listener);
	}
	
	/**
	 * 检查接口是否实现
	 * 
	 * @throws CnBlogsException
	 */
	protected boolean checkBlogApiIsNull()
	{
		if (mBlogDownload == null || mBlogDownload.getCnBlogCallbackListener() == null)
		{
			mBlogDownload.onHttpRequestError(new CnBlogsException("请通过setOnCnBlogsLoadListener设置博客回调"));
			return true;
		}
		return false;
	}
	
	/**
	 * 获取首页博客
	 * 
	 * @param page
	 *            分页
	 */
	public abstract void getBlogs(int page);
	
	/**
	 * 获取分类博客
	 * 
	 * @param cateId
	 *            分类ID
	 * @param page
	 *            分页
	 */
	public abstract void getBlogs(String cateId, int page);
	
	/**
	 * 获取博客正文内容
	 * 
	 * @param model
	 *            博客实体
	 */
	public abstract void getBlogContent(Blog model);
	
}
