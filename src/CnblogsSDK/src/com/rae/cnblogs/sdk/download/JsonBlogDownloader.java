package com.rae.cnblogs.sdk.download;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.model.Blog;


public class JsonBlogDownloader extends Downloader<Blog>
{
	
	public JsonBlogDownloader(Context context)
	{
		super(context);
	}
	
	@Override
	public void onHttpResponse(String html)
	{
		try
		{
			List<Blog> result = new ArrayList<Blog>();
			JSONObject obj = new JSONObject(html);
			JSONArray arr = obj.getJSONArray("data");
			for (int i = 0; i < arr.length(); i++)
			{
				Blog blog = new Blog();
				JSONObject item = arr.getJSONObject(i);
				blog.setId(item.getString("blog_id"));
				blog.setTitle(item.getString("title"));
				blog.setSummary(item.getString("content"));
				blog.setAutor(item.getString("author"));
				blog.setPostDate(item.getString("public_time"));
				blog.setViewCount(item.getString("hit"));
				blog.setCommentCount(item.getString("comment"));
				blog.setUrl(item.getString("blog_url"));
				blog.setBlogApp(item.getString("blogapp"));
				result.add(blog);
			}
			super.onCallback(result);
			
		}
		catch (JSONException e)
		{
			onHttpRequestError(new CnBlogsException("数据解析错误！", e));
		}
	}
	
}
