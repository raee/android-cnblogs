package com.rae.cnblogs.sdk.download;

import java.util.List;

import android.content.Context;
import android.util.Log;

import com.rae.cnblogs.sdk.CnBlogsCallbackListener;
import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.data.DataProvider;
import com.rae.cnblogs.sdk.data.IDataProvider;
import com.rae.cnblogs.sdk.http.IHttpResponse;
import com.rae.cnblogs.sdk.http.SyncHttpClient;

/**
 * 博客请求下载
 * 
 * @author ChenRui
 * @param <T>
 * 
 */
public abstract class Downloader<T> implements IHttpResponse {
	private CnBlogsCallbackListener<T> mCallbackListener;
	protected SyncHttpClient mClient;
	protected IDataProvider mDbProvider;

	public Downloader(Context context) {
		this.mClient = new SyncHttpClient(context, this);
		mDbProvider = DataProvider.getInstance(context);
	}

	protected void onCallback(List<T> result) {
		mCallbackListener.onLoadBlogs(result);
	}

	public void download(String url, Object... params) {
		mClient.get(url, params);
	}

	public void setOnCnBlogCallbackListener(CnBlogsCallbackListener<T> l) {
		this.mCallbackListener = l;
	}

	protected CnBlogsCallbackListener<T> getListener() {
		return mCallbackListener;
	}

	public CnBlogsCallbackListener<T> getCnBlogCallbackListener() {
		return this.mCallbackListener;
	}

	@Override
	public void onHttpRequestError(CnBlogsException exception) {
		this.mCallbackListener.onLoadError(exception);
	}

	protected void info(Object msg) {
		Log.i("cnblog", msg.toString());
	}

}
