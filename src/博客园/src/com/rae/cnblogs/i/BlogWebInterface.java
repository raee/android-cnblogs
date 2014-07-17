package com.rae.cnblogs.i;

import java.util.List;

import android.content.Context;
import android.webkit.JavascriptInterface;

import com.rae.cnblogs.model.Blog;

/**
 * 查看博客的WebView脚本交互接口， object名称为：android
 * 
 * @author ChenRui
 * 
 */
public class BlogWebInterface implements BlogListener<Blog>
{
	
	private String	mBlogId;
	private boolean	mIsLoadding	= false;
	private String	mBlogContent;
	private Context	mContext;
	
	public BlogWebInterface(Context context)
	{
		this.mContext = context;
	}
	
	/**
	 * 设置当前查看的博客ID
	 * 
	 * @param id
	 */
	public void setBlogID(String id)
	{
		this.mBlogId = id;
	}
	
	// 获取博客正文 html
	@JavascriptInterface
	public String getContent()
	{
		mIsLoadding = true;
		BlogFactory.getCnBlogsApi().getBlogContent(this, mBlogId);
		while (true)
		{
			if (mIsLoadding == false && mBlogContent != null)
			{
				return mBlogContent;
			}
		}
	}
	
	@Override
	public void onBlogSuccess(List<Blog> result)
	{
		mIsLoadding = false;
		if (result.size() > 0)
		{
			mBlogContent = result.get(0).getContent();
		}
		else
		{
			mBlogContent = "加载博客失败！";
		}
	}
	
	@Override
	public void onError(BlogException e)
	{
		mIsLoadding = false;
		mBlogContent = "加载博客失败！" + e.getMessage();
		
	}
}
