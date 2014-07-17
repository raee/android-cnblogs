package com.rae.cnblogs.http;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import android.util.Log;

import com.rae.cnblogs.i.BlogException;
import com.rae.cnblogs.i.BlogListener;
import com.rae.cnblogs.parser.IBlogParser;
import com.rae.core.http.async.AsyncHttpClient;
import com.rae.core.http.async.AsyncHttpResponseHandler;

/**
 * 博客园Http请求，注意添加网络访问权限
 * 
 * @author Chenrui
 * 
 * @param <T>
 *            请求解析的数据类型
 */
public class HttpRequest<T> extends AsyncHttpResponseHandler
{
	private AsyncHttpClient		mClient;
	private IBlogParser<T>		mParser;
	protected BlogListener<T>	mListener;
	
	public HttpRequest()
	{
		mClient = new AsyncHttpClient();
	}
	
	/**
	 * 发起请求
	 * 
	 * @param url
	 *            请求路径
	 * @param parser
	 *            数据解析
	 * @param listener
	 *            回调监听
	 * @param params
	 *            Url参数
	 */
	public void get(String url, IBlogParser<T> parser, BlogListener<T> listener, Object... params)
	{
		if (listener == null)
		{
			throw new NullPointerException("必须设置BlogListener，用于数据回调。");
		}
		if (parser == null)
		{
			throw new NullPointerException("必须设置IBlogParser，用于数据解析");
		}
		
		url = convertUrl(url, params); // 转换Url
		this.mParser = parser;
		this.mListener = listener;
		Log.i("HTTP", url);
		mClient.get(url, this);
	}
	
	@Override
	public void onFailure(int code, Header[] header, byte[] html, Throwable e)
	{
		if (mListener != null)
		{
			BlogException exception = null;
			if (code == 0)
			{
				exception = new BlogException("网络没有连接！");
			}
			else
			{
				exception = new BlogException(code, e);
			}
			mListener.onError(exception);
		}
	}
	
	@Override
	public void onSuccess(int code, Header[] header, byte[] html)
	{
		if (mListener != null && mParser != null)
		{
			try
			{
				String xml = new String(html, Charset.forName("utf-8"));
				mListener.onBlogSuccess(mParser.onParse(xml)); // 开始解析数据
			}
			catch (BlogException e)
			{
				mListener.onError(e);
			}
		}
	}
	
	public void gc()
	{
		this.mClient = null;
		this.mListener = null;
		this.mParser = null;
		System.gc(); //通知系统回收资源
	}
	
	/**
	 * 自动转化URl
	 * 
	 * @return
	 */
	public String convertUrl(String url, Object... params)
	{
		// http://wcf.open.cnblogs.com/blog/post/body/@id
		String result = url;
		
		// 从路径中找出{}的匹配值
		String regx = "\\{\\w+\\}?";
		Pattern pattern = Pattern.compile(regx);
		Matcher match = pattern.matcher(url);
		int i = 0;
		while (match.find())
		{
			if (i >= params.length) break;
			String matchResult = match.group();
			// 按照序号替换调值
			result = result.replace(matchResult, params[i].toString());
			i++;
		}
		return result;
	}
}
