package com.rae.cnblogs.http;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.Header;

import com.rae.cnblogs.BlogException;
import com.rae.cnblogs.BlogListener;
import com.rae.cnblogs.model.Blog;
import com.rae.cnblogs.parser.IBlogParser;
import com.rae.core.http.async.AsyncHttpClient;
import com.rae.core.http.async.AsyncHttpResponseHandler;

public class HttpRequest extends AsyncHttpResponseHandler
{
	private AsyncHttpClient	mClient;
	private IBlogParser		mParser;
	protected BlogListener	mListener;
	
	public HttpRequest()
	{
		mClient = new AsyncHttpClient();
	}
	
	/**
	 * 设置博客回调监听
	 * 
	 * @param l
	 */
	public void setBlogListener(BlogListener l)
	{
		this.mListener = l;
	}
	
	public void get(IBlogParser parser, String url, Object... params)
	{
		if (mListener == null) throw new NullPointerException(
				"BlogListener不能为空，请通过setBlogListener()方法设置");
		url = convertUrl(url, params); // 转换Url
		this.mParser = parser;
		mClient.get(url, this);
	}
	
	@Override
	public void onFailure(int code, Header[] arg1, byte[] arg2, Throwable e)
	{
		if (mListener != null) mListener.onError(new BlogException(code, e));
	}
	
	@Override
	public void onSuccess(int code, Header[] arg1, byte[] data)
	{
		if (mListener != null && mParser != null)
		{
			List<Blog> result = mParser.onParse(new String(data));
			mListener.onBlogSuccess(result);
		}
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
