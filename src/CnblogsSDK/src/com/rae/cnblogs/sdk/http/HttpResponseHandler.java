package com.rae.cnblogs.sdk.http;

import org.apache.http.Header;

import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.core.http.async.AsyncHttpResponseHandler;

class HttpResponseHandler extends AsyncHttpResponseHandler
{
	private IHttpResponse	mListener;
	
	public HttpResponseHandler(IHttpResponse l)
	{
		this.mListener = l;
	}
	
	@Override
	public void onFailure(int status, Header[] arg1, byte[] data, Throwable e)
	{
		String msg = "";
		if (data != null && data.length > 0)
		{
			msg = new String(data);
		}
		
		mListener.onHttpRequestError(new CnBlogsException(msg, e));
	}
	
	@Override
	public void onSuccess(int status, Header[] arg1, byte[] data)
	{
		if (data == null || data.length <= 0)
		{
			mListener.onHttpRequestError(new CnBlogsException("服务器没有返回数据！"));
			return;
		}
		
		String html = new String(data);
		mListener.onHttpResponse(html);
	}
	
}