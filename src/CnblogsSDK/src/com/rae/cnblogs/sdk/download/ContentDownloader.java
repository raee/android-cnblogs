package com.rae.cnblogs.sdk.download;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Xml;

import com.rae.cnblogs.sdk.CnBlogsException;
import com.rae.cnblogs.sdk.http.Downloader;
import com.rae.cnblogs.sdk.model.Blog;

/**
 * 博文下载器
 * 
 * @author ChenRui
 * 
 */
public class ContentDownloader extends Downloader<Blog>
{
	private Blog	mBlog;
	
	public void download(String url, Blog model)
	{
		super.download(url, model.getId());
		mBlog = model;
	}
	
	public ContentDownloader(Context context)
	{
		super(context);
	}
	
	@Override
	public void onHttpResponse(String xml)
	{
		List<Blog> result = new ArrayList<Blog>();
		XmlPullParser parser = Xml.newPullParser();
		
		try
		{
			parser.setInput(new StringReader(xml));
			int eventType = 0;
			while (eventType != XmlPullParser.END_DOCUMENT)
			{
				switch (eventType)
				{
					case XmlPullParser.TEXT:
						if (mBlog != null)
						{
							mBlog.setContent(parser.getText());
							info("获取博客正文：" + mBlog.getTitle());
							onCallback(result);
							return;
						}
					case XmlPullParser.START_TAG:
						String name = parser.getName();
						if (name.equals("string"))
						{
							result.add(mBlog);
						}
						break;
					default:
						break;
				}
				eventType = parser.next();
			}
		}
		catch (XmlPullParserException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
	}
	
}
