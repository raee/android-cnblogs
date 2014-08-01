package com.rae.cnblogs.sdk.official;

import android.content.Context;

import com.rae.cnblogs.sdk.download.Downloader;
import com.rae.cnblogs.sdk.model.Blog;

/**
 * 博客列表
 * 
 * @author ChenRui
 * 
 */
public class BlogDownload extends Downloader<Blog>
{
	private CnblogsXmlParser	parser;
	
	public BlogDownload(Context context)
	{
		super(context);
		parser = new CnblogsXmlParser();
	}
	
	@Override
	public void onHttpResponse(String html)
	{
		parser.parser(getListener(), html);		// 解析Xml		
		
	}
	
}
