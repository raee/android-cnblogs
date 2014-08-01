package com.rae.cnblogs.sdk.http;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.CnBlogsException;

/**
 * 博客请求下载
 * 
 * @author ChenRui
 * @param <T>
 * 
 */
public abstract class Downloader<T> implements IHttpResponse
{
	private CnBlogsCallbackListener<T>	mCallbackListener;
	protected SyncHttpClient			mClient;
	
	public Downloader(Context context)
	{
		this.mClient = new SyncHttpClient(context, this);
	}
	
	protected void onCallback(List<T> result)
	{
		mCallbackListener.onLoadBlogs(result);
	}
	
	protected void download(String url, Object... params)
	{
		mClient.get(url, params);
	}
	
	public void setOnCnBlogCallbackListener(CnBlogsCallbackListener<T> l)
	{
		this.mCallbackListener = l;
	}
	
	public CnBlogsCallbackListener<T> getCnBlogCallbackListener()
	{
		return this.mCallbackListener;
	}
	
	@Override
	public void onHttpRequestError(CnBlogsException exception)
	{
		this.mCallbackListener.onLoadError(exception);
	}
	
	protected void info(Object msg)
	{
		Log.i("cnblog", msg.toString());
	}
	
}
