package com.rae.cnblogs.sdk.http;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;

import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.core.http.async.AsyncHttpClient;
import com.rae.core.http.async.ResponseHandlerInterface;

/**
 * 异步Http请求
 * 
 * @author ChenRui
 * 
 */
public class SyncHttpClient extends AsyncHttpClient

{
	
	private ResponseHandlerInterface	mResponseHandler;
	private IHttpResponse				mListener;
	private Context						mContext;
	
	public SyncHttpClient(Context context, IHttpResponse l)
	{
		this.mContext = context;
		this.mListener = l;
		this.mResponseHandler = new HttpResponseHandler(l);
	}
	
	public void get(String url, Object... params)
	{
		if (!HttpUtil.IsConnected(mContext)) //网络连通判断
		{
			CnBlogsException ex = new CnBlogsException("网络没有连接，请打开网络连接！");
			ex.setCode(404);
			mListener.onHttpRequestError(ex);
			return;
		}
		
		url = convertUrl(url, params); // 转换Url
		super.get(url, mResponseHandler);
		Log.i("cnblog", "url:" + url);
	}
	
	/**
	 * 自动转化URl
	 * 
	 * @return
	 */
	private String convertUrl(String url, Object... params)
	{
		String dateNow = DateFormat.format("yyyyMMddhhmmss", new Date()).toString();
		String result = url.replace("{date}", dateNow);
		
		// 从路径中找出{}的匹配值
		String regx = "\\{\\w+\\}?";
		Pattern pattern = Pattern.compile(regx);
		Matcher match = pattern.matcher(url);
		int i = 0;
		while (match.find())
		{
			if (i >= params.length) break;
			String matchResult = match.group();
			String val = params[i] == null ? "" : params[i].toString();
			result = result.replace(matchResult, val);// 按照序号替换调值
			i++;
		}
		return result;
	}
}
