package com.rae.cnblogs.sdk;

import java.util.Calendar;

import android.content.Context;

import com.rae.cnblogs.sdk.download.Downloader;
import com.rae.cnblogs.sdk.download.JsonBlogDownloader;
import com.rae.cnblogs.sdk.model.Blog;
import com.rae.cnblogs.sdk.model.Constant;

/**
 * 其他博客园接口
 * 
 * @author ChenRui
 * 
 */
public class CnBlogsOtherOpenAPI
{
	private Downloader<Blog>	mDownloader	= null;
	
	public CnBlogsOtherOpenAPI(Context context)
	{
		mDownloader = new JsonBlogDownloader(context);
	}
	
	public void getCnblogs(CnBlogsCallbackListener<Blog> l, int page, String sinceID, String maxId, String cateId)
	{
		mDownloader.setOnCnBlogCallbackListener(l);
		long date = Calendar.getInstance().getTimeInMillis();
		mDownloader.download(Constant.OTHER_BLOGS_HOME, page, sinceID, maxId, cateId, date);
		
	}
	
}
