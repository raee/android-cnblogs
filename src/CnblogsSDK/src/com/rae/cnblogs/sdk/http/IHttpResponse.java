package com.rae.cnblogs.sdk.http;

import com.rae.cnblogs.sdk.CnBlogsException;

/**
 * http 响应回调
 * 
 * @author ChenRui
 * 
 */
public interface IHttpResponse
{
	/**
	 * 调用成功返回响应内容
	 * 
	 * @param html
	 *            响应内容
	 */
	void onHttpResponse(String html);
	
	/**
	 * 请求发生异常
	 * 
	 * @param exception
	 */
	void onHttpRequestError(CnBlogsException exception);
}
